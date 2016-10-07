package magic.ui;

import javax.swing.JButton;

public final class WidgetHelper {

    public static void setTransparent(JButton btn) {
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setBorder(null);
    }

    private WidgetHelper() {}
}
