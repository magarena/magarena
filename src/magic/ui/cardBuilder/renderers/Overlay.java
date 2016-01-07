package magic.ui.cardBuilder.renderers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    static BufferedImage getLevellerMask(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(ResourceManager.whiteLevellerBox);
            case Blue:
                return ResourceManager.newFrame(ResourceManager.blueLevellerBox);
            case Black:
                return ResourceManager.newFrame(ResourceManager.blackLevellerBox);
            case Green:
                return ResourceManager.newFrame(ResourceManager.greenLevellerBox);
            case Red:
                return ResourceManager.newFrame(ResourceManager.redLevellerBox);
        }
        return ResourceManager.newFrame(ResourceManager.colorlessLevellerBox);
    }

    static BufferedImage getLevellerLandMask(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(ResourceManager.whiteLandLevellerBox);
            case Blue:
                return ResourceManager.newFrame(ResourceManager.blueLandLevellerBox);
            case Black:
                return ResourceManager.newFrame(ResourceManager.blackLandLevellerBox);
            case Green:
                return ResourceManager.newFrame(ResourceManager.greenLandLevellerBox);
            case Red:
                return ResourceManager.newFrame(ResourceManager.redLandLevellerBox);
        }
        return ResourceManager.newFrame(ResourceManager.colorlessLandLevellerBox);
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

    static BufferedImage getLevellerOverlay(IRenderableCard cardDef) {
        boolean artifact = cardDef.isArtifact();
        boolean land = cardDef.isLand();
        Set<MagicColor> landColors = new HashSet<>();
        BufferedImage baseFrame = ResourceManager.newFrame(ResourceManager.colorlessLevellerBox);
        if (artifact) {
            return ResourceManager.newFrame(ResourceManager.artifactLevellerBox);
        }
        if (land) {
            landColors = Frame.getLandColors(cardDef);
        }
        if (cardDef.isMulti() || landColors.size() > 1) {
            if (cardDef.isHybrid() || landColors.size() == 2) {
                List<BufferedImage> colorFrames = new ArrayList<>(2);
                if (land) {
                    colorFrames.addAll(Frame.getColorOrder(landColors).stream().map(Overlay::getLevellerLandMask).collect(Collectors.toList()));
                } else {
                    colorFrames.addAll(Frame.getColorOrder(cardDef).stream().map(Overlay::getLevellerMask).collect(Collectors.toList()));
                }
                return Frame.getBlendedFrame(
                    ResourceManager.newFrame(colorFrames.get(0)),
                    ResourceManager.newFrame(ResourceManager.gainTextBoxHybridBlend),
                    ResourceManager.newFrame(colorFrames.get(1))
                );
            } else {
                return land ? ResourceManager.newFrame(ResourceManager.multiLandLevellerBox) : ResourceManager.newFrame(ResourceManager.multiLevellerBox);
            }
        }
        //Mono
        for (MagicColor color : MagicColor.values()) {
            if (cardDef.hasColor(color) || landColors.contains(color)) {
                return land ? getLevellerLandMask(color) : getLevellerMask(color);
            }
        }
        //Colorless
        return land ? ResourceManager.newFrame(ResourceManager.colorlessLandLevellerBox) : baseFrame;
    }

    public static void drawOverlay(BufferedImage cardImage, IRenderableCard cardDef) {
        if (cardDef.hasOverlay()) {
            BufferedImage overlay = getOverlay(cardDef);
            Graphics2D g2d = cardImage.createGraphics();
            g2d.drawImage(overlay, 0, 0, null);
            g2d.dispose();
        }
    }

    public static void drawTextOverlay(BufferedImage cardImage, IRenderableCard cardDef) {
        if (cardDef.hasTextOverlay()) {
            BufferedImage overlay = getTextOverlay(cardDef);
            Graphics2D g2d = cardImage.createGraphics();
            g2d.drawImage(overlay, 29, 327, null);
            g2d.dispose();
        }
    }

    private static BufferedImage getOverlay(IRenderableCard cardDef) {
        return cardDef.hasAbility(MagicAbility.Miracle) ? getMiracleOverlay(cardDef) : getDevoidOverlay(cardDef);
    }

    private static BufferedImage getTextOverlay(IRenderableCard cardDef) {
        if (cardDef.hasAbility(MagicAbility.LevelUp)) {
            return getLevellerOverlay(cardDef);
        }
        return null;
    }
}
