package magic.ui.cardpool;

import java.awt.event.MouseAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.JPanel;
import magic.model.MagicCardDefinition;
import magic.ui.CardFilterPanel;
import magic.ui.ICardFilterPanelListener;
import magic.ui.cardtable.CardTablePanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public abstract class CardPoolPanel extends JPanel implements ICardFilterPanelListener {

    // fired when contents of cardPoolTable are updated.
    public static final String CP_CARDPOOL = "cardPoolContent";
    // fired when card selection changes
    public static final String CP_CARD_SELECTED = CardTablePanel.CP_CARD_SELECTED;
    //
    public static final String CP_CARD_LCLICKED = CardTablePanel.CP_CARD_LCLICKED;

    protected static final int FILTERS_PANEL_HEIGHT = 88; // pixels

    // ui components.
    protected final CardFilterPanel filterPanel;
    protected final CardTablePanel cardPoolTable;
    // fields
    private final MigLayout miglayout = new MigLayout();
    protected List<MagicCardDefinition> cardPool;
    private boolean isFilterVisible = true;
    // abstract methods
    protected abstract String getCardPoolTitle();
    protected abstract MouseAdapter getMouseAdapter();

    public CardPoolPanel() {
        filterPanel = new CardFilterPanel(this);
        cardPool = filterPanel.getCardDefinitions(isDeckEditor());
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


}
