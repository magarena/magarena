package magic.ui.screen.deck.tiled;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.swing.SwingUtilities;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.model.MagicType;
import magic.translate.UiString;
import magic.ui.MagicImages;
import magic.ui.screen.widget.MenuButton;
import magic.ui.screen.widget.SampleHandActionButton;
import magic.ui.screen.HeaderFooterScreen;

@SuppressWarnings("serial")
public class DeckTiledCardsScreen extends HeaderFooterScreen {

    // translatable strings
    private static final String _S1 = "Deck image view";

    private final MagicDeck deck;
    private ContentPanel content;
    private HeaderPanel headerPanel;

    public DeckTiledCardsScreen(final MagicDeck aDeck) {
        super(UiString.get(_S1));
        this.deck = aDeck;
        useLoadingScreen(this::initUI);
    }

    @Override
    protected boolean isCardBuilderRequired() {
        return MagicImages.hasProxyImage(deck);
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

    private List<MagicCard> getFilteredDeck(final MagicDeck deck, final CardTypeFilter filterType) {

        final List<MagicCard> cards = new ArrayList<>();

        for (MagicCardDefinition cardDef : deck) {

            final Set<MagicType> cardType = cardDef.getCardType();
            final MagicCard card = new MagicCard(cardDef, null, 0);

            if (filterType == CardTypeFilter.ALL
                    || cardType.contains(filterType.getMagicType())) {
                cards.add(card);
            }
        }

        Collections.sort(cards);
        return cards;
    }

    private void showCards(CardTypeFilter filter) {
        final List<MagicCard> cards = getFilteredDeck(deck, filter);
        content.refresh(cards);
        headerPanel.setContent(deck, filter, cards);
    }

    private void setFilterButtons() {

        final CardTypeFilter endOfGroup =
            CardTypeFilter.values()[CardTypeFilter.values().length-2];

        MenuButton.startGroup(); 

        for (CardTypeFilter f : CardTypeFilter.values()) {
            if (f == CardTypeFilter.ALL) {
                addToFooter(
                    MenuButton.build(() -> showCards(f), f.getTitle())
                );
            } else if (deck.contains(f.getMagicType())) {
                if (f == endOfGroup) {
                    MenuButton.endGroup();
                }
                addToFooter(
                    MenuButton.build(() -> showCards(f), f.getIcon(), f.getTitle())
                );
            }
        }
    }

    private void setFooterButtons() {
        setFilterButtons();
        addToFooter(SampleHandActionButton.createInstance(deck));
    }

}
