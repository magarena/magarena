package magic.ui.screen.decks;

import java.awt.Dimension;
import java.nio.file.Path;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import magic.data.DeckType;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.ui.screen.interfaces.IDeckConsumer;
import magic.ui.widget.cards.canvas.CardImageOverlay;
import magic.ui.widget.deck.DeckStatusPanel;
import magic.ui.widget.deck.stats.PwlWorker;
import magic.ui.widget.duel.viewer.CardViewer;
import magic.utility.DeckUtils;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class DecksScreenPanel extends JPanel
    implements IDeckConsumer, ICardsTableListener {

    private MagicDeck selectedDeck = null;
    private Path deckFilePath = null;
    private final CardViewer cardViewer = new CardViewer();
    private final JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    private final DeckStatusPanel deckStatusPanel;
    private final SidebarPanel sidebar;
    private final DeckViewsPanel viewsPanel;
    private PwlWorker pwlWorker;

    DecksScreenPanel(DeckStatusPanel deckStatusPanel) {

        this.deckStatusPanel = deckStatusPanel;

        setOpaque(false);

        selectedDeck = new MagicDeck();

        sidebar = new SidebarPanel(this);
        viewsPanel = new DeckViewsPanel();
        viewsPanel.setCardsTableListeners(this);

        setLayout(new MigLayout("insets 0", "[]0[fill, grow]", "fill, grow"));
        add(sidebar);
        add(getDeckDetailsPane());
    }

    MagicDeck getDeck() {
        return selectedDeck;
    }

    Path getDeckPath() {
        return deckFilePath;
    }

    private JSplitPane getDeckDetailsPane() {
        splitter.setOneTouchExpandable(false);
        splitter.setLeftComponent(viewsPanel);
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
        throw new UnsupportedOperationException("setDeck(" + deckName + ", " + deckType + ")");
    }

    @Override
    public boolean setDeck(MagicDeck deck, Path deckPath) {
        if (deckPath != null) {
            selectedDeck = deck;
            deckFilePath = deckPath;
            sidebar.setDeck(deck);
            deckStatusPanel.setDeck(deck, deck.isValid() || deck.size() > 0);
            splitter.setVisible(deck.isValid() || deck.size() > 0);
        } else {
            selectedDeck = null;
            deckFilePath = null;
            sidebar.setDeck(selectedDeck);
            deckStatusPanel.setDeck(null, false);
            splitter.setVisible(false);
        }
        doPWLStatsQuery(deck);
        viewsPanel.setDeck(deck);
        return true;
    }

    @Override
    public void setDeck(MagicDeck deck) {
        setDeck(deck, DeckUtils.getDeckPath(deck));
    }

    private void doPWLStatsQuery(MagicDeck deck) {
        pwlWorker = new PwlWorker(deck);
        pwlWorker.setListeners(sidebar, viewsPanel);
        pwlWorker.execute();
    }

    @Override
    public void onCardSelected(MagicCardDefinition card) {
        cardViewer.setCard(card);
    }

    @Override
    public void onLeftClick(MagicCardDefinition card) {
        // not applicable.
    }

    @Override
    public void onRightClick(MagicCardDefinition card) {
        new CardImageOverlay(card);
    }
}
