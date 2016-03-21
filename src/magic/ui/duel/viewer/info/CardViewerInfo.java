package magic.ui.duel.viewer.info;

import java.awt.image.BufferedImage;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicLocationType;
import magic.model.MagicObject;
import magic.model.MagicPermanent;
import magic.model.MagicType;
import magic.model.stack.MagicCardOnStack;
import magic.ui.MagicImages;
import magic.utility.MagicSystem;

public class CardViewerInfo {

    public static final CardViewerInfo NO_CARD = new CardViewerInfo(MagicCard.NONE);

    private final MagicCard magicCard;
    private final MagicCardDefinition frontFace;
    private final MagicCardDefinition backFace;
    private final MagicObject magicObject;

    CardViewerInfo(final MagicCard aCard) {
        this.magicCard = aCard;
        this.magicObject = aCard;
        this.frontFace = aCard.getCardDefinition();
        this.backFace = getBackFace(frontFace);
    }

    CardViewerInfo(MagicPermanent aPerm) {
        this.magicCard = aPerm.getCard();
        this.magicObject = aPerm;
        this.frontFace = getFrontFace(aPerm);
        this.backFace = getBackFace(frontFace);
    }

    CardViewerInfo(MagicCardOnStack aCard) {
        this.magicCard = aCard.getCard();
        this.magicObject = aCard;
        this.frontFace = getFrontFace(aCard);
        this.backFace = getBackFace(frontFace);
    }

    public boolean isEmpty() {
        return magicCard == MagicCard.NONE;
    }

    public boolean isNotEmpty() {
        return isEmpty() == false;
    }

    public boolean isLand() {
        return magicCard.hasType(MagicType.Land);
    }

    public MagicObject getMagicObject() {
        return magicObject;
    }

    public BufferedImage getImage() {
        return MagicImages.getCardImage(frontFace);
    }

    public BufferedImage getBackFaceImage() {
        return backFace == MagicCardDefinition.UNKNOWN
            ? MagicImages.BACK_IMAGE
            : MagicImages.getCardImage(backFace);
    }

    public MagicLocationType getLocation() {
        return magicCard.getLocation();
    }

    public long getId() {
        return magicCard.getId();
    }

    public int getControllerIndex() {
        return magicCard.getController().getIndex();
    }

    public boolean usesStack() {
        return frontFace.usesStack();
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

    private MagicCardDefinition getBackFace(MagicCardDefinition aCard) {
        return aCard.isDoubleFaced()
            ? aCard.getTransformedDefinition()
            : MagicCardDefinition.UNKNOWN;
    }

}
