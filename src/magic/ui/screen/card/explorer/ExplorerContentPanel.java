package magic.ui.screen.card.explorer;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import magic.model.MagicCardDefinition;
import magic.model.MagicRandom;
import magic.ui.ICardFilterPanelListener;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.ui.widget.card.filter.CardFilterPanel;
import magic.ui.widget.cards.table.CardTablePanelB;
import magic.ui.widget.cards.table.ICardSelectionListener;
import magic.utility.MagicSystem;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ExplorerContentPanel extends JPanel
        implements ICardSelectionListener, ICardFilterPanelListener {

    private static final int FILTERS_PANEL_HEIGHT = 88; // pixels

    private CardTablePanelB cardPoolTable;
    public CardFilterPanel filterPanel;
    private List<MagicCardDefinition> cardPoolDefs;
    private ExplorerSideBar sideBarPanel;
    private final MigLayout migLayout = new MigLayout();
    private final JPanel rhs = new JPanel();
    private final ExplorerScreen screen;

    public ExplorerContentPanel(ExplorerScreen explorerScreen) {
        this.screen = explorerScreen;
        setupExplorerPanel();
    }

    private void setupExplorerPanel() {

        MagicSystem.waitForPlayableCards();

        setOpaque(false);

        // create ui components.
        sideBarPanel = new ExplorerSideBar();
        filterPanel = new CardFilterPanel(this);
        final Container cardsPanel = getMainContentContainer();

        rhs.setLayout(new MigLayout("flowy, insets 0, gapy 0"));
        rhs.add(filterPanel, "w 100%, h " + FILTERS_PANEL_HEIGHT + "!");
        rhs.add(cardsPanel, "w 100%, h 100%");
        rhs.setOpaque(false);
        rhs.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK));

        migLayout.setLayoutConstraints("insets 0, gap 0");
        setLayout(migLayout);
        refreshLayout();

        // set initial card image
        if (cardPoolDefs.isEmpty()) {
            sideBarPanel.setCard(MagicCardDefinition.UNKNOWN);
        } else {
            final int index = MagicRandom.nextRNGInt(cardPoolDefs.size());
            sideBarPanel.setCard(cardPoolDefs.get(index));
            //sideBarPanel.setCard(CardDefinitions.getCard("Damnation"));
        }

        refreshScreenTotals();
    }

    private Container getMainContentContainer() {
        cardPoolDefs = filterPanel.getFilteredCards();
        cardPoolTable = new CardTablePanelB(cardPoolDefs);
        cardPoolTable.addMouseListener(new CardPoolMouseListener());
        cardPoolTable.addCardSelectionListener(this);
        return cardPoolTable;

    }

    @Override
    public boolean isDeckEditor() {
        return false;
    }

    public void updateCardPool() {
        cardPoolDefs = filterPanel.getFilteredCards();
        cardPoolTable.setCards(cardPoolDefs);
    }

    @Override
    public void newCardSelected(final MagicCardDefinition card) {
        SwingUtilities.invokeLater(() -> sideBarPanel.setCard(card));
    }

    public MagicCardDefinition getSelectedCard() {
        if (cardPoolTable.getSelectedCards().size() == 1) {
            return cardPoolTable.getSelectedCards().get(0);
        } else {
            return null;
        }
    }

    private void refreshScreenTotals() {
        screen.refreshTotals(
                filterPanel.getTotalCardCount(),
                filterPanel.getPlayableCardCount(),
                filterPanel.getMissingCardCount()
        );
    }

    @Override
    public void refreshTable() {
        updateCardPool();
        refreshScreenTotals();
    }

    public void selectRandomCard() {
        cardPoolTable.selectRandomCard();
        cardPoolTable.requestFocus();
    }

    void setCardsTableStyle() {
        cardPoolTable.setStyle();
    }

    BufferedImage getCardImage(int index) {
        return MagicImages.getCardImage(cardPoolTable.getCard(index));
    }

    int getCardsCount() {
        return cardPoolTable.getRowCount();
    }

    void setCardAt(int index) {
        cardPoolTable.selectCardAt(index);
    }

    int getSelectedCardIndex() {
        return cardPoolTable.getSelectedCardIndex();
    }

    private class CardPoolMouseListener extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            // double-click actions.
            if (e.getClickCount() > 1 && SwingUtilities.isLeftMouseButton(e)) {
                showCardScriptScreen();
            }
        }
    }

    public void showCardScriptScreen() {
        if (cardPoolTable.getSelectedCards().size() == 1) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            ScreenController.showCardScreen(cardPoolTable.getSelectedCards().get(0));
            setCursor (Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void doDefaultLayout() {
        removeAll();
        add(sideBarPanel, "h 100%");
        add(rhs, "w 100%, h 100%");
        validate();
    }

    private void doNoSidebarLayout() {
        removeAll();
        add(rhs, "w 100%, h 100%");
        validate();
    }

    public void refreshLayout() {
        switch (ExplorerScreenLayout.getLayout()) {
            case DEFAULT:
                doDefaultLayout();
                break;
            case NO_SIDEBAR:
                doNoSidebarLayout();
                break;
            default:
                throw new IndexOutOfBoundsException();
        }
    }
}
