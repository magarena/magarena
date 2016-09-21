package magic.ui.explorer.filter.dialogs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager2;
import javax.swing.JDialog;
import magic.ui.ScreenController;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;


@SuppressWarnings("serial")
public abstract class FilterDialog extends JDialog {

    private static final Color OPACITY_COLOR = new Color(255, 255, 255, 160);

    public abstract void reset();
    public abstract boolean isFiltering();

    FilterDialog() {
        super(ScreenController.getMainFrame());
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
                "[fill, grow]",        // column layout
                "[fill, grow][fill]"   // row layout
        );
    }

    private void setContentPane() {
        TexturedPanel p = new TexturedPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(OPACITY_COLOR);
                g.fillRect(0, 0, getWidth(), getHeight());
            }            
        };
        p.setBorder(FontsAndBorders.UP_BORDER);
        p.setLayout(getLayoutManager());
        setContentPane(p);
    }
}
