package magic;

import magic.data.CardDefinitions;
import magic.data.CubeDefinitions;
import magic.data.DeckGenerators;
import magic.data.DeckUtils;
import magic.data.History;
import magic.data.KeywordDefinitions;
import magic.model.MagicGameLog;
import magic.ui.MagicFrame;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MagicMain {

    public static final String VERSION = "1.46";
    public static final String SOFTWARE_TITLE = "Magarena " + VERSION;

    public static JFrame rootFrame;

    private static final String GAME_FOLDER  = "Magarena";
    private static final String MODS_PATH    = "mods";
    private static final String SCRIPTS_PATH = "scripts";
    private static final String LOGS_PATH = "logs";
    private static final String SAVED_DUELS_PATH = "duels";
    private static final String GAME_PATH    =
         (System.getProperty("magarena.dir") != null ?
          System.getProperty("magarena.dir") :
          System.getProperty("user.dir")) +
         File.separatorChar +
         GAME_FOLDER;

    public static void main(final String[] args) {
        // setup the handler for any uncaught exception
        Thread.setDefaultUncaughtExceptionHandler(new magic.model.MagicGameReport());

        // setup the game log
        MagicGameLog.initialize();

        // show the data folder being used
        System.err.println("Data folder : "+GAME_PATH);

        // try to set the look and feel
        try {
            for (final LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }

            // customize nimbus look
            UIManager.getLookAndFeelDefaults().put("Table.showGrid", true);
            // removes hardcoded border
            UIManager.getLookAndFeelDefaults().put("ScrollPane[Enabled].borderPainter", null);
        }
        catch (Exception e) {
            System.err.println("Unable to set look and feel. Probably missing the latest version of Java 6.");
            e.printStackTrace();
        }

        final long start_time = System.currentTimeMillis();
        initialize();
        final double duration = (double)(System.currentTimeMillis() - start_time) / 1000;
        System.err.println("Initalization of engine took " + duration + "s");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                rootFrame = new MagicFrame(SOFTWARE_TITLE);
            }
        });
    }

    public static String getGamePath() {
        return GAME_PATH;
    }

    public static String getGameFolder() {
        return GAME_FOLDER;
    }

    public static String getModsPath() {
        return getGamePath()+File.separatorChar+MODS_PATH;
    }

    public static String getScriptsPath() {
        return getGamePath()+File.separatorChar+SCRIPTS_PATH;
    }

    public static String getLogsPath() {
        return getDataPath(LOGS_PATH);
    }

    public static String getSavedDuelsPath() {
        return getDataPath(SAVED_DUELS_PATH);
    }

    /**
     * Gets path to a specified data directory. If the directory does not exist it
     * attempts to create it. If that fails then it uses the GAME_PATH directory instead.
     */
    private static String getDataPath(final String dataDirectory) {
        final Path path = Paths.get(getGamePath()).resolve(dataDirectory);
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                e.printStackTrace();
                return Paths.get(getGamePath()).toString();
            }
        }
        return path.toString();
    }

    static void initializeEngine() {
        CardDefinitions.loadCardDefinitions();
        if (Boolean.getBoolean("debug")) {
            CardDefinitions.loadCardAbilities();
        }
        CardDefinitions.loadCardTexts();
        CubeDefinitions.loadCubeDefinitions();
        KeywordDefinitions.getInstance().loadKeywordDefinitions();
        DeckGenerators.getInstance().loadDeckGenerators();
    }

    private static void initialize() {
        final File gamePathFile = new File(getGamePath());
        if (!gamePathFile.exists() && !gamePathFile.mkdir()) {
            System.err.println("Unable to create directory " + getGamePath());
        }

        final File modsPathFile = new File(getModsPath());
        if (!modsPathFile.exists() && !modsPathFile.mkdir()) {
            System.err.println("Unable to create directory " + getModsPath());
        }

        DeckUtils.createDeckFolder();
        History.createHistoryFolder();
        initializeEngine();
    }
}
