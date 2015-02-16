package magic.exception.handler;

/**
 * Prints exception report to stderr.
 */
public class ConsoleExceptionHandler extends ExceptionHandler {

    @Override
    public void reportException(final ExceptionReport report) {
        System.err.println(report.toString());
    }

}
