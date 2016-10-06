package magic.ui;

import java.awt.Dimension;
import javax.swing.UIManager;
import magic.data.GeneralConfig;

public final class UIManagerHelper {

    private UIManagerHelper() {
    }

    public static void setLookAndFeel(final String laf) {
        try {

            for (final UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (laf.equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }

            // customize nimbus look
            UIManager.getLookAndFeelDefaults().put("Table.showGrid", true);
            // removes hardcoded border
            UIManager.getLookAndFeelDefaults().put("ScrollPane[Enabled].borderPainter", null);

            if (GeneralConfig.getInstance().isCustomScrollBar()) {
                // custom scrollbar
                UIManager.put("ScrollBarUI", "magic.ui.widget.scrollbar.MScrollBarUI");
                final Dimension d = (Dimension) UIManager.get("ScrollBar.minimumThumbSize");
                UIManager.put("ScrollBar.minimumThumbSize", new Dimension(
                        d.width < 50 ? 50 : d.width,
                        d.height < 50 ? 50 : d.height)
                );
            }

        } catch (Exception e) {
            System.err.println("Unable to set look and feel. Probably missing the latest version of Java 6.");
            e.printStackTrace();
        }
    }

}
