package magic.ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import magic.utility.MagicFileSystem;

public class MagicLogFile {

    private boolean isError = false;
    private final File logFile;

    public MagicLogFile(String aLogName) {
        final Path logsPath = MagicFileSystem.getDataPath(MagicFileSystem.DataPath.LOGS);
        logFile = logsPath.resolve(aLogName + ".log").toFile();
    }

    public void deleteLogFileIfExists() throws IOException {
        if (!isError) {
            Files.deleteIfExists(logFile.toPath());
        }
    }

    public void log(String msg) throws IOException {
        if (!isError) {
            try (
                FileWriter fw = new FileWriter(logFile, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
                out.println(msg);
            } catch (IOException ex) {
                isError = true;
                throw new IOException("Logging switched off (restart Magarena to enable) : " + ex.getMessage(), ex);
            }
        }
    }

    public void log(String msg, CardTextLanguage lang, URL cardUrl) throws IOException {
        log(String.format("%s  %s  %s",
            lang != CardTextLanguage.ENGLISH ? lang.getMagicCardsCode() : "  ",
            msg,
            cardUrl != null ? "(" + cardUrl.toExternalForm() + ")" : ""));
    }
}
