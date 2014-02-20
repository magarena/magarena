package magic;

import java.awt.Cursor;

final public class MagicUtility {
    private MagicUtility() {}

    public static final boolean IS_WINDOWS_OS = System.getProperty("os.name").toLowerCase().startsWith("windows");

    public static void setBusyMouseCursor(final boolean b) {
        MagicMain.rootFrame.setCursor(
                b ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) :
                    Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

}
