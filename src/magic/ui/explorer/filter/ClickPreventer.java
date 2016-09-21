package magic.ui.explorer.filter;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Enables smooth toggling of the filter popup's visibility.
 */
class ClickPreventer {

    private boolean isRunning = false;

    void start() {
        isRunning = true;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isRunning = false;
            }
        }, 300);
    }

    boolean isNotRunning() {
        return !isRunning;
    }
}
