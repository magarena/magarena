package magic.ui.theme;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.ImageIcon;
import magic.ui.ImageFileIO;
import magic.ui.MagicImages;
import magic.utility.FileIO;
import magic.utility.MagicFileSystem;

public class CustomTheme extends AbstractTheme {

    private static final String THEME_PROPERTIES_FILE="theme.properties";

    private final File file;
    private ZipFile zipFile;
    private final Map<String, BufferedImage> imagesMap;

    public CustomTheme(final File file) {
        super(getThemeName(file));

        addToTheme(ICON_SMALL_BATTLEFIELD, null);
        addToTheme(ICON_SMALL_COMBAT, null);

        this.file = file;
        imagesMap = new HashMap<>();
    }

    public static String getThemeName(final File aFile) {
        final String name = aFile.getName();
        if (aFile.isFile()) {
            if (name.endsWith("_theme.zip")) {
                return name.substring(0, name.length() - "_theme.zip".length());
            }
            if (name.endsWith(".zip")) {
                return name.substring(0, name.length() - ".zip".length());
            }
        }
        if (name.endsWith("_theme")) {
            return name.substring(0, name.length() - "_theme".length());
        }
        return name;
    }

    private void parseEntry(final String key,final String value) {
        if (value.isEmpty()) {
            return;
        }
        final int index=key.indexOf('_');
        if (index<0) {
            return;
        }
        Object typeValue=null;
        final String type=key.substring(0,index);
        switch (type) {
            case "value":
                typeValue = Integer.parseInt(value);
                break;
            case "color":
                final String[] parts = value.split(",");
                final int r = Integer.parseInt(parts[0], 16);
                final int g = Integer.parseInt(parts[1], 16);
                final int b = Integer.parseInt(parts[2], 16);
                if (parts.length == 4) {
                    typeValue = new Color(r, g, b, Integer.parseInt(parts[3], 16));
                } else if (parts.length == 3) {
                    typeValue = new Color(r, g, b);
                }
                break;
            case "texture":
                typeValue = value;
                break;
            case "icon":
                typeValue = new ImageIcon(loadImage(value));
                break;
            case "option":
                typeValue = Boolean.parseBoolean(value);
                break;
        }
        // add or replace existing/default value.
        if (typeValue != null) {
            addToTheme(key, typeValue);
        }
    }

    private InputStream getInputStream(final String filename) {
        try { //get input stream
            if (zipFile!=null) {
                final ZipEntry zipEntry=zipFile.getEntry(filename);
                return zipFile.getInputStream(zipEntry);
            } else {
                return new FileInputStream(new File(file,filename));
            }
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to obtain input stream from " + filename);
            return null;
        }
    }

    private BufferedImage loadImage(final String filename) {
        final InputStream ins = getInputStream(filename);
        return ImageFileIO.toImg(ins, MagicImages.MISSING_BIG);
    }

    @Override
    public void load() {
        if (file.isFile()) {
            try { //create zip file
                zipFile=new ZipFile(file);
            } catch (final IOException ex) {
                System.err.println("ERROR! Unable to create ZipFile from " + file);
            }
        }

        final InputStream inputStream=getInputStream(THEME_PROPERTIES_FILE);
        final Properties properties=FileIO.toProp(inputStream);

        for (final Map.Entry<Object,Object> entry : properties.entrySet()) {
            parseEntry(entry.getKey().toString(),entry.getValue().toString().trim());
        }
    }

    static File getThemeFile(String name) {
        final Path path = MagicFileSystem.getThemesPath();
        if (path.resolve(name).toFile().exists()) {
            return path.resolve(name).toFile();
        }
        if (path.resolve(name + "_theme").toFile().exists()) {
            return path.resolve(name + "_theme").toFile();
        }
        if (path.resolve(name + ".zip").toFile().exists()) {
            return path.resolve(name + ".zip").toFile();
        }
        if (path.resolve(name + "_theme.zip").toFile().exists()) {
            return path.resolve(name + "_theme.zip").toFile();
        }
        return null;
    }

    static Theme loadTheme(String name) {
        if (getThemeFile(name) != null) {
            final Theme t = new CustomTheme(getThemeFile(name));
            t.load();
            return t;
        }
        return null;
    }

    private BufferedImage getImage(String imageKey, String filename) {
        if (!imagesMap.containsKey(imageKey)) {
            imagesMap.put(imageKey, loadImage(filename));
        }
        return imagesMap.get(imageKey);
    }

    @Override
    public BufferedImage getTexture(String name) {
        return hasValue(name)
            ? getImage(name, getStringValue(name))
            : MagicImages.MISSING_BIG;
    }

}
