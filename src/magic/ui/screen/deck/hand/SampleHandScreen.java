package magic.ui.screen.deck.hand;

import java.util.Collections;
import java.util.List;
import magic.data.MagicIcon;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicCardList;
import magic.model.MagicDeck;
import magic.model.MagicRandom;
import magic.ui.MagicImages;
import magic.translate.MText;
import magic.ui.widget.cards.canvas.CardsCanvas.LayoutMode;
import magic.ui.widget.cards.canvas.CardsCanvas;
import magic.ui.screen.widget.MenuButton;
import magic.ui.widget.deck.DeckStatusPanel;
import magic.ui.screen.HeaderFooterScreen;

@SuppressWarnings("serial")
public class SampleHandScreen extends HeaderFooterScreen {

    // translatable strings
    private static final String _S1 = "Sample Hand";
    private static final String _S3 = "Refresh";
    private static final String _S4 = "Deal a new sample hand.";

    private CardsCanvas content;
    private final MagicDeck deck;
    private final DeckStatusPanel deckStatusPanel = new DeckStatusPanel();

    public SampleHandScreen(final MagicDeck aDeck) {
        super(MText.get(_S1));
        this.deck = aDeck;
        useLoadingScreen(this::initUI);
    }

    @Override
    protected boolean isCardBuilderRequired() {
        return MagicImages.hasProxyImage(deck);
    }

    private void initUI() {
        content = new CardsCanvas();
        content.setAnimationDelay(50, 20);
        content.setLayoutMode(LayoutMode.SCALE_TO_FIT);
        content.refresh(getRandomHand(deck));
        setMainContent(content);
        deckStatusPanel.setDeck(deck, false);
        setHeaderContent(deckStatusPanel);
        addToFooter(MenuButton.build(this::dealSampleHand, 
                MagicIcon.REFRESH, MText.get(_S3), MText.get(_S4))
        );
    }
    
    private void dealSampleHand() {
        if (!content.isBusy()) {
            content.refresh(getRandomHand(deck));
        }
    }

    private List<MagicCard> getRandomHand(final MagicDeck deck) {
        final MagicCardList library = new MagicCardList();
        for (MagicCardDefinition magicCardDef : deck) {
            library.add(new MagicCard(magicCardDef, null, 0));
        }
        library.shuffle(MagicRandom.nextRNGInt());
        if (library.size() >= 7) {
            final List<MagicCard> hand = library.subList(0, 7);
            Collections.sort(hand);
            return hand;
        } else {
            return null;
        }
    }

}
