package magic.ui.screen.interfaces;

/**
 * A (Mag)screen that implements this interface will display MagStatusBar.
 */
public interface IStatusBar {
    /**
     * Name of the screen. If null then MagStatusBar will be hidden.
     */
    String getScreenCaption();
}
