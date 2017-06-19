package magic.model;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import static java.nio.charset.StandardCharsets.UTF_8;

public class MagicGameLog {
    private MagicGameLog() {}

    public static final String LOG_FILE = "game.log";

    private static final String gameLog = (System.getProperty("game.log") != null) ?
        System.getProperty("game.log") :
        MagicFileSystem.getDataPath(DataPath.LOGS).resolve(LOG_FILE).toString();

    private static PrintWriter writer;

    public static String getLogFileName(){
        return gameLog;
    }

    public static void initialize() {
        try {
            writer = new PrintWriter(gameLog, UTF_8.name());
            final StringBuilder sb = new StringBuilder();
            MagicLogger.setLogHeader(sb, "MAGARENA GAME LOG");
            sb.append("\n\n");
            log(sb.toString());
        } catch (FileNotFoundException|UnsupportedEncodingException e) {
            System.err.println("Unable to create game log");
        }
    }

    public static void log(final String message) {
        if (writer != null) {
            writer.println(message);
            writer.flush();
        }
    }

    public static void close() {
        if (writer != null) {
            writer.close();
        }
    }
}
