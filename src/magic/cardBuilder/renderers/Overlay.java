package magic.cardBuilder.renderers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import magic.cardBuilder.ResourceManager;
import magic.cardBuilder.CardResource;
import magic.model.IRenderableCard;
import magic.model.MagicAbility;
import magic.model.MagicColor;

public class Overlay {

    static BufferedImage getDevoidMask(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(CardResource.whiteDevoidFrame);
            case Blue:
                return ResourceManager.newFrame(CardResource.blueDevoidFrame);
            case Black:
                return ResourceManager.newFrame(CardResource.blackDevoidFrame);
            case Green:
                return ResourceManager.newFrame(CardResource.greenDevoidFrame);
            case Red:
                return ResourceManager.newFrame(CardResource.redDevoidFrame);
        }
        return ResourceManager.newFrame(CardResource.colorlessDevoidFrame);
    }

    static BufferedImage getMiracleMask(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(CardResource.whiteMiracle);
            case Blue:
                return ResourceManager.newFrame(CardResource.blueMiracle);
            case Black:
                return ResourceManager.newFrame(CardResource.blackMiracle);
            case Green:
                return ResourceManager.newFrame(CardResource.greenMiracle);
            case Red:
                return ResourceManager.newFrame(CardResource.redMiracle);
        }
        return ResourceManager.newFrame(CardResource.colorlessMiracle);
    }

    static BufferedImage getLevellerMask(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(CardResource.whiteLevellerBox);
            case Blue:
                return ResourceManager.newFrame(CardResource.blueLevellerBox);
            case Black:
                return ResourceManager.newFrame(CardResource.blackLevellerBox);
            case Green:
                return ResourceManager.newFrame(CardResource.greenLevellerBox);
            case Red:
                return ResourceManager.newFrame(CardResource.redLevellerBox);
        }
        return ResourceManager.newFrame(CardResource.colorlessLevellerBox);
    }

    static BufferedImage getLandMask(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(CardResource.whiteLandBox);
            case Blue:
                return ResourceManager.newFrame(CardResource.blueLandBox);
            case Black:
                return ResourceManager.newFrame(CardResource.blackLandBox);
            case Green:
                return ResourceManager.newFrame(CardResource.greenLandBox);
            case Red:
                return ResourceManager.newFrame(CardResource.redLandBox);
        }
        return ResourceManager.newFrame(CardResource.multiLandBox);
    }

    static BufferedImage getLevellerLandMask(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(CardResource.whiteLandLevellerBox);
            case Blue:
                return ResourceManager.newFrame(CardResource.blueLandLevellerBox);
            case Black:
                return ResourceManager.newFrame(CardResource.blackLandLevellerBox);
            case Green:
                return ResourceManager.newFrame(CardResource.greenLandLevellerBox);
            case Red:
                return ResourceManager.newFrame(CardResource.redLandLevellerBox);
        }
        return ResourceManager.newFrame(CardResource.colorlessLandLevellerBox);
    }

    static BufferedImage getDevoidOverlay(IRenderableCard cardDef) {
        boolean artifact = cardDef.isArtifact();
        BufferedImage baseFrame = ResourceManager.newFrame(CardResource.colorlessDevoidFrame);
        if (artifact) {
            baseFrame = ResourceManager.newFrame(CardResource.artifactDevoidFrame);
        }
        if (cardDef.isMulti()) {
            if (cardDef.isHybrid()) {
                List<BufferedImage> colorFrames = new ArrayList<>();
                colorFrames.addAll(Frame.getColorPairOrder(cardDef).stream().map(Overlay::getDevoidMask).collect(Collectors.toList()));
                return Frame.getBlendedFrame(
                    ResourceManager.newFrame(colorFrames.get(0)),
                    ResourceManager.newFrame(CardResource.gainHybridBlend),
                    ResourceManager.newFrame(colorFrames.get(1))
                );
            } else {
                return ResourceManager.newFrame(CardResource.multiDevoidFrame);
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
        BufferedImage baseFrame = ResourceManager.newFrame(CardResource.colorlessMiracle);
        if (artifact) {
            baseFrame = ResourceManager.newFrame(CardResource.artifactMiracle);
        }
        if (cardDef.isMulti()) {
            if (cardDef.isHybrid()) {
                List<BufferedImage> colorFrames = new ArrayList<>();
                colorFrames.addAll(Frame.getColorPairOrder(cardDef).stream().map(Overlay::getMiracleMask).collect(Collectors.toList()));
                return Frame.getBlendedFrame(
                    ResourceManager.newFrame(colorFrames.get(0)),
                    ResourceManager.newFrame(CardResource.gainHybridBlend),
                    ResourceManager.newFrame(colorFrames.get(1))
                );
            } else {
                return ResourceManager.newFrame(CardResource.multiMiracle);
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
        BufferedImage baseFrame = ResourceManager.newFrame(CardResource.colorlessLevellerBox);
        if (artifact) {
            return ResourceManager.newFrame(CardResource.artifactLevellerBox);
        }
        if (land) {
            landColors = Frame.getLandColors(cardDef);
        }
        if (cardDef.isMulti() || landColors.size() > 1) {
            if (cardDef.isHybrid() || landColors.size() == 2) {
                List<BufferedImage> colorFrames = new ArrayList<>(2);
                if (land) {
                    colorFrames.addAll(Frame.getColorPairOrder(landColors).stream().map(Overlay::getLevellerLandMask).collect(Collectors.toList()));
                } else {
                    colorFrames.addAll(Frame.getColorPairOrder(cardDef).stream().map(Overlay::getLevellerMask).collect(Collectors.toList()));
                }
                return Frame.getBlendedFrame(
                    ResourceManager.newFrame(colorFrames.get(0)),
                    ResourceManager.newFrame(CardResource.gainTextBoxHybridBlend),
                    ResourceManager.newFrame(colorFrames.get(1))
                );
            } else {
                return ResourceManager.newFrame(land ? CardResource.multiLandLevellerBox : CardResource.multiLevellerBox);
            }
        }
        //Mono
        for (MagicColor color : MagicColor.values()) {
            if (cardDef.hasColor(color) || landColors.contains(color)) {
                return land ? getLevellerLandMask(color) : getLevellerMask(color);
            }
        }
        //Colorless
        return land ? ResourceManager.newFrame(CardResource.colorlessLandLevellerBox) : baseFrame;
    }

    static BufferedImage getLandOverlay(IRenderableCard cardDef) {
        Set<MagicColor> landColors = Frame.getLandColors(cardDef);
        if (landColors.size() > 1) {
            if (landColors.size() == 2) {
                List<BufferedImage> colorFrames = new ArrayList<>(2);
                colorFrames.addAll(Frame.getColorPairOrder(landColors).stream().map(Overlay::getLandMask).collect(Collectors.toList()));
                return Frame.getBlendedFrame(
                    ResourceManager.newFrame(colorFrames.get(0)),
                    ResourceManager.newFrame(CardResource.gainTextBoxHybridBlend),
                    ResourceManager.newFrame(colorFrames.get(1))
                );
            } else {
                return ResourceManager.newFrame(CardResource.multiLandBox);
            }
        }
        //Mono
        for (MagicColor color : MagicColor.values()) {
            if (cardDef.hasColor(color) || landColors.contains(color)) {
                return getLandMask(color);
            }
        }
        return ResourceManager.newFrame(CardResource.multiLandBox);
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
        return cardDef.hasAbility(MagicAbility.LevelUp) ? getLevellerOverlay(cardDef) : getLandOverlay(cardDef);
    }
}
