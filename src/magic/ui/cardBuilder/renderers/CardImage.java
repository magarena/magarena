package magic.ui.cardBuilder.renderers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;

import magic.model.MagicColor;
import magic.model.MagicType;
import magic.ui.ImageFileIO;
import magic.ui.MagicImages;
import magic.ui.cardBuilder.IRenderableCard;
import magic.ui.cardBuilder.ResourceManager;
import magic.ui.utility.GraphicsUtils;
import magic.utility.MagicFileSystem;

public class CardImage {

    private static BufferedImage defaultImage(MagicColor color) {
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
            default:
                return ResourceManager.newFrame(ResourceManager.defaultColorless);
        }
    }

    static void drawImage(BufferedImage card, IRenderableCard cardDef) {
        Graphics2D g2d = card.createGraphics();
        g2d.drawImage(getCardImage(cardDef), null, 29, 60);
        g2d.dispose();
    }

    public static BufferedImage getDefaultImage(IRenderableCard cardDef) {
        if (cardDef.hasType(MagicType.Land)) {
            return ResourceManager.newFrame(ResourceManager.defaultLand);
        }
        if (cardDef.hasType(MagicType.Artifact)) {
            return ResourceManager.newFrame(ResourceManager.defaultArtifact);
        }
        if (cardDef.isMulti()) {
            if (cardDef.isHybrid()) {
                List<BufferedImage> colorDefaults = CardFrame.getColorOrder(cardDef).stream().filter(color -> cardDef.hasColor(color)).map(CardImage::defaultImage).collect(Collectors.toList());
                return CardFrame.getBlendedFrame(
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
                return defaultImage(color);
            }
        }
        return ResourceManager.newFrame(ResourceManager.defaultColorless);
    }

    public static BufferedImage getCardImage(IRenderableCard cardDef) {
        File cropFile = MagicFileSystem.getCroppedCardImageFile(cardDef);
        if (cropFile.exists()) {
            BufferedImage image = ImageFileIO.toImg(cropFile, MagicImages.MISSING_CARD);
            return GraphicsUtils.scale(image, 316, 231);
        } else {
            String fName = "/cardbuilder/images/" + cardDef.getImageName() + ".jpg";
            try (final InputStream is = ResourceManager.getJarResourceStream(fName)) {
                if (is != null) {
                    BufferedImage cardImage = GraphicsUtils.getOptimizedImage(ImageIO.read(is));
                    return GraphicsUtils.scale(cardImage, 316, 231);
                } else {
                    return getDefaultImage(cardDef);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
