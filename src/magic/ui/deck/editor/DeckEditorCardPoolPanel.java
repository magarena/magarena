package magic.ui.deck.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.List;
import javax.swing.JPanel;
import magic.model.MagicCardDefinition;
import magic.ui.CardFilterPanel;
import magic.ui.ICardFilterPanelListener;
import magic.ui.cardtable.CardTablePanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckEditorCardPoolPanel extends JPanel implements ICardFilterPanelListener {

    public static final String CP_CARD_RCLICKED = CardTablePanel.CP_CARD_RCLICKED;

    // fired when contents of cardPoolTable are updated.
    public static final String CP_CARDPOOL = "cardPoolContent";
    // fired when card selection changes
    public static final String CP_CARD_SELECTED = CardTablePanel.CP_CARD_SELECTED;
    //
    public static final String CP_CARD_LCLICKED = CardTablePanel.CP_CARD_LCLICKED;

    private static final int FILTERS_PANEL_HEIGHT = 88; // pixels

    // ui components.
    private final CardFilterPanel filterPanel;
    private final CardTablePanel cardPoolTable;
    // fields
    private final MigLayout miglayout = new MigLayout();
    private List<MagicCardDefinition> cardPool;
    private boolean isFilterVisible = true;

    public DeckEditorCardPoolPanel() {

        filterPanel = new CardFilterPanel(this);
        cardPool = filterPanel.getCardDefinitions(true);
        cardPoolTable = new CardTablePanel(cardPool, getCardPoolTitle());

        cardPoolTable.addPropertyChangeListener(CardTablePanel.CP_CARD_SELECTED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        firePropertyChange(CP_CARD_SELECTED, false, true);
                    }
                });
        cardPoolTable.addPropertyChangeListener(
                CardTablePanel.CP_CARD_LCLICKED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        firePropertyChange(CP_CARD_LCLICKED, false, true);
                    }
                });

        //
        setLookAndFeel();
        refreshLayout();
        cardPoolTable.addPropertyChangeListener(
                CardTablePanel.CP_CARD_RCLICKED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        firePropertyChange(CP_CARD_RCLICKED, false, true);
                    }
                });

    }

    private void setLookAndFeel() {
        setOpaque(false);
        setLayout(miglayout);
        miglayout.setLayoutConstraints("flowy, insets 0, gapy 0");
    }

    private void refreshLayout() {
        removeAll();
        add(filterPanel, "w 100%, h " + (isFilterVisible ? FILTERS_PANEL_HEIGHT : 0) + "!");
        add(cardPoolTable.getTitleBar(), "w 100%, h " + (isDeckEditor() ? 0 : 26) + "!");
        add(cardPoolTable, "w 100%, h 100%");
        revalidate();
    }

   public void refreshContent() {
        cardPool = filterPanel.getCardDefinitions(isDeckEditor());
        cardPoolTable.setCards(cardPool);
        cardPoolTable.setTitle(getCardPoolTitle());
        firePropertyChange(CP_CARDPOOL, false, true);
    }

    public List<MagicCardDefinition> getCardPool() {
        return cardPool;
    }

    public void setFilterVisible(final boolean b) {
        isFilterVisible = b;
        refreshLayout();
    }

    public MagicCardDefinition getSelectedCard() {
        if (cardPoolTable.getSelectedCards().size() > 0) {
            return cardPoolTable.getSelectedCards().get(0);
        } else {
            return MagicCardDefinition.UNKNOWN;
        }
    }

    private String getCardPoolTitle() {
        final StringBuffer sb = new StringBuffer();
        final int total = cardPool.size();
        sb.append("Cards: ").append(NumberFormat.getInstance().format(total));
        return sb.toString();
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

}
