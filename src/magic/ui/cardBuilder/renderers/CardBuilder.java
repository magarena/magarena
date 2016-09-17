package magic.ui.cardBuilder.renderers;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import magic.model.MagicAbility;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.ui.cardBuilder.IRenderableCard;

public class CardBuilder {

    public static boolean IS_LOADED;

    public static BufferedImage getCardBuilderImage(IRenderableCard cardDef) {

        BufferedImage image;

        //Frame type hierarchy may need adjusting
        if (cardDef.isDoubleFaced()) {
            image = cardDef.isPlaneswalker() ? makeTransformPlaneswalker(cardDef) : makeTransform(cardDef);
        } else if (cardDef.hasType(MagicType.Planeswalker)) {
            image = makePlaneswalker(cardDef);
        } else if (cardDef.isToken()) {
            image = makeToken(cardDef);
        } else if (cardDef.isFlipCard()) {
            image = makeFlipCard(cardDef);
        } else if (cardDef.isSplitCard()) {
            image = makeSplitCard(cardDef);
        } else {
            image = makeBasicCard(cardDef);
        }
        IS_LOADED = true;
        return image;
    }

    private static BufferedImage makeTransform(IRenderableCard cardDef) {
        BufferedImage cardImage = Frame.getTransformFrameType(cardDef);
        if (cardDef.isHidden()) {
            PTFrame.drawHiddenPTPanel(cardImage, cardDef);
        } else {
            PTFrame.drawPTPanel(cardImage, cardDef);
        }
        TypeLine.drawCardTypeLine(cardImage, cardDef);
        TypeLine.drawRarity(cardImage, cardDef);
        TitleFrame.drawTransformCardName(cardImage, cardDef);
        ImageFrame.drawImage(cardImage, cardDef);
        TitleFrame.drawManaCost(cardImage, cardDef);
        PTFrame.drawTransformSymbol(cardImage, cardDef);
        OracleText.drawOracleText(cardImage, cardDef);
        return trimImage(cardImage);
    }

    private static BufferedImage makeTransformPlaneswalker(IRenderableCard cardDef) {
        BufferedImage cardImage = Frame.getTransformPlaneswalkerFrameType(cardDef);
        ImageFrame.drawImage(cardImage, cardDef);
        OracleText.drawPlaneswalkerOracleText(cardImage, cardDef);
        PTFrame.drawLoyaltyPanels(cardImage, cardDef);
        PTFrame.drawTransformSymbol(cardImage, cardDef);
        TitleFrame.drawTransformCardName(cardImage, cardDef);
        TitleFrame.drawManaCost(cardImage, cardDef);
        TypeLine.drawCardTypeLine(cardImage, cardDef);
        TypeLine.drawRarity(cardImage, cardDef);
        return trimImage(cardImage);
    }

    private static BufferedImage makeFlipCard(IRenderableCard cardDef) {
        return makeBasicCard(cardDef);
    }

    private static BufferedImage makeSplitCard(IRenderableCard cardDef) {
        BufferedImage firstHalf;
        BufferedImage secondHalf;
        if (cardDef.isSecondHalf()) {
            firstHalf = makeBasicCard(cardDef.getSplitDefinition());
            secondHalf = makeBasicCard(cardDef);
        } else {
            firstHalf = makeBasicCard(cardDef);
            secondHalf = makeBasicCard(cardDef.getSplitDefinition());
        }
        BufferedImage base = new BufferedImage(firstHalf.getHeight(), firstHalf.getWidth() << 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = base.createGraphics();
        //Transforms occur in reverse order
        AffineTransform affineTransform1 = new AffineTransform();
        affineTransform1.translate(0, firstHalf.getWidth() << 1);
        affineTransform1.rotate(-Math.PI / 2, 0, 0);
        graphics2D.drawImage(firstHalf, affineTransform1, null);

        AffineTransform affineTransform2 = new AffineTransform();
        affineTransform2.translate(0, secondHalf.getWidth());
        affineTransform2.rotate(-Math.PI / 2, 0, 0);
        graphics2D.drawImage(secondHalf, affineTransform2, null);

        graphics2D.dispose();
        return base;
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
        BufferedImage cardImage = determineBasicFrame(cardDef);
        Overlay.drawOverlay(cardImage, cardDef);
        Overlay.drawTextOverlay(cardImage, cardDef);
        if (cardDef.hasAbility(MagicAbility.LevelUp)) {
            PTFrame.drawLevellerPTPanels(cardImage, cardDef);
            PTFrame.drawLevellerArrowText(cardImage, cardDef);
            OracleText.drawLevellerOracleText(cardImage, cardDef);
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

    private static BufferedImage determineBasicFrame(IRenderableCard cardDef) {
        if (cardDef.hasAbility(MagicAbility.Devoid)) {
            return Frame.getColorlessFrameType(cardDef);
        } else if (cardDef.hasSubType(MagicSubType.Vehicle)) {
            return Frame.getVehicleFrameType(cardDef);
        } else {
            return Frame.getBasicFrameType(cardDef);
        }
    }

}
