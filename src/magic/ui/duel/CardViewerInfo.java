package magic.ui.duel;

import java.awt.image.BufferedImage;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicObject;
import magic.model.MagicPermanent;
import magic.ui.CachedImagesProvider;
import magic.utility.MagicSystem;

public class CardViewerInfo {

    private final MagicCard card;
    private final MagicCardDefinition faceupCardDef;
    private final MagicObject magicObject;

    CardViewerInfo(final MagicCard aCard) {
        this.card = aCard;
        this.magicObject = aCard;
        this.faceupCardDef = card.getCardDefinition();
    }

    CardViewerInfo(MagicPermanent aPerm) {
        this.card = aPerm.getCard();
        this.magicObject = aPerm;
        this.faceupCardDef = aPerm.getController().isHuman() || MagicSystem.isAiVersusAi()
            ? aPerm.getRealCardDefinition()
            : aPerm.getCardDefinition();
    }    

    MagicCard getMagicCard() {
        return card;
    }

    public MagicObject getMagicObject() {
        return magicObject;
    }

    public BufferedImage getImage() {
        return CachedImagesProvider.getInstance().getImage(faceupCardDef, 0, true);
    }
}
