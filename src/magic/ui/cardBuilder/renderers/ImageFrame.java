package magic.ui.cardBuilder.renderers;

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
import magic.ui.cardBuilder.IRenderableCard;
import magic.ui.cardBuilder.ResourceManager;
import magic.ui.utility.GraphicsUtils;
import magic.utility.MagicFileSystem;

public class ImageFrame {

    private static BufferedImage defaultBackground(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(ResourceManager.defaultWhite);
            case Blue:
                return ResourceManager.newFrame(ResourceManager.defaultBlue);
            case Black:
                return ResourceManager.newFrame(ResourceManager.defaultBlack);
            case Red:
                return ResourceManager.newFrame(ResourceManager.defaultRed);
            case Green:
                return ResourceManager.newFrame(ResourceManager.defaultGreen);
        }
        return null;
    }

    private static BufferedImage defaultSymbol(MagicType type) {
        switch (type) {
            case Artifact:
                return ResourceManager.newFrame(ResourceManager.artifactSymbol);
            case Creature:
                return ResourceManager.newFrame(ResourceManager.creatureSymbol);
            case Enchantment:
                return ResourceManager.newFrame(ResourceManager.enchantmentSymbol);
            case Instant:
                return ResourceManager.newFrame(ResourceManager.instantSymbol);
            case Land:
                return ResourceManager.newFrame(ResourceManager.landSymbol);
            case Planeswalker:
                return ResourceManager.newFrame(ResourceManager.planeswalkerSymbol);
            case Sorcery:
                return ResourceManager.newFrame(ResourceManager.sorcerySymbol);
            default:
                return null;
        }
    }

    static void drawImage(BufferedImage card, IRenderableCard cardDef) {
        Graphics2D g2d = card.createGraphics();
        if (cardDef.isPlaneswalker()) {
            g2d.drawImage(getCardImage(cardDef), null, 27, 54);
        } else {
            g2d.drawImage(getCardImage(cardDef), null, 29, 60);
        }
        g2d.dispose();
    }

    public static BufferedImage getDefaultBackground(IRenderableCard cardDef) {
        if (cardDef.isMulti()) {
            if (cardDef.isHybrid()) {
                List<BufferedImage> colorDefaults = Frame.getColorPairOrder(cardDef).stream().filter(cardDef::hasColor).map(ImageFrame::defaultBackground).collect(Collectors.toList());
                return Frame.getBlendedFrame(
                    ResourceManager.newFrame(colorDefaults.get(0)),
                    ResourceManager.newFrame(ResourceManager.defaultHybridBlend),
                    ResourceManager.newFrame(colorDefaults.get(1))
                );
            } else {
                return ResourceManager.newFrame(ResourceManager.defaultMulti);
            }
        }
        for (MagicColor color : MagicColor.values()) {
            if (cardDef.hasColor(color)) {
                return defaultBackground(color);
            }
        }
        if (cardDef.hasType(MagicType.Land)) {
            return ResourceManager.newFrame(ResourceManager.defaultLand);
        }
        if (cardDef.hasType(MagicType.Artifact)) {
            return ResourceManager.newFrame(ResourceManager.defaultArtifact);
        }
        return ResourceManager.newFrame(ResourceManager.defaultColorless);
    }

    public static BufferedImage getDefaultSymbol(IRenderableCard cardDef) {
        Set<MagicType> cardTypes = cardDef.getTypes();
        cardTypes.remove(MagicType.Legendary);
        cardTypes.remove(MagicType.Tribal);
        cardTypes.remove(MagicType.World);
        cardTypes.remove(MagicType.Basic);
        cardTypes.remove(MagicType.Snow);
        if (cardTypes.size() > 1) {
            if (cardDef.isCreature()) {
                return ResourceManager.newFrame(ResourceManager.creatureSymbol);
            }
            return ResourceManager.newFrame(ResourceManager.multiSymbol);
        }
        for (MagicType type : MagicType.values()) {
            if (cardTypes.contains(type)) {
                return defaultSymbol(type);
            }
        }
        return ResourceManager.newFrame(ResourceManager.magarenaSymbol);
    }

    public static BufferedImage getCardImage(IRenderableCard cardDef) {
        File cropFile = MagicFileSystem.getCroppedCardImageFile(cardDef);
        if (cropFile.exists()) {
            if (cardDef.isPlaneswalker()) {
                BufferedImage crop = GraphicsUtils.scale(ImageFileIO.toImg(cropFile, MagicImages.MISSING_CARD), 320, 234);
                if (OracleText.getPlaneswalkerAbilityCount(cardDef) == 3) {
                    BufferedImage blend = ResourceManager.newFrame(ResourceManager.getPlaneswalkerImageBlend);
                    return Frame.getBlendedFrame(new BufferedImage(320, 234, BufferedImage.TYPE_INT_ARGB), blend, crop);
                } else {
                    BufferedImage cropSmall = crop.getSubimage(0, 0, 320, 201);
                    BufferedImage blend = GraphicsUtils.scale(ResourceManager.newFrame(ResourceManager.getPlaneswalkerImageBlend), 320, 201);
                    return Frame.getBlendedFrame(new BufferedImage(320, 210, BufferedImage.TYPE_INT_ARGB), blend, cropSmall);
                }
            } else {
                BufferedImage image = ImageFileIO.toImg(cropFile, MagicImages.MISSING_CARD);
                return GraphicsUtils.scale(image, 316, 231);
            }
        }
        if (cardDef.isPlaneswalker()) {
            return buildPlaneswalkerImage(cardDef);
        }
        return buildDefaultImage(cardDef);
    }


    private static BufferedImage buildDefaultImage(IRenderableCard cardDef) {
        return getCompositeImage(getDefaultBackground(cardDef), getDefaultSymbol(cardDef));
    }

    private static BufferedImage buildPlaneswalkerImage(IRenderableCard cardDef) {
        int WIDTH = 320;
        int HEIGHT;
        BufferedImage blend;
        if (OracleText.getPlaneswalkerAbilityCount(cardDef) <= 3) {
            HEIGHT = 234;
            blend = ResourceManager.newFrame(ResourceManager.getPlaneswalkerImageBlend);
        } else {
            HEIGHT = 201;
            blend = GraphicsUtils.scale(ResourceManager.newFrame(ResourceManager.getPlaneswalkerImageBlend), WIDTH, HEIGHT);
        }
        BufferedImage background = GraphicsUtils.scale(getDefaultBackground(cardDef), WIDTH, HEIGHT);
        BufferedImage symbol = GraphicsUtils.scale(getDefaultSymbol(cardDef), WIDTH, HEIGHT);
        BufferedImage image = Frame.getBlendedFrame(new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB), blend, background);
        return getCompositeImage(image, symbol);
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
