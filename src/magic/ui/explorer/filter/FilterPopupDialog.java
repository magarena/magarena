package magic.ui.explorer.filter;

import javax.swing.JDialog;
import magic.ui.ScreenController;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;


@SuppressWarnings("serial")
class FilterPopupDialog extends JDialog {

    FilterPopupDialog() {
        super(ScreenController.getMainFrame());
        setUndecorated(true);
        setSize(260, 300);
        setContentPane();
    }

    /**
     * Default layout and style. Subclasses of FilterButtonPanel can override if required.
     */
    private void setContentPane() {
        TexturedPanel p = new TexturedPanel();
        p.setBorder(FontsAndBorders.UP_BORDER);
        p.setLayout(new MigLayout("flowy, gap 0, insets 0", "[fill, grow]", "[fill, grow]"));
        setContentPane(p);
    }
}
