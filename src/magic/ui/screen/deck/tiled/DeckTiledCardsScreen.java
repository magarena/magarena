package magic.ui.screen.deck.tiled;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.translate.MText;
import magic.ui.IDeckProvider;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.screen.widget.PlainMenuButton;
import magic.ui.screen.widget.SampleHandActionButton;

@SuppressWarnings("serial")
public class DeckTiledCardsScreen extends HeaderFooterScreen
    implements IDeckProvider {

    // translatable strings
    private static final String _S1 = "Deck image view";

    private final MagicDeck deck;
    private ContentPanel content;
    private HeaderPanel headerPanel;

    public DeckTiledCardsScreen(final MagicDeck aDeck) {
        super(MText.get(_S1));
        this.deck = aDeck;
        useCardsLoadingScreen(this::initUI);
    }

    private void initUI() {
        assert SwingUtilities.isEventDispatchThread();
        headerPanel = new HeaderPanel();
        setHeaderContent(headerPanel);
        content = new ContentPanel(deck);
        setMainContent(content);
        setFooterButtons();
        showCards(CardTypeFilter.ALL);
    }

    private List<MagicCardDefinition> getFilteredDeck(MagicDeck deck, CardTypeFilter filterType) {
        return deck.stream()
            .filter(c -> filterType == CardTypeFilter.ALL || c.getCardType().contains(filterType.getMagicType()))
            .sorted(MagicCardDefinition.SORT_BY_NAME)
            .collect(Collectors.toList());
    }

    private void showCards(CardTypeFilter filter) {
        final List<MagicCardDefinition> cards = getFilteredDeck(deck, filter);
        content.refresh(cards);
        headerPanel.setContent(deck, filter, cards);
    }

    private void setFilterButtons() {
        final List<PlainMenuButton> btns = new ArrayList<>();
        for (CardTypeFilter f : CardTypeFilter.values()) {
            if (f == CardTypeFilter.ALL) {
                btns.add(PlainMenuButton.build(() -> showCards(f), f.getTitle()));
            } else if (deck.contains(f.getMagicType())) {
                btns.add(PlainMenuButton.build(() -> showCards(f), f.getIcon(), f.getTitle()));
            }
        }
        addFooterGroup(btns.toArray(new PlainMenuButton[btns.size()]));
    }

    private void setFooterButtons() {
        setFilterButtons();
        addToFooter(SampleHandActionButton.createInstance(this));
    }

    @Override
    public MagicDeck getDeck() {
        return deck;
    }

}
