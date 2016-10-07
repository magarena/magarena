package magic.ui;

import java.awt.Component;
import java.awt.Cursor;

public final class MouseHelper {

    public static void showDefaultCursor(Component c) {
        c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public static void showBusyCursor(Component c) {
        c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }
    
    public static void showHandCursor(Component c) {
        c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private MouseHelper() {}
}
