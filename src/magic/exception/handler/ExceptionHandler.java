package magic.exception.handler;

public abstract class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    // Safeguard that ensures that if for some reason uncaughtException()
    // is called multiple times (eg. from multiple threads) then only the
    // first case is actually handled. This is mainly to prevent multiple
    // error notification dialogs being created in UiExceptionReporter.
    private volatile boolean isRunning = false;

    @Override
    public void uncaughtException(final Thread th, final Throwable ex) {
        if (!isRunning) {
            isRunning = true;
            reportException(new ExceptionReport(th, ex));
            System.exit(1);
        }
    }

    public abstract void reportException(final ExceptionReport report);

}
