package magic.ui.deck.selector;

import java.awt.Color;
import java.awt.Dimension;
import java.nio.file.Path;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import magic.data.DeckType;
import magic.model.MagicDeck;
import magic.translate.UiString;
import magic.ui.cardtable.CardTable;
import magic.ui.deck.widget.DeckDescriptionViewer;
import magic.ui.deck.widget.DeckPicker;
import magic.ui.deck.widget.DeckStatusPanel;
import magic.ui.duel.viewer.CardViewer;
import magic.ui.screen.interfaces.IDeckConsumer;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ScreenPanel extends JPanel implements IDeckConsumer {

    // translatable strings
    private static final String _S14 = "%s (%d cards)";
    private static final String _S15 = "NO DECK";

    private MagicDeck selectedDeck = null;
    private Path deckFilePath = null;
    private final DeckDescriptionViewer descViewer = new DeckDescriptionViewer();
    private final CardTable deckTable;
    private final CardViewer cardViewer = new CardViewer();
    private DeckPicker deckPicker;
    private final JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    private final DeckStatusPanel deckStatusPanel;

    ScreenPanel(DeckStatusPanel deckStatusPanel) {

        this.deckStatusPanel = deckStatusPanel;

        descViewer.setDeckChooserLayout();
        setOpaque(false);

        selectedDeck = new MagicDeck();
        deckTable = new CardTable(selectedDeck, "{deckName}", true);
        deckTable.addCardSelectionListener(cardViewer);
        deckTable.setHeaderVisible(false);
        deckTable.showCardCount(true);

        setLayout(new MigLayout("insets 0, gap 0"));
        add(getDeckNamesPanel(), "w 300!, h 100%");
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

    private JPanel getDeckNamesPanel() {

        deckPicker = new DeckPicker();
        deckPicker.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
        deckPicker.addListener(this);

        descViewer.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));

        // main container panel
        final JPanel container = new TexturedPanel();
        container.setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);
        container.setLayout(new MigLayout("insets 0, gap 0, flowy"));
        container.add(deckPicker, "w 100%, h 100%");
        container.add(descViewer, "w 100%, h 28%:28%:200px");
        return container;
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
            descViewer.setDeckDescription(deck);
            deckTable.setCards(deck);
            deckTable.setTitle(UiString.get(_S14, deck.getName(), deck.size()));
            deckStatusPanel.setDeck(deck, deck.isValid() || deck.size() > 0);
            splitter.setVisible(deck.isValid() || deck.size() > 0);
        } else {
            selectedDeck = null;
            deckFilePath = null;
            descViewer.setDeckDescription(selectedDeck);
            deckTable.setCards(deck);
            deckTable.setTitle(UiString.get(_S15));
            deckStatusPanel.setDeck(null, false);
            splitter.setVisible(false);
        }
    }

}
