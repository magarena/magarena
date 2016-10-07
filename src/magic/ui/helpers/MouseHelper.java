package magic.ui.helpers;

import java.awt.Component;
import java.awt.Cursor;
import magic.ui.ScreenController;

public final class MouseHelper {

    public static void showDefaultCursor(Component c) {
        c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public static void showDefaultCursor() {
        showDefaultCursor(ScreenController.getMainFrame());
    }

    public static void showBusyCursor(Component c) {
        c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    public static void showBusyCursor() {
        showBusyCursor(ScreenController.getMainFrame());
    }
    
    public static void showHandCursor(Component c) {
        c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private MouseHelper() {}
}
