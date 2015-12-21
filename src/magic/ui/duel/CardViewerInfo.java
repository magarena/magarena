package magic.ui.duel;

import magic.model.MagicCard;

public class CardViewerInfo {

    private final MagicCard card;

    CardViewerInfo(final MagicCard aCard) {
        this.card = aCard;
    }

    MagicCard getCard() {
        return card;
    }

}
