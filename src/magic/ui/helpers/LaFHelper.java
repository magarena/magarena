package magic.ui.helpers;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import magic.data.GeneralConfig;
import magic.data.settings.BooleanSetting;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;

public final class LaFHelper {

    public static void setDefaultLookAndFeel() {

        if (trySetNimbusLookAndFeel()) {

            final UIDefaults defaults = UIManager.getLookAndFeelDefaults();

            //
            // ** JTable
            //
            defaults.put("Table.showGrid", true);
            // theme-based selection background color.
            final Color oldColor = MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND);
            final Color newColor = ColorHelper.bleach1(oldColor, 0.2f);
            defaults.put("Table[Enabled+Selected].textBackground", newColor);

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
            if (GeneralConfig.get(BooleanSetting.CUSTOM_SCROLLBAR)) {
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
