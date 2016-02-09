package magic.ui.explorer;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import magic.model.MagicCardDefinition;
import magic.model.MagicRandom;
import magic.ui.CardFilterPanel;
import magic.ui.ICardFilterPanelListener;
import magic.ui.ScreenController;
import magic.translate.UiString;
import magic.ui.cardtable.CardTable;
import magic.ui.cardtable.ICardSelectionListener;
import magic.utility.MagicSystem;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ExplorerPanel extends JPanel implements ICardSelectionListener, ICardFilterPanelListener {

    // translatable strings
    private static final String _S1 = "Cards:";
    private static final String _S2 = "Playable:";
    private static final String _S3 = "Unimplemented:";

    private static final int FILTERS_PANEL_HEIGHT = 88; // pixels

    private CardTable cardPoolTable;
    public CardFilterPanel filterPanel;
    private List<MagicCardDefinition> cardPoolDefs;
    private ExplorerSidebarPanel sideBarPanel;
    private final MigLayout migLayout = new MigLayout();

    public ExplorerPanel() {
        setupExplorerPanel();
    }

    private void setupExplorerPanel() {

        MagicSystem.waitForAllCards();

        setOpaque(false);

        // create ui components.
        sideBarPanel = new ExplorerSidebarPanel();
        filterPanel = new CardFilterPanel(this);
        final Container cardsPanel = getMainContentContainer();

        final JPanel rhs = new JPanel();
        rhs.setLayout(new MigLayout("flowy, insets 0, gapy 0"));
        rhs.add(filterPanel, "w 100%, h " + FILTERS_PANEL_HEIGHT + "!");
        rhs.add(cardsPanel, "w 100%, h 100%");
        rhs.setOpaque(false);
        rhs.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK));

        migLayout.setLayoutConstraints("insets 0, gap 0");
        setLayout(migLayout);
        add(sideBarPanel, "h 100%");
        add(rhs, "w 100%, h 100%");

        // set initial card image
        if (cardPoolDefs.isEmpty()) {
            sideBarPanel.setCard(MagicCardDefinition.UNKNOWN);
        } else {
            final int index = MagicRandom.nextRNGInt(cardPoolDefs.size());
            sideBarPanel.setCard(cardPoolDefs.get(index));
            //sideBarPanel.setCard(CardDefinitions.getCard("Damnation"));
        }

    }

    private Container getMainContentContainer() {

        cardPoolDefs = filterPanel.getFilteredCards();

        cardPoolTable = new CardTable(cardPoolDefs, generatePoolTitle(), false);
        cardPoolTable.addMouseListener(new CardPoolMouseListener());
        cardPoolTable.addCardSelectionListener(this);
        return cardPoolTable;

    }

    public String generatePoolTitle() {
        final int total = filterPanel.getTotalCardCount();
        return String.format("%s %s      %s %s      %s %s",
                UiString.get(_S1),
                NumberFormat.getInstance().format(total),
                UiString.get(_S2),
                getCountCaption(total, filterPanel.getPlayableCardCount()),
                UiString.get(_S3),
                getCountCaption(total, filterPanel.getMissingCardCount()));
    }

    private String getCountCaption(final int total, final int value) {
        final double percent = value / (double)total * 100;
        DecimalFormat df = new DecimalFormat("0.0");
        return NumberFormat.getInstance().format(value) + " (" + (!Double.isNaN(percent) ? df.format(percent) : "0.0") + "%)";
    }

    @Override
    public boolean isDeckEditor() {
        return false;
    }

    public void updateCardPool() {
        cardPoolDefs = filterPanel.getFilteredCards();
        cardPoolTable.setCards(cardPoolDefs);
        cardPoolTable.setTitle(generatePoolTitle());
    }

    protected void close() {
        filterPanel.closePopups();
    }

    @Override
    public void newCardSelected(final MagicCardDefinition card) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                sideBarPanel.setCard(card);
            }
        });
    }

    public MagicCardDefinition getSelectedCard() {
        if (cardPoolTable.getSelectedCards().size() == 1) {
            return cardPoolTable.getSelectedCards().get(0);
        } else {
            return null;
        }
    }

    @Override
    public void refreshTable() {
        updateCardPool();
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
            ScreenController.showCardScriptScreen(cardPoolTable.getSelectedCards().get(0));
            setCursor (Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

}
