package magic.ui.screen.interfaces;

import javax.swing.JPanel;

/**
 * A screen that implements this interface will display MagStatusBar.
 */
public interface IStatusBar {
    /**
     * Name of the screen. If null then MagStatusBar will be hidden.
     */
    String getScreenCaption();
    /**
     * Can use to display a panel of info in central section of status bar.
     */
    JPanel getStatusPanel();
}
