package magic.ui.widget.cards.table;

import java.util.List;
import magic.model.MagicCardDefinition;
import magic.ui.widget.M.MScrollPane;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class CardsTablePanel extends TexturedPanel {

    protected final MScrollPane scrollpane = new MScrollPane();

    protected CardsJTable table;
    protected final MigLayout migLayout = new MigLayout();
    protected final CardTableModel tableModel;
    protected List<MagicCardDefinition> lastSelectedCards;

    public CardsTablePanel(List<MagicCardDefinition> defs) {
        tableModel = new CardTableModel(defs);
    }
}
