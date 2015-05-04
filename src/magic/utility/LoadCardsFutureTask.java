package magic.utility;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class LoadCardsFutureTask extends FutureTask<Void> {

    public LoadCardsFutureTask(Runnable r) {
        super(r, null);
    }

    @Override
    protected void done() {
        try {
            if (!isCancelled()) {
                get();
            }
       
        } catch (ExecutionException ex) {
            throw new RuntimeException(ex.getCause());

        } catch (InterruptedException ex) {
            // Shouldn't happen, we're invoked when computation is finished
            throw new AssertionError(ex);
        }
    }

}
