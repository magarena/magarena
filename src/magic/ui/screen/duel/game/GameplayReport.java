package magic.ui.screen.duel.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import magic.game.state.GameState;
import magic.game.state.GameStateFileWriter;
import magic.game.state.GameStateSnapshot;
import magic.model.MagicGame;
import magic.model.MagicGameLog;
import magic.ui.ScreenController;
import magic.ui.helpers.DesktopHelper;
import magic.ui.utility.GraphicsUtils;
import magic.utility.MagicFileSystem;
import static magic.utility.MagicFileSystem.getDataPath;
import org.apache.commons.io.FileUtils;
import static magic.utility.MagicFileSystem.getDataPath;

final class GameplayReport {
    private GameplayReport() {}

    // jpg : small size, lower quality than png.
    // png : much bigger size, slightly better quality than jpg.
    private static final String IMAGE_TYPE = "jpg";

    private static final String SCREEN_FILE = "screenshot." + IMAGE_TYPE;
    private static final String GAME_FILE = "snapshot.game";
    private static final String LOG_FILE = MagicGameLog.LOG_FILE;
    private static final String ZIP_FILE = "gameplay-report.zip";

    static void createNewReport(final MagicGame aGame) throws IOException {
        MagicFileSystem.clearGameplayReportDirectory();
        saveScreenshot();
        saveGameState(aGame);
        saveGameLog();
        createZipFile();
    }

    private static void saveScreenshot() throws IOException {
        final Path filePath = MagicFileSystem.getGameplayReportDirectory().resolve(SCREEN_FILE);
        GraphicsUtils.doScreenshotToFile(ScreenController.getMainFrame().getContentPane(), filePath, IMAGE_TYPE);
    }

    private static void saveGameState(MagicGame aGame) {
        final File file = MagicFileSystem.getGameplayReportDirectory().resolve(GAME_FILE).toFile();
        final GameState gameState = GameStateSnapshot.getGameState(aGame);
        GameStateFileWriter.createSaveGameFile(gameState, file);
    }

    private static void saveGameLog() throws IOException {
        final File logFile = MagicFileSystem.getDataPath(MagicFileSystem.DataPath.LOGS).resolve(LOG_FILE).toFile();
        FileUtils.copyFileToDirectory(logFile, MagicFileSystem.getGameplayReportDirectory().toFile());
    }

    static void openReportDirectory() throws IOException {
        DesktopHelper.openDirectory(MagicFileSystem.getDataPath(MagicFileSystem.DataPath.REPORTS).toString());
    }

    private static void createZipFile() throws IOException {

        final byte[] buf = new byte[1024];
        final String[] filenames = new String[]{SCREEN_FILE, GAME_FILE, LOG_FILE};

        final File zipFile = getDataPath(MagicFileSystem.DataPath.REPORTS).resolve(ZIP_FILE).toFile();
        try (final ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile))) {

            for (String filename : filenames) {

                final File f = MagicFileSystem.getGameplayReportDirectory().resolve(filename).toFile();
                try (final FileInputStream in = new FileInputStream(f)) {
                    out.putNextEntry(new ZipEntry(filename));
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.closeEntry();
                }
            }
        }
    }

}
