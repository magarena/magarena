package magic.ui.screen.decks;

import java.awt.Dimension;
import java.nio.file.Path;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import magic.data.DeckType;
import magic.model.MagicDeck;
import magic.translate.MText;
import magic.ui.widget.cards.table.CardTable;
import magic.ui.widget.deck.DeckStatusPanel;
import magic.ui.widget.duel.viewer.CardViewer;
import magic.ui.screen.interfaces.IDeckConsumer;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ScreenPanel extends JPanel implements IDeckConsumer {

    // translatable strings
    private static final String _S14 = "%s (%d cards)";
    private static final String _S15 = "NO DECK";

    private MagicDeck selectedDeck = null;
    private Path deckFilePath = null;
    private final CardTable deckTable;
    private final CardViewer cardViewer = new CardViewer();
    private final JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    private final DeckStatusPanel deckStatusPanel;
    private final SidebarPanel sidebar;

    ScreenPanel(DeckStatusPanel deckStatusPanel) {

        this.deckStatusPanel = deckStatusPanel;

        setOpaque(false);

        selectedDeck = new MagicDeck();
        deckTable = new CardTable(selectedDeck, "{deckName}", true);
        deckTable.addCardSelectionListener(cardViewer);
        deckTable.setHeaderVisible(false);
        deckTable.showCardCount(true);

        sidebar = new SidebarPanel(this);

        setLayout(new MigLayout("insets 0, gap 0"));
        add(sidebar, "h 100%");
        add(getDeckDetailsPane(), "w 100%, h 100%");

    }

    MagicDeck getDeck() {
        return selectedDeck;
    }

    Path getDeckPath() {
        return deckFilePath;
    }

    private JSplitPane getDeckDetailsPane() {
        splitter.setOneTouchExpandable(false);
        splitter.setLeftComponent(deckTable);
        splitter.setRightComponent(getCardDetailsPanel());
        splitter.setDividerSize(14);
        splitter.setBorder(null);
        splitter.setOpaque(false);
        splitter.getRightComponent().setMinimumSize(new Dimension());
        splitter.setResizeWeight(1.0);
        return splitter;
    }

    private JPanel getCardDetailsPanel() {
        final JPanel panel = new JPanel();
        panel.setMinimumSize(new Dimension());
        panel.setOpaque(false);
        panel.setLayout(new MigLayout("insets 0"));
        panel.add(cardViewer, "w 100%, h 0:100%");
        return panel;
    }

    @Override
    public void setDeck(String deckName, DeckType deckType) {
        System.out.println(deckName + ", " + deckType);
    }

    @Override
    public void setDeck(MagicDeck deck, Path deckPath) {
        if (deckPath != null) {
            selectedDeck = deck;
            deckFilePath = deckPath;
            sidebar.setDeck(deck);
            deckTable.setCards(deck);
            deckTable.setTitle(MText.get(_S14, deck.getName(), deck.size()));
            deckStatusPanel.setDeck(deck, deck.isValid() || deck.size() > 0);
            splitter.setVisible(deck.isValid() || deck.size() > 0);
        } else {
            selectedDeck = null;
            deckFilePath = null;
            sidebar.setDeck(selectedDeck);
            deckTable.setCards(deck);
            deckTable.setTitle(MText.get(_S15));
            deckStatusPanel.setDeck(null, false);
            splitter.setVisible(false);
        }
    }

}
