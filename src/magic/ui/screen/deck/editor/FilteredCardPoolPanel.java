package magic.ui.screen.deck.editor;

import java.text.NumberFormat;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTable;

import magic.model.MagicCardDefinition;
import magic.translate.MText;
import magic.translate.StringContext;
import magic.ui.ICardFilterPanelListener;
import magic.ui.widget.card.filter.CardFilterPanel;
import magic.ui.widget.cards.table.CardTablePanelA;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class FilteredCardPoolPanel extends JPanel implements ICardFilterPanelListener {

    // translatable strings
    @StringContext(eg="Cards: 290")
    private static final String _S1 = "Cards: %s";

    // fired when card selection changes
    public static final String CP_CARD_SELECTED = "87308824-9b54-42ba-ba52-6e9eee04bf92";
    public static final String CP_CARD_LCLICKED = "0d75bebc-f6ed-43af-81f1-370e4c779e76";
    public static final String CP_CARD_RCLICKED = "dfa8dd32-0f53-4001-ba0b-c23c76de9984";

    private static final int FILTERS_PANEL_HEIGHT = 88; // pixels

    private final CardFilterPanel filterPanel;
    private final CardTablePanelA cardPoolTable;
    private final MigLayout miglayout = new MigLayout();
    private List<MagicCardDefinition> cardPool;

    FilteredCardPoolPanel() {

        filterPanel = new CardFilterPanel(this);
        cardPool = filterPanel.getFilteredCards();
        cardPoolTable = new CardTablePanelA(cardPool, getCardPoolTitle());

        setLookAndFeel();
        refreshLayout();

        setPropertyChangeListeners();

    }

    private void setPropertyChangeListeners() {

        cardPoolTable.addPropertyChangeListener(CardTablePanelA.CP_CARD_SELECTED,
                evt -> firePropertyChange(CP_CARD_SELECTED, false, true));

        cardPoolTable.addPropertyChangeListener(CardTablePanelA.CP_CARD_LCLICKED,
                evt -> firePropertyChange(CP_CARD_LCLICKED, false, true));

        cardPoolTable.addPropertyChangeListener(CardTablePanelA.CP_CARD_RCLICKED,
                evt -> firePropertyChange(CP_CARD_RCLICKED, false, true));

    }

    private void setLookAndFeel() {
        setOpaque(false);
        setLayout(miglayout);
        miglayout.setLayoutConstraints("flowy, insets 0, gapy 0");
    }

    private void refreshLayout() {
        removeAll();
        add(filterPanel, "w 100%, h " + FILTERS_PANEL_HEIGHT + "!");
        add(cardPoolTable, "w 100%, h 100%");
        revalidate();
    }

    void refreshContent() {
        cardPool = filterPanel.getFilteredCards();
        cardPoolTable.setCards(cardPool);
        cardPoolTable.setTitle(getCardPoolTitle());
    }

    List<MagicCardDefinition> getCardPool() {
        return cardPool;
    }

    MagicCardDefinition getSelectedCard() {
        return cardPoolTable.getSelectedCards().size() > 0
                ? cardPoolTable.getSelectedCards().get(0)
                : null;
    }

    private String getCardPoolTitle() {
        return MText.get(_S1, NumberFormat.getInstance().format(cardPool.size()));
    }

    @Override
    public boolean isDeckEditor() {
        return true;
    }

    @Override
    public boolean requestFocusInWindow() {
        if (cardPoolTable.getSelectedCards().isEmpty()) {
            cardPoolTable.selectFirstRow();
        }
        return super.requestFocusInWindow();
    }

    @Override
    public void refreshTable() {
        refreshContent();
    }

    void clearSelection() {
        cardPoolTable.clearSelection();
    }

    void setSelectedCard(MagicCardDefinition aCard) {
        cardPoolTable.setSelectedCard(aCard);
    }

    JTable getTable() {
        return cardPoolTable.getTable();
    }

}
