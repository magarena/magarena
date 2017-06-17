package magic.model;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import magic.utility.MagicSystem;

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
            sb.append("MAGARENA GAME LOG");
            sb.append('\n');
            sb.append("CREATED ON ").append(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
            sb.append('\n');
            sb.append("MAGARENA VERSION ").append(MagicSystem.VERSION);
            sb.append(", JRE ").append(System.getProperty("java.version"));
            sb.append(", OS ").append(System.getProperty("os.name"));
            sb.append("_").append(System.getProperty("os.version"));
            sb.append(" ").append(System.getProperty("os.arch"));
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
