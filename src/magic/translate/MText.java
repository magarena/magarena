package magic.translate;

import groovy.json.StringEscapeUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import magic.data.GeneralConfig;
import magic.data.settings.BooleanSetting;
import magic.utility.MagicFileSystem;
import magic.utility.MagicSystem;

public final class MText {
    private MText() { }

    private static final Logger LOGGER = Logger.getLogger(MText.class.getName());

    private static final String UTF_CHAR_SET = "UTF-8";
    private static final String HEADER_CHAR = "¦";

    // Not sure if it is a bug or by design but if no UTF character is written
    // then (on Windows 7 anyway) it ignores UTF_CHAR_SET and encodes as ANSI.
    // If you subsequently overwrite the template strings with translations that do
    // use unicode characters no error is thrown but the translations are not loaded
    // either. Therefore since the template file uses a prefix character to highlight
    // untranslated strings in the UI use a unicode character to force correct encoding.
    private static final String UTF_PREFIX = "\u25AB"; // small white square ▫

    // Delimiter used to separate an abbreviation and the whole word/phrase.
    // Needs to be something that will never appear in a normal English sentence.
    private static final String ABBREVIATOR = "|+";

    private static final CRC32 crc = new CRC32();
    private static final Map<Long, String> translationsMap = new HashMap<>();
    private static final Map<Long, String> annotations = new HashMap<>();

    private static boolean useCustomFonts = false;

    static {
        try {
            loadTranslationFile();
        } catch (Exception ex) {
            Logger.getLogger(MText.class.getName()).log(Level.WARNING, null, ex);
        }
    }

    /**
     * Converts (English) string into CRC32 number which is used
     * as the mapping ID to identify the equivalent translation.
     */
    private static Long getStringId(final String aString) {
        crc.reset();
        try {
            crc.update(aString.getBytes(UTF_CHAR_SET));
            return crc.getValue();
        } catch (UnsupportedEncodingException ex) {
            System.err.println(ex);
            translationsMap.clear();
            return 0L;
        }
    }

    private static String getDisplayText(String text) {
        return text.contains(ABBREVIATOR)
            ? text.substring(0, text.indexOf(ABBREVIATOR))
            : text;
    }

    private static boolean isTranslating() {
        return !translationsMap.isEmpty();
    }

    public static final String get(final String aString, final Object... args) {
        if (isTranslating()) {
            final Long stringId = getStringId(aString);
            if (translationsMap.containsKey(stringId)) {
                return String.format(translationsMap.get(stringId), args);
            }
        }
        return getDisplayText(String.format(aString, args));
    }

    public static final String get(final String aString) {
        if (isTranslating()) {
            final Long stringId = getStringId(aString);
            if (translationsMap.containsKey(stringId)) {
                return translationsMap.get(stringId);
            }
        }
        return getDisplayText(aString);
    }

    /**
     * Returns translated string enclosed in {@literal <html>...</html>} tags.
     *
     * This is useful for automatically wrapping long strings.
     */
    public static final String asHtml(final String aString) {
        return "<html>" + get(aString) + "</html>";
    }

    private static Map<Long, String> getStringsMapFromFile(final File txtFile, final boolean unescape) throws FileNotFoundException {
        final Map<Long, String> stringsMap = new LinkedHashMap<>();
        try (final Scanner sc = new Scanner(txtFile, UTF_CHAR_SET)) {
            while (sc.hasNextLine()) {
                final String line = sc.nextLine().trim();
                if (line.startsWith("#") || line.isEmpty()) {
                    // ignore comments and blank lines.
                    continue;
                }
                if (line.startsWith(HEADER_CHAR)) {
                    parseHeaderLine(line, txtFile.getName());
                } else {
                    int equalsChar = line.indexOf('=');
                    long stringId = Long.valueOf(line.substring(0, equalsChar).trim());
                    String translation = line.substring(equalsChar + 1).trim();
                    stringsMap.put(stringId, unescape ? StringEscapeUtils.unescapeJava(translation) : translation);
                }
            }
        }
        return stringsMap;
    }

    private static void parseHeaderLine(String text, String fileName) {
        String[] values = text.substring(1).split(HEADER_CHAR);
        try {
            // version = values[0];
            useCustomFonts = Boolean.valueOf(Integer.valueOf(values[1]) == 1);
        } catch (ArrayIndexOutOfBoundsException ex) {
            LOGGER.log(Level.INFO, String.format(
                "Parsing header line in '%s' at item : %s",
                fileName, ex.getMessage())
            );
        } catch (RuntimeException ex) {
            LOGGER.log(Level.WARNING, String.format(
                "Error parsing header line in '%s' : %s",
                fileName, ex.getMessage())
            );
        }
    }

    public static Map<Long, String> getUnescapedStringsMap(final File txtFile) throws FileNotFoundException {
        return getStringsMapFromFile(txtFile, true);
    }

    public static Map<Long, String> getEscapedStringsMap(final File txtFile) throws FileNotFoundException {
        return getStringsMapFromFile(txtFile, false);
    }

    public static void loadTranslationFile() throws FileNotFoundException {
        useCustomFonts = false;
        translationsMap.clear();
        final String language = GeneralConfig.getInstance().getTranslation();
        if (language.isEmpty() == false) {
            final Path dirPath = MagicFileSystem.getDataPath(MagicFileSystem.DataPath.TRANSLATIONS);
            final File txtFile = dirPath.resolve(language + ".txt").toFile();
            translationsMap.putAll(getUnescapedStringsMap(txtFile));
        } else {
            useCustomFonts = true;
        }
    }

