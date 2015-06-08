package magic.ai;

public abstract class Task implements Runnable {
    @Override
    public final void run() {
        try {
            execute();
        } catch (final Throwable ex) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), ex);
        }
    }
    
    public abstract void execute();
}
