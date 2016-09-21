package magic.ui;

import java.awt.Component;
import java.awt.Cursor;

public final class MagicUI {

    public static void showBusyCursorFor(Component c) {
        c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    public static void showDefaultCursorFor(Component c) {
        c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private MagicUI() {
    }
}
