package magic.ai;

public abstract class Task implements Runnable {
    @Override
    public final void run() {
        try {
            execute();
        } catch (final Throwable ex) {
            final Thread t = Thread.currentThread();
            t.getUncaughtExceptionHandler().uncaughtException(t, ex);
        }
    }
    
    public abstract void execute();
}
