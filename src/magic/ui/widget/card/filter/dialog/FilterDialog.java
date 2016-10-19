package magic.ui.widget.card.filter.dialog;

import java.awt.LayoutManager2;
import javax.swing.JDialog;
import magic.ui.ScreenController;
import magic.ui.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;


@SuppressWarnings("serial")
public abstract class FilterDialog extends JDialog {

    public abstract void reset();
    public abstract boolean isFiltering();

    FilterDialog() {
        super(ScreenController.getFrame());
        setUndecorated(true);
        setSize(260, 300);
        setContentPane();
    }

    /**
     * Default layout. Variable sized main panel containing filter
     * values above fixed sized action bar.
     */
    protected LayoutManager2 getLayoutManager() {
       return new MigLayout("flowy, gap 0, insets 0",
                "[fill, grow]",         // column layout
                "[fill, grow]3[fill]"   // row layout
        );
    }

    private void setContentPane() {
        TexturedPanel p = new TexturedPanel();
        p.setBorder(FontsAndBorders.UP_BORDER);
        p.setLayout(getLayoutManager());
        setContentPane(p);
    }
}