    /**
     * Returns the names of all classes in specified package (including sub-packages).
     */
    public static List<String> getClassNamesInPackage(final File jarFile, String packageName) throws IOException {

        final List<String> classes = new ArrayList<>();

        if (jarFile == null || !jarFile.exists() || !jarFile.isFile()) {
            throw new IOException("Unable to locate JAR file!\n\nTo manually specify the location please use the '-DjarFile' VM option.");
        }

        try (JarInputStream jarStream = new JarInputStream(new FileInputStream(jarFile))) {
            packageName = packageName.replaceAll("\\.", "/");
            while (true) {
                final JarEntry jarEntry = jarStream.getNextJarEntry();
                if (jarEntry == null) {
                    break;
                }
                final String entryName = jarEntry.getName();
                if (entryName.startsWith(packageName) && entryName.endsWith(".class")) {
                    classes.add(entryName.replaceAll("/", "\\."));
                }
            }
        }

        return classes;
    }

    /**
     * Use reflection to find all _S* strings.
     */
    public static Map<Long, String> getUiStringsMap() throws URISyntaxException, IOException {

        final Map<Long, String> stringsMap = new LinkedHashMap<>();
        annotations.clear();

        for (final String c : getClassNamesInPackage(MagicSystem.getJarFile(), "magic")) {
            final String className = c.substring(0, c.length() - ".class".length());
            try {
                for (final Field f : Class.forName(className).getDeclaredFields()) {

                    final boolean isFieldValid =
                            f.getType() == String.class
                            && f.getName().startsWith("_S")
                            && Modifier.isStatic(f.getModifiers());   // prevents a UnsafeObjectFieldAccessorImpl error.

                    if (isFieldValid) {
                        f.setAccessible(true);
                        try {
                            final String fieldValue = (String) f.get(null);
                            final Long stringId = getStringId(fieldValue);
                            final String stringValue = UTF_PREFIX + StringEscapeUtils.escapeJava(getDisplayText(fieldValue));
                            if (stringsMap.containsKey(stringId) == false) {
                                stringsMap.put(stringId, stringValue);
                                if (f.getAnnotation(StringContext.class) != null) {
                                    annotations.put(stringId, f.getAnnotation(StringContext.class).eg());
                                }
                            } else if (stringValue.equals(stringsMap.get(stringId)) == false) {
                                throw new RuntimeException(
                                        "Failed to generate translation file because the following strings have the same CRC32 value:-\n" +
                                        stringValue + "\n" + stringsMap.get(stringId));
                            }
                        } catch (IllegalAccessException ex) {
                            System.err.println(ex);
                        }
                    }
                }
            } catch (ClassNotFoundException ex) {
                System.err.println(ex);
            }
        }

        return stringsMap;
    }

    private static String getHeaderLineData() {
        StringBuilder sb = new StringBuilder();
        sb.append(HEADER_CHAR).append(MagicSystem.VERSION);
        sb.append(HEADER_CHAR).append(useCustomFonts ? 1 : 0);
        return sb.toString();
    }

    public static void createTranslationFile(File txtFile, Map<Long, String> stringsMap) throws FileNotFoundException, UnsupportedEncodingException {
        try (final PrintWriter writer = new PrintWriter(txtFile, UTF_CHAR_SET)) {
            writer.print(getHeaderLineData() + "\n");
            for (Map.Entry<Long, String> entry : stringsMap.entrySet()) {
                final Long key = entry.getKey();
                if (annotations.containsKey(key)) {
                    writer.print(String.format("# %010d eg. %s\n", key, annotations.get(key)));
                }
                // CRC32 function returns 32 bit long = max 10 numerals. Pad if smaller.
                writer.print(String.format("%010d = %s\n", key, entry.getValue()));
            }
        }
    }

    public static void createTranslationFIle(File txtFile) throws URISyntaxException, IOException {
        createTranslationFile(txtFile, getUiStringsMap());
    }

    public static void disableTranslations() {
        translationsMap.clear();
    }

    public static boolean isEnglish() {
        return translationsMap.isEmpty();
    }

    public static String getTranslationVersion(String lang) {
        if (!"English".equals(lang)) {
            File file = MagicFileSystem.getTranslationFile(lang);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), UTF_CHAR_SET))) {
                String line = br.readLine();
                if (line != null && line.startsWith(HEADER_CHAR)) {
                    return line.substring(1).trim().split(HEADER_CHAR)[0];
                }
            } catch (IOException ex) {
                LOGGER.log(Level.WARNING, null, ex);
            }
        }
        return "";
    }

    public static boolean canUseCustomFonts() {
        return useCustomFonts && GeneralConfig.get(BooleanSetting.CUSTOM_FONTS);
    }

    /**
     * This is used to create a string consisting of an abbreviation and it's
     * associated word or phrase. Only the abbreviation will be displayed in the
     * UI or require translation whereas the whole string will be used to
     * generate the CRC ID for the translation file entry.
     *
     * An example of where this is necessary is when "P" is used to mean
     * games [P]layed or a creature's [P]ower. If the string "P" is used on its
     * own then the CRC will be the same for both cases which is fine in English
     * but the chances of "Played" and "Power" using the same abbreviation when
     * translated is very unlikely. By combining the abbreviation and its
     * meaning there is a much less chance of the CRCs being the same.
     */
    public static String abbreviate(String s1, String s2) {
        return s1 + ABBREVIATOR + s2;
    }
}
