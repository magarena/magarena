package magic.ui.cardBuilder.renderers;

import java.awt.image.BufferedImage;

import magic.model.MagicAbility;
import magic.model.MagicType;
import magic.ui.cardBuilder.IRenderableCard;

public class CardBuilder {

    public static BufferedImage getCardBuilderImage(IRenderableCard cardDef) {
        //Frame type hierarchy may need adjusting
        if (cardDef.isDoubleFaced() && !cardDef.isPlaneswalker()) {
            return makeTransform(cardDef);
        }
        if (cardDef.hasType(MagicType.Planeswalker)) {
            return makePlaneswalker(cardDef);
        }
        if (cardDef.isToken()) {
            return makeToken(cardDef);
        }
        if (cardDef.isFlipCard()) {
            return makeFlipCard(cardDef);
        }
        return makeBasicCard(cardDef);
    }

    private static BufferedImage makeTransform(IRenderableCard cardDef) {
        BufferedImage cardImage = Frame.getTransformFrameType(cardDef);
        if (cardDef.isHidden()) {
            PTFrame.drawHiddenPTPanel(cardImage, cardDef);
            TypeLine.drawHiddenCardTypeLine(cardImage, cardDef);
        } else {
            PTFrame.drawPTPanel(cardImage, cardDef);
            TypeLine.drawCardTypeLine(cardImage, cardDef);
        }
        TypeLine.drawRarity(cardImage, cardDef);
        TitleFrame.drawTransformCardName(cardImage, cardDef);
        ImageFrame.drawImage(cardImage, cardDef);
        TitleFrame.drawManaCost(cardImage, cardDef);
        PTFrame.drawTransformSymbol(cardImage, cardDef);
        OracleText.drawOracleText(cardImage, cardDef);
        return trimImage(cardImage);
    }

    private static BufferedImage makeFlipCard(IRenderableCard cardDef) {
        return makeBasicCard(cardDef);
    }

    private static BufferedImage makeToken(IRenderableCard cardDef) {
        BufferedImage cardImage = Frame.getTokenFrameType(cardDef);
        PTFrame.drawPTPanel(cardImage, cardDef);
        OracleText.drawOracleText(cardImage, cardDef);
        TitleFrame.drawCardName(cardImage, cardDef);
        TypeLine.drawCardTypeLine(cardImage, cardDef);
        TypeLine.drawRarity(cardImage, cardDef);
        return trimImage(cardImage);
    }

    private static BufferedImage makePlaneswalker(IRenderableCard cardDef) {
        BufferedImage cardImage = Frame.getPlaneswalkerFrameType(cardDef);
        ImageFrame.drawImage(cardImage, cardDef);
        OracleText.drawPlaneswalkerOracleText(cardImage, cardDef);
        PTFrame.drawLoyaltyPanels(cardImage, cardDef);
        TitleFrame.drawCardName(cardImage, cardDef);
        TitleFrame.drawManaCost(cardImage, cardDef);
        TypeLine.drawCardTypeLine(cardImage, cardDef);
        TypeLine.drawRarity(cardImage, cardDef);
        return trimImage(cardImage);
    }

    private static BufferedImage makeBasicCard(IRenderableCard cardDef) {
        BufferedImage cardImage = cardDef.hasAbility(MagicAbility.Devoid) ? Frame.getColorlessFrameType(cardDef) : Frame.getBasicFrameType(cardDef);
        Overlay.drawOverlay(cardImage, cardDef);
        Overlay.drawTextOverlay(cardImage,cardDef);
        if (cardDef.hasAbility(MagicAbility.LevelUp)){
            PTFrame.drawLevellerPTPanels(cardImage,cardDef);
            PTFrame.drawLevellerArrowText(cardImage,cardDef);
            OracleText.drawLevellerOracleText(cardImage,cardDef);
        } else {
            PTFrame.drawPTPanel(cardImage, cardDef);
            OracleText.drawOracleText(cardImage, cardDef);
        }
        ImageFrame.drawImage(cardImage, cardDef);
        TitleFrame.drawCardName(cardImage, cardDef);
        TitleFrame.drawManaCost(cardImage, cardDef);
        TypeLine.drawCardTypeLine(cardImage, cardDef);
        TypeLine.drawRarity(cardImage, cardDef);
        return trimImage(cardImage);
    }

    // Remove rounded corners from original image
    private static BufferedImage trimImage(BufferedImage image) {
        int cropSize = 5;
        return image.getSubimage(
            cropSize, cropSize,
            image.getWidth() - 2 * cropSize,
            image.getHeight() - 2 * cropSize
        );
    }

}
