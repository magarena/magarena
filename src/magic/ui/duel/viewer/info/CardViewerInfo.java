package magic.ui.duel.viewer.info;

import java.awt.image.BufferedImage;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicObject;
import magic.model.MagicPermanent;
import magic.model.stack.MagicCardOnStack;
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
        this.faceupCardDef = getFaceupCardDef(aPerm);
    }

    CardViewerInfo(MagicCardOnStack aCard) {
        this.card = aCard.getCard();
        this.faceupCardDef = getFaceupCardDef(aCard);
        this.magicObject = aCard;
    }

    public boolean isEmpty() {
        return card == MagicCard.NONE;
    }
       
    public MagicCardDefinition getFaceupCardDef() {
        return faceupCardDef;
    }

    public MagicCard getMagicCard() {
        return card;
    }

    public MagicObject getMagicObject() {
        return magicObject;
    }

    public BufferedImage getImage() {
        return CachedImagesProvider.getInstance().getImage(faceupCardDef, 0, true);
    }

    private MagicCardDefinition getFaceupCardDef(MagicCardOnStack aCard) {
        return aCard.getController().isHuman() || MagicSystem.isAiVersusAi()
            ? aCard.getRealCardDefinition()
            : aCard.getCardDefinition();
    }

    private MagicCardDefinition getFaceupCardDef(MagicPermanent aCard) {
        return aCard.getController().isHuman() || MagicSystem.isAiVersusAi()
            ? aCard.getRealCardDefinition()
            : aCard.getCardDefinition();
    }

}
