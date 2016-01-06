package magic.ui.cardBuilder.renderers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import magic.model.MagicAbility;
import magic.model.MagicColor;
import magic.ui.cardBuilder.IRenderableCard;
import magic.ui.cardBuilder.ResourceManager;

public class Overlay {

    static BufferedImage getDevoidMask(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(ResourceManager.whiteDevoidFrame);
            case Blue:
                return ResourceManager.newFrame(ResourceManager.blueDevoidFrame);
            case Black:
                return ResourceManager.newFrame(ResourceManager.blackDevoidFrame);
            case Green:
                return ResourceManager.newFrame(ResourceManager.greenDevoidFrame);
            case Red:
                return ResourceManager.newFrame(ResourceManager.redDevoidFrame);
        }
        return ResourceManager.newFrame(ResourceManager.colorlessDevoidFrame);
    }

    static BufferedImage getMiracleMask(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(ResourceManager.whiteMiracle);
            case Blue:
                return ResourceManager.newFrame(ResourceManager.blueMiracle);
            case Black:
                return ResourceManager.newFrame(ResourceManager.blackMiracle);
            case Green:
                return ResourceManager.newFrame(ResourceManager.greenMiracle);
            case Red:
                return ResourceManager.newFrame(ResourceManager.redMiracle);
        }
        return ResourceManager.newFrame(ResourceManager.colorlessMiracle);
    }

    static BufferedImage getDevoidOverlay(IRenderableCard cardDef) {
        boolean artifact = cardDef.isArtifact();
        BufferedImage baseFrame = ResourceManager.newFrame(ResourceManager.colorlessDevoidFrame);
        if (artifact) {
            baseFrame = ResourceManager.newFrame(ResourceManager.artifactDevoidFrame);
        }
        if (cardDef.isMulti()) {
            if (cardDef.isHybrid()) {
                List<BufferedImage> colorFrames = new ArrayList<>();
                colorFrames.addAll(Frame.getColorOrder(cardDef).stream().map(Overlay::getDevoidMask).collect(Collectors.toList()));
                return Frame.getBlendedFrame(
                    ResourceManager.newFrame(colorFrames.get(0)),
                    ResourceManager.newFrame(ResourceManager.gainHybridBlend),
                    ResourceManager.newFrame(colorFrames.get(1))
                );
            } else {
                return ResourceManager.newFrame(ResourceManager.multiDevoidFrame);
            }
        }
        //Mono
        for (MagicColor color : MagicColor.values()) {
            if (cardDef.hasColor(color)) {
                return getDevoidMask(color);
            }
        }
        //Colorless
        return baseFrame;
    }

    static BufferedImage getMiracleOverlay(IRenderableCard cardDef) {
        boolean artifact = cardDef.isArtifact();
        BufferedImage baseFrame = ResourceManager.newFrame(ResourceManager.colorlessMiracle);
        if (artifact) {
            baseFrame = ResourceManager.newFrame(ResourceManager.artifactMiracle);
        }
        if (cardDef.isMulti()) {
            if (cardDef.isHybrid()) {
                List<BufferedImage> colorFrames = new ArrayList<>();
                colorFrames.addAll(Frame.getColorOrder(cardDef).stream().map(Overlay::getMiracleMask).collect(Collectors.toList()));
                return Frame.getBlendedFrame(
                    ResourceManager.newFrame(colorFrames.get(0)),
                    ResourceManager.newFrame(ResourceManager.gainHybridBlend),
                    ResourceManager.newFrame(colorFrames.get(1))
                );
            } else {
                return ResourceManager.newFrame(ResourceManager.multiMiracle);
            }
        }
        //Mono
        for (MagicColor color : MagicColor.values()) {
            if (cardDef.hasColor(color)) {
                return getMiracleMask(color);
            }
        }
        //Colorless
        return baseFrame;
    }

    public static void drawOverlay(BufferedImage cardImage, IRenderableCard cardDef) {
        if (cardDef.hasOverlay()) {
            BufferedImage overlay = getOverlay(cardDef);
            Graphics2D g2d = cardImage.createGraphics();
            g2d.drawImage(overlay, 0, 0, null);
            g2d.dispose();
        }
    }

    private static BufferedImage getOverlay(IRenderableCard cardDef) {
        return cardDef.hasAbility(MagicAbility.Devoid) ? getDevoidOverlay(cardDef) : getMiracleOverlay(cardDef);
    }

}
