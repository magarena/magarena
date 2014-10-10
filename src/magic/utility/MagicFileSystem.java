package magic.utility;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import javax.swing.JOptionPane;
import magic.MagicMain;
import magic.MagicUtility;
import magic.data.CardImagesProvider;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;

/**
 * Utility class for useful or common file-system related tasks.
 *
 */
public final class MagicFileSystem {
    private MagicFileSystem() {}

    // card images
    public static final String CARD_IMAGE_FOLDER = "cards";
    public static final String TOKEN_IMAGE_FOLDER = "tokens";
    private static final String CARD_IMAGE_EXT = CardImagesProvider.IMAGE_EXTENSION;

    private enum ImagesPath {

        CARDS("cards"),
        TOKENS("tokens");

        private final GeneralConfig CONFIG = GeneralConfig.getInstance();
        private final String directoryName;

        private ImagesPath(final String directoryName) {
            this.directoryName = directoryName;
        }

        public Path getPath() {
            return CONFIG.getCardImagesPath().resolve(directoryName);
        }
    }

    // Top level install directory containing exe, etc.
    private static final Path INSTALL_PATH;
    static {
        if (System.getProperty("magarena.dir", "").isEmpty()) {
            INSTALL_PATH = Paths.get(System.getProperty("user.dir"));
        } else {
            INSTALL_PATH = Paths.get(System.getProperty("magarena.dir"));
        }
    }

    // TODO: rename to "data".
    public static final String DATA_DIRECTORY_NAME = "Magarena";
    private static final Path DATA_PATH = INSTALL_PATH.resolve(DATA_DIRECTORY_NAME);

    public enum DataPath {

        DECKS("decks"),
        MODS("mods"),
        SCRIPTS("scripts"),
        SCRIPTS_MISSING("scripts_missing"),
        SOUNDS("sounds"),
        LOGS("logs"),
        DUELS("duels"),
        PLAYERS("players"),
        AVATARS("avatars"),
        FIREMIND("firemind");

        private final Path directoryPath;

        private DataPath(final String directoryName) {
            directoryPath = DATA_PATH.resolve(directoryName);
            MagicFileSystem.verifyDirectoryPath(directoryPath);
        }

        public Path getPath() {
            return directoryPath;
        }
        
    }
    
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

    private static String getImageFilename(final MagicCardDefinition card, final int index) {
        final int imageIndex = index % card.getImageCount();
        final String indexPostfix = imageIndex > 0 ? String.valueOf(imageIndex + 1) : "";
        return card.getImageName() + indexPostfix + CARD_IMAGE_EXT;
    }
    
    /**
     * Returns a File object representing the given card's image file.
     */
    public static File getCardImageFile(final MagicCardDefinition card, final int index) {
        final Path imageDirectory = card.isToken() ? 
                getImagesPath(ImagesPath.TOKENS) :
                getImagesPath(ImagesPath.CARDS);
        return new File(imageDirectory.toFile(), getImageFilename(card, index));
    }

    /**
     * Returns a File object representing the given card's image file.
     */
    public static File getCardImageFile(final MagicCardDefinition card) {
        return getCardImageFile(card, 0);
    }

    /**
     * Deletes all directory contents and then directory itself.
     */
    public static void deleteDirectory(final Path root) {
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

    public static void openMagicDirectory(final DataPath directory) throws IOException {
        openDirectory(getDataPath(directory).toString());
    }

    /**
     * Opens specified directory in OS file explorer.
     */
    public static void openDirectory(final String path) throws IOException {
        final File imagesPath = new File(path);
        if (MagicUtility.IS_WINDOWS_OS) {
            // Specific fix for Windows.
            // If running Windows and path is the default "Magarena" directory
            // then Desktop.getDesktop() will start a new instance of Magarena
            // instead of opening the directory! This is because the "Magarena"
            // directory and "Magarena.exe" are both at the same level and
            // Windows incorrectly assumes you mean "Magarena.exe".
            new ProcessBuilder("explorer.exe", imagesPath.getPath()).start();
        } else {
            Desktop.getDesktop().open(imagesPath);
        }
    }

    public static void openFileInDefaultOsEditor(final File file) {
        if (Desktop.isDesktopSupported()) {
            try {
                if (MagicUtility.IS_WINDOWS_OS) {
                    // There is an issue in Windows where the open() method of getDesktop()
                    // fails silently. The recommended solution is to use getRuntime().
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + file.toString());
                } else {
                    Desktop.getDesktop().open(file);
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(MagicMain.rootFrame, "Unable to open the following file using default application :\n" + file.getAbsolutePath());
            }
        } else {
            JOptionPane.showMessageDialog(MagicMain.rootFrame, "Sorry, opening this file with the default application is not supported on this OS.");
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
                throw new RuntimeException("!!! error creating " + path, ex);
            }
        }
    }

}
