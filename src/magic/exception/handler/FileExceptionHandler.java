package magic.exception.handler;

import java.io.IOException;
import java.nio.file.Path;
import magic.utility.FileIO;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;

/**
 * Saves exception report to "crash.log" in logs folder and to stderr.
 */
public class FileExceptionHandler extends ConsoleExceptionHandler {

    @Override
    public void reportException(final ExceptionReport report) {
        super.reportException(report);
        saveReport(report);
    }

    private static void saveReport(final ExceptionReport report) {
        final Path clog = MagicFileSystem.getDataPath(DataPath.LOGS).resolve("crash.log");
        try {
            FileIO.toFile(clog.toFile(), report.toString(), true);
        } catch (final IOException ex) {
            System.err.println("Unable to save crash log : " + ex.getMessage());
        }
    }

}
