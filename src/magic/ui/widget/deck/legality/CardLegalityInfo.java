package magic.ui.widget.deck.legality;

import magic.data.CardLegality;
import magic.data.MagicFormat;
import magic.model.MagicCardDefinition;

public class CardLegalityInfo {

    private final MagicCardDefinition card;
    private final CardLegality legality;
    private final MagicFormat magicFormat;

    public CardLegalityInfo(MagicCardDefinition aCard, final CardLegality legality, final MagicFormat aFormat) {
        this.card = aCard;
        this.legality = legality;
        this.magicFormat = aFormat;
    }

    public boolean isCardLegal() {
        return legality == CardLegality.Legal;
    }

    public String getCardName() {
        return card.getName();
    }

    public MagicCardDefinition getCard() {
        return card;
    }

    public CardLegality getLegality() {
        return legality;
    }

    public MagicFormat getFormat() {
        return magicFormat;
    }

}
