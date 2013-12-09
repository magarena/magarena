package magic.ui.interfaces;

/**
 * A (Mag)screen that implements this interface will display MagStatusBar.
 */
public interface IMagStatusBar {
    /**
     * Name of the screen. If null then MagStatusBar will be hidden.
     */
    String getScreenCaption();
}
