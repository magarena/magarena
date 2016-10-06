package magic.utility;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.model.IRenderableCard;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 * Utility class for useful or common file-system related tasks.
 *
 */
public final class MagicFileSystem {

    private MagicFileSystem() {}

    // card images
    public static final String CARD_IMAGE_FOLDER = "cards";
    public static final String TOKEN_IMAGE_FOLDER = "tokens";
    private static final String CARD_IMAGE_EXT = ".jpg";

    private enum ImagesPath {

        CARDS(CARD_IMAGE_FOLDER),
        TOKENS(TOKEN_IMAGE_FOLDER),
        CUSTOM("custom"),
        CROPS("crops");

        private final String directoryName;

        private ImagesPath(final String directoryName) {
            this.directoryName = directoryName;
        }

        public Path getPath() {
            return GeneralConfig.getInstance().getCardImagesPath().resolve(directoryName);
        }
    }

    // Top level install directory containing exe, etc.
    public static final Path INSTALL_PATH;
    static {
        if (System.getProperty("magarena.dir", "").isEmpty()) {
            INSTALL_PATH = Paths.get(System.getProperty("user.dir"));
        } else {
            INSTALL_PATH = Paths.get(System.getProperty("magarena.dir"));
        }
    }

    public static final String DATA_DIRECTORY_NAME = System.getProperty("data.dir", "Magarena");
    private static final Path DATA_PATH = INSTALL_PATH.resolve(DATA_DIRECTORY_NAME);

    public enum DataPath {

        DECKS("decks"),
        MODS("mods"),
        SCRIPTS("scripts"),
        SCRIPTS_MISSING("scripts_missing"),
        SCRIPTS_ORIG("scripts_orig"),
        LOGS("logs"),
        DUELS("duels"),
        PLAYERS("players"),
        AVATARS("avatars"),
        FIREMIND("firemind"),
        SAVED_GAMES("saved_games"),
        TRANSLATIONS("translations"),
        IMAGES("images"),
        REPORTS("reports"),
        THEMES("themes")
        ;

        private final Path directoryPath;

        private DataPath(final String directoryName) {
            directoryPath = DATA_PATH.resolve(directoryName);
            MagicFileSystem.verifyDirectoryPath(directoryPath);
        }

        public Path getPath() {
            return directoryPath;
        }

    }

    private static final FileFilter THEME_FILE_FILTER = (final File file) ->
        file.isDirectory() || (file.isFile() && file.getName().endsWith(".zip"));

    /**
     * Returns the main data directory.
     * <p>
     * Generally, this will contain sub-directories for the
     * different categories of data that can be generated.
     */
    public static Path getDataPath() {
        return DATA_PATH;
    }

    /**
     * Returns a pre-defined data sub-directory of main data path.
     */
    public static Path getDataPath(final DataPath directory) {
        return directory.getPath();
    }

    private static Path getImagesPath(final ImagesPath imageType) {
        return imageType.getPath();
    }

    private static String getImageFilename(final MagicCardDefinition card) {
        return card.getImageName() + CARD_IMAGE_EXT;
    }

    /**
     * Returns a File object representing the given card's image file.
     */
    public static File getCardImageFile(final MagicCardDefinition card) {
        final Path imageDirectory = card.isToken() ?
                getImagesPath(ImagesPath.TOKENS) :
                getImagesPath(ImagesPath.CARDS);
        return new File(imageDirectory.toFile(), getImageFilename(card));
    }

    /**
     * Returns a File object representing the given card's cropped image file.
     */
    public static File getCroppedCardImageFile(final IRenderableCard cardDef) {
        final Path imageDirectory = getImagesPath(ImagesPath.CROPS);
        return new File(imageDirectory.toFile(), cardDef.getImageName() + ".jpg");
    }

    public static File getCustomCardImageFile(final IRenderableCard cardDef) {
        final Path imageDirectory = getImagesPath(ImagesPath.CUSTOM);
        return new File(imageDirectory.toFile(), cardDef.getImageName() + ".jpg");
    }


