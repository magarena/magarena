package magic.cardBuilder.renderers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import magic.model.MagicColor;
import magic.model.MagicType;
import magic.ui.ImageFileIO;
import magic.ui.MagicImages;
import magic.model.IRenderableCard;
import magic.cardBuilder.ResourceManager;
import magic.cardBuilder.CardResource;
import magic.ui.helpers.ImageHelper;
import magic.utility.MagicFileSystem;

public class ImageFrame {

    private static BufferedImage defaultBackground(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(CardResource.defaultWhite);
            case Blue:
                return ResourceManager.newFrame(CardResource.defaultBlue);
            case Black:
                return ResourceManager.newFrame(CardResource.defaultBlack);
            case Red:
                return ResourceManager.newFrame(CardResource.defaultRed);
            case Green:
                return ResourceManager.newFrame(CardResource.defaultGreen);
        }
        return null;
    }

    private static BufferedImage defaultSymbol(MagicType type) {
        switch (type) {
            case Artifact:
                return ResourceManager.newFrame(CardResource.artifactSymbol);
            case Creature:
                return ResourceManager.newFrame(CardResource.creatureSymbol);
            case Enchantment:
                return ResourceManager.newFrame(CardResource.enchantmentSymbol);
            case Instant:
                return ResourceManager.newFrame(CardResource.instantSymbol);
            case Land:
                return ResourceManager.newFrame(CardResource.landSymbol);
            case Planeswalker:
                return ResourceManager.newFrame(CardResource.planeswalkerSymbol);
            case Sorcery:
                return ResourceManager.newFrame(CardResource.sorcerySymbol);
            default:
                return null;
        }
    }

    static void drawImage(BufferedImage card, IRenderableCard cardDef) {
        Graphics2D g2d = card.createGraphics();
        if (cardDef.isPlaneswalker()) {
            g2d.drawImage(getCardImage(cardDef), null, 27, 54);
        } else if (cardDef.isToken()) {
            g2d.drawImage(getCardImage(cardDef), null, 29, 64);
        } else {
            g2d.drawImage(getCardImage(cardDef), null, 29, 60);
        }
        g2d.dispose();
    }

    private static BufferedImage getDefaultBackground(IRenderableCard cardDef) {
        if (cardDef.isMulti()) {
            if (cardDef.isHybrid() || (cardDef.isToken() && cardDef.getNumColors() == 2)) {
                List<BufferedImage> colorDefaults = Frame.getColorPairOrder(cardDef).stream().filter(cardDef::hasColor).map(ImageFrame::defaultBackground).collect(Collectors.toList());
                return Frame.getBlendedFrame(
                    ResourceManager.newFrame(colorDefaults.get(0)),
                    ResourceManager.newFrame(CardResource.defaultHybridBlend),
                    ResourceManager.newFrame(colorDefaults.get(1))
                );
            } else {
                return ResourceManager.newFrame(CardResource.defaultMulti);
            }
        }
        for (MagicColor color : MagicColor.values()) {
            if (cardDef.hasColor(color)) {
                return defaultBackground(color);
            }
        }
        if (cardDef.hasType(MagicType.Land)) {
            return ResourceManager.newFrame(CardResource.defaultLand);
        }
        if (cardDef.hasType(MagicType.Artifact)) {
            return ResourceManager.newFrame(CardResource.defaultArtifact);
        }
        return ResourceManager.newFrame(CardResource.defaultColorless);
    }

    private static BufferedImage getDefaultSymbol(IRenderableCard cardDef) {
        Set<MagicType> cardTypes = cardDef.getTypes();
        cardTypes.removeAll(MagicType.SUPERTYPES);
        cardTypes.remove(MagicType.Tribal);
        if (cardTypes.size() > 1) {
            if (cardDef.isCreature()) {
                return ResourceManager.newFrame(CardResource.creatureSymbol);
            }
            return ResourceManager.newFrame(CardResource.multiSymbol);
        }
        if (!cardTypes.isEmpty()) {
            return defaultSymbol(cardTypes.iterator().next());
        }
        return ResourceManager.newFrame(CardResource.magarenaSymbol);
    }

    public static BufferedImage getCardImage(IRenderableCard cardDef) {
        File cropFile = MagicFileSystem.getCroppedCardImage(cardDef);
        if (cropFile.exists()) {
            if (cardDef.isPlaneswalker()) {
                BufferedImage crop = ImageHelper.scale(ImageFileIO.toImg(cropFile, MagicImages.MISSING_CARD), 320, 234);
                if (OracleText.getPlaneswalkerAbilityCount(cardDef) <= 3) {
                    BufferedImage blend = ResourceManager.newFrame(CardResource.getPlaneswalkerImageBlend);
                    return Frame.getBlendedFrame(new BufferedImage(320, 234, BufferedImage.TYPE_INT_ARGB), blend, crop);
                } else {
                    BufferedImage cropSmall = crop.getSubimage(0, 0, 320, 201);
                    BufferedImage blend = ImageHelper.scale(ResourceManager.newFrame(CardResource.getPlaneswalkerImageBlend), 320, 201);
                    return Frame.getBlendedFrame(new BufferedImage(320, 210, BufferedImage.TYPE_INT_ARGB), blend, cropSmall);
                }
            } else if (cardDef.isToken()) {
                if (cardDef.hasText()) {
                    BufferedImage crop = ImageHelper.scale(ImageFileIO.toImg(cropFile, MagicImages.MISSING_CARD), 320, 289);
                    BufferedImage blend = ResourceManager.newFrame(CardResource.tokenImageMaskSmall);
                    return Frame.getBlendedFrame(new BufferedImage(320, 289, BufferedImage.TYPE_INT_ARGB), blend, crop);
                } else {
                    BufferedImage crop = ImageHelper.scale(ImageFileIO.toImg(cropFile, MagicImages.MISSING_CARD), 320, 360);
                    BufferedImage blend = ResourceManager.newFrame(CardResource.tokenImageMaskLarge);
                    return Frame.getBlendedFrame(new BufferedImage(320, 360, BufferedImage.TYPE_INT_ARGB), blend, crop);
                }
            } else {
                BufferedImage image = ImageFileIO.toImg(cropFile, MagicImages.MISSING_CARD);
                return ImageHelper.scale(image, 316, 231);
            }
        }
        if (cardDef.isPlaneswalker()) {
            return buildPlaneswalkerImage(cardDef);
        }
        if (cardDef.isToken()) {
            return buildTokenImage(cardDef);
        }
        return buildDefaultImage(cardDef);
    }

    private static BufferedImage buildDefaultImage(IRenderableCard cardDef) {
        File fullImage = MagicFileSystem.getPrintedCardImage(cardDef);
        if (fullImage.exists()) {
            BufferedImage full = ImageHelper.scale(ImageFileIO.toImg(fullImage, MagicImages.MISSING_CARD),375,523);
            return full.getSubimage(29, 60, 316, 231);
        }
        return getCompositeImage(getDefaultBackground(cardDef), getDefaultSymbol(cardDef));
    }

    private static BufferedImage buildPlaneswalkerImage(IRenderableCard cardDef) {
        int WIDTH = 320;
        int HEIGHT;
        BufferedImage blend;
        if (OracleText.getPlaneswalkerAbilityCount(cardDef) <= 3) {
            HEIGHT = 234;
            blend = ResourceManager.newFrame(CardResource.getPlaneswalkerImageBlend);
        } else {
            HEIGHT = 201;
            blend = ImageHelper.scale(ResourceManager.newFrame(CardResource.getPlaneswalkerImageBlend), WIDTH, HEIGHT);
        }
        BufferedImage background = ImageHelper.scale(getDefaultBackground(cardDef), WIDTH, HEIGHT);
        BufferedImage symbol = ImageHelper.scale(getDefaultSymbol(cardDef), WIDTH, HEIGHT);
        BufferedImage image = Frame.getBlendedFrame(new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB), blend, background);
        return getCompositeImage(image, symbol);
    }

    private static BufferedImage buildTokenImage(IRenderableCard cardDef) {
        int HEIGHT;
        BufferedImage blend;
        if (cardDef.hasText()) {
            HEIGHT = 289;
            blend = ResourceManager.newFrame(CardResource.tokenImageMaskSmall);
        } else {
            HEIGHT = 360;
            blend = ResourceManager.newFrame(CardResource.tokenImageMaskLarge);
        }
        int WIDTH = 317;
        BufferedImage background = ImageHelper.scale(getDefaultBackground(cardDef), WIDTH, HEIGHT);
        BufferedImage symbol = ImageHelper.scale(getDefaultSymbol(cardDef), WIDTH, HEIGHT);
        BufferedImage image = Frame.getBlendedFrame(new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB), blend, background);
        return getCompositeImage(image,symbol);
    }

    static BufferedImage getCompositeImage(BufferedImage baseFrame, BufferedImage topFrame) {
        //create top Image
        Graphics2D graphics2D = topFrame.createGraphics();
        graphics2D.drawImage(topFrame, null, 0, 0);
        graphics2D.dispose();

        //draw top on top of baseFrame
        Graphics2D graphics2D1 = baseFrame.createGraphics();
        graphics2D1.drawImage(topFrame, null, 0, 0);
        graphics2D1.dispose();
        return ResourceManager.newFrame(baseFrame);
    }
}
