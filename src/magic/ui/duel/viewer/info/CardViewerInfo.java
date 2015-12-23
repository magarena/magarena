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

    private final MagicCard magicCard;
    private final MagicCardDefinition frontFace;
    private final MagicObject magicObject;

    CardViewerInfo(final MagicCard aCard) {
        this.magicCard = aCard;
        this.magicObject = aCard;
        this.frontFace = aCard.getCardDefinition();
    }

    CardViewerInfo(MagicPermanent aPerm) {
        this.magicCard = aPerm.getCard();
        this.magicObject = aPerm;
        this.frontFace = getFrontFace(aPerm);
    }

    CardViewerInfo(MagicCardOnStack aCard) {
        this.magicCard = aCard.getCard();
        this.magicObject = aCard;
        this.frontFace = getFrontFace(aCard);
    }

    public boolean isEmpty() {
        return magicCard == MagicCard.NONE;
    }
       
    public MagicCardDefinition getCardDefinition() {
        return frontFace;
    }

    public MagicCard getMagicCard() {
        return magicCard;
    }

    public MagicObject getMagicObject() {
        return magicObject;
    }

    public BufferedImage getImage() {
        return CachedImagesProvider.getInstance().getImage(frontFace, 0, true);
    }

    private MagicCardDefinition getFrontFace(MagicCardOnStack aCard) {
        return aCard.getController().isHuman() || MagicSystem.isAiVersusAi()
            ? aCard.getRealCardDefinition()
            : aCard.getCardDefinition();
    }

    private MagicCardDefinition getFrontFace(MagicPermanent aCard) {
        return aCard.getController().isHuman() || MagicSystem.isAiVersusAi()
            ? aCard.getRealCardDefinition()
            : aCard.getCardDefinition();
    }

}