    /**
     * Deletes all directory contents and then directory itself.
     */
    public static void deleteDirectory(final Path root) {
        if (Files.exists(root) == false) {
            return;
        }
        try {
            Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (exc == null){
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                    throw exc;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void serializeStringList(final List<String> list, final File targetFile) {
        try (final FileOutputStream fos = new FileOutputStream(targetFile);
            final ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(list);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<String> deserializeStringList(final File sourceFile) {
        try (final FileInputStream fis = new FileInputStream(sourceFile);
            final ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (List<String>)ois.readObject();
        } catch (IOException|ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void verifyDirectoryPath(final Path path) {
        if (!path.toFile().exists()) {
            try {
                Files.createDirectory(path);
            } catch (IOException ex) {
                throw new RuntimeException(String.format("Failed to create '%s'.", path), ex);
            }
        }
    }

    public static File[] getSortedScriptFiles(final File scriptsDirectory) {
        final File[] files = scriptsDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".txt");
            }
        });
        Arrays.sort(files);
        return files;
    }

    public static List<String> getTranslationFilenames() {
        final List<String> filenames = new ArrayList<>();
        final Path langPath = getDataPath(DataPath.TRANSLATIONS);
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(langPath, "*.txt")) {
            for (Path p : ds) {
                filenames.add(FilenameUtils.getBaseName(p.getFileName().toString()));
            }
            Collections.sort(filenames);
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return filenames;
    }

    public static void deleteGeneralConfigFile() {
        getDataPath().resolve(GeneralConfig.CONFIG_FILENAME).toFile().delete();
    }

    /**
     * Should return the directory containing the current installation of Magarena.
     * <p>
     * The idea being that a new version of Magarena would most likely be
     * installed to a new directory at the same level as the previous version,
     * so it would display the previous version all ready to select & import.
     */
    public static Path getDefaultImportDirectory() {
        final Path p = getDataPath().getParent().getParent();
        if (p == null) {
            // triggered if using a single relative path for -Dmagarena.dir.
            return getDataPath().getParent();
        }
        return p;
    }

    /**
     * Confirms if two paths point to the same location regardless of whether
     * the path is relative or absolute.
     */
    public static boolean isSamePath(Path p1, Path p2) {
        return p1.toAbsolutePath().equals(p2.toAbsolutePath());
    }

    /**
     * Determines whether p2 is the same as or a sub-directory of p1.
     */
    public static boolean directoryContains(Path p1, Path p2) {
        if (p1 == null || p2 == null) {
            return false;
        }
        if (isSamePath(p1, p2)) {
            return true;
        } else {
            return directoryContains(p1, p2.getParent());
        }
    }

    public static Path getCustomImagesPath() {
        return getImagesPath(ImagesPath.CUSTOM);
    }

    public static Path getGameplayReportDirectory() {
        return getDataPath(DataPath.REPORTS).resolve("gameplay");
    }

    public static void clearGameplayReportDirectory() throws IOException {
        verifyDirectoryPath(getGameplayReportDirectory());
        FileUtils.cleanDirectory(getGameplayReportDirectory().toFile());
    }

    public static boolean isCardImageMissing(MagicCardDefinition aCard) {
        if (getCustomCardImageFile(aCard).exists()) {
            return false;
        }
        if (getCroppedCardImageFile(aCard).exists()) {
            return false;
        }
        if (getCardImageFile(aCard).exists()) {
            return false;
        }
        return true;
    }

    public static File[] getThemes() {
        return getDataPath(DataPath.THEMES).toFile().listFiles(THEME_FILE_FILTER);
    }

    public static Path getThemesPath() {
        return getDataPath(DataPath.THEMES);
    }

    public static Path getBackgroundImagePath() {
        return getDataPath(DataPath.MODS).resolve("background.image");
    }

    public static File getBackgroundImageFile() {
        return getBackgroundImagePath().toFile();
    }
}
