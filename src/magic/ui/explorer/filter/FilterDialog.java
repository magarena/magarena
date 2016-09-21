package magic.ui.explorer.filter;

import java.util.List;
import javax.swing.JDialog;
import magic.model.MagicCardDefinition;
import magic.ui.ScreenController;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;


@SuppressWarnings("serial")
class FilterDialog extends JDialog {

    FilterDialog() {
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

    protected boolean isFiltering() {
        return false;
    }

    boolean filterMatches(MagicCardDefinition aCard) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    List<Integer> getSelectedItemIndexes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void reset() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    String getSearchOperandText() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    String getItemText(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }    
}
