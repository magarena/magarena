package magic.ui.explorer.filter;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Enables smooth toggling of the filter popup's visibility.
 */
public class ClickPreventer {

    private boolean isRunning = false;

    public void start() {
        isRunning = true;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isRunning = false;
            }
        }, 300);
    }

    public boolean isNotRunning() {
        return !isRunning;
    }
}
