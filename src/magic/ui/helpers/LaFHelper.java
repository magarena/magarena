package magic.ui.helpers;

import java.awt.Dimension;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import magic.data.GeneralConfig;

public final class LaFHelper {

    public static void setDefaultLookAndFeel() {

        if (trySetNimbusLookAndFeel()) {
            
            final UIDefaults defaults = UIManager.getLookAndFeelDefaults();

            //
            // ** JTable
            //
            defaults.put("Table.showGrid", true);


            //
            // ** JList
            //
            // set JList selection colors the same as JTable.
            defaults.put("List[Selected].textBackground",
                    defaults.getColor("Table[Enabled+Selected].textBackground"));
            defaults.put("List[Selected].textForeground",
                    defaults.getColor("Table[Enabled+Selected].textForeground"));


            //
            // ** JScrollPane
            //
            // removes hardcoded border
            defaults.put("ScrollPane[Enabled].borderPainter", null);


            //
            // ** JScrollBar
            //
            // custom scrollbar
            if (GeneralConfig.getInstance().isCustomScrollBar()) {
                defaults.put("ScrollBarUI", "magic.ui.widget.scrollbar.MScrollBarUI");
                final Dimension d = (Dimension) UIManager.get("ScrollBar.minimumThumbSize");
                defaults.put("ScrollBar.minimumThumbSize", new Dimension(
                        d.width < 50 ? 50 : d.width,
                        d.height < 50 ? 50 : d.height)
                );
            }
            
        }
    }

    private static boolean trySetNimbusLookAndFeel() {
        try {
            for (final UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            return true;
        } catch (Exception ex) {
            System.err.println(ex);
        }
        return false;
    }

    private LaFHelper() { }
}
