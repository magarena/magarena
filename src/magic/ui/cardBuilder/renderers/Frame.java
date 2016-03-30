package magic.ui.cardBuilder.renderers;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import magic.model.MagicColor;
import magic.model.MagicManaType;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.event.MagicManaActivation;
import magic.ui.cardBuilder.IRenderableCard;
import magic.ui.cardBuilder.ResourceManager;

public class Frame {

    static BufferedImage getBasicFrameType(IRenderableCard cardDef) {
        BufferedImage baseFrame = ResourceManager.newFrame(ResourceManager.colorlessFrame);
        boolean land = cardDef.hasType(MagicType.Land);
        boolean artifact = cardDef.hasType(MagicType.Artifact);
        boolean enchantmentPermanent = cardDef.hasType(MagicType.Enchantment) &&
            (cardDef.hasType(MagicType.Creature) || cardDef.hasType(MagicType.Artifact));
        List<MagicColor> landColor = new ArrayList<>();
        if (land) {
            baseFrame = ResourceManager.newFrame(enchantmentPermanent ? ResourceManager.landNyx : ResourceManager.landFrame);
            //Land Colors
            landColor = getLandColors(cardDef);
        } else if (artifact) {
            baseFrame = ResourceManager.newFrame(enchantmentPermanent ? ResourceManager.artifactNyx : ResourceManager.artifactFrame);
        }
        //Multi
        if (cardDef.isMulti() || landColor.size() > 1) {
            if (cardDef.getNumColors() > 2 || land && landColor.size() > 2) {
                return artifact || land ? getBlendedFrame(
                    baseFrame,
                    ResourceManager.newFrame(ResourceManager.gainColorBlend),
                    ResourceManager.newFrame(ResourceManager.multiFrame)) : ResourceManager.newFrame(enchantmentPermanent ? ResourceManager.multiNyx : ResourceManager.multiFrame);
            } else {
                //Hybrid
                List<BufferedImage> colorFrames = new ArrayList<>(2);
                if (land) {
                    if (enchantmentPermanent) {
                        colorFrames.addAll(getColorPairOrder(landColor).stream().map(Frame::getLandNyxFrame).collect(Collectors.toList()));
                    } else {
                        colorFrames.addAll(getColorPairOrder(landColor).stream().map(Frame::getLandFrame).collect(Collectors.toList()));
                    }
                } else {
                    if (enchantmentPermanent) {
                        colorFrames.addAll(getColorPairOrder(cardDef).stream().map(Frame::getNyxFrame).collect(Collectors.toList()));
                    } else {
                        colorFrames.addAll(getColorPairOrder(cardDef).stream().map(Frame::getFrame).collect(Collectors.toList()));
                    }
                }
                //A colorless Banner with color piping
                BufferedImage colorlessBanner = getBannerFrame(
                    ResourceManager.newFrame(ResourceManager.colorlessFrame),
                    getBlendedFrame(
                        ResourceManager.newFrame(colorFrames.get(0)),
                        ResourceManager.newFrame(ResourceManager.gainHybridBlend),
                        ResourceManager.newFrame(colorFrames.get(1)))
                );
                //Check for Hybrid + Return colorless banner blend
                if (cardDef.isHybrid()) {
                    return colorlessBanner;
                }
                //Check dual color land + Return colorless banner blend
                if (land && landColor.size() == 2) {
                    return getBlendedFrame(
                        baseFrame,
                        ResourceManager.newFrame(ResourceManager.gainColorBlend),
                        ResourceManager.newFrame(colorlessBanner)
                    );
                }
                //Create Gold Banner blend
                BufferedImage goldBanner = getBannerFrame(
                    ResourceManager.newFrame(ResourceManager.multiFrame),
                    getBlendedFrame(
                        ResourceManager.newFrame(colorFrames.get(0)),
                        ResourceManager.newFrame(ResourceManager.gainHybridBlend),
                        ResourceManager.newFrame(colorFrames.get(1))
                    ));
                //Color piping for Dual-Color
                return getBlendedFrame(
                    artifact || land ? baseFrame : ResourceManager.newFrame(enchantmentPermanent ? ResourceManager.multiNyx : ResourceManager.multiFrame),
                    ResourceManager.newFrame(ResourceManager.gainColorBlend),
                    ResourceManager.newFrame(goldBanner));
            }
        }
        //Mono
        for (MagicColor color : MagicColor.values()) {
            if (cardDef.hasColor(color) || landColor.contains(color)) {
                if (land) {
                    return enchantmentPermanent ? getLandNyxFrame(color) : getLandFrame(color);
                } else if (artifact) {
                    return getColorBlendFrame(baseFrame, color);
                } else {
                    return enchantmentPermanent ? getNyxFrame(color) : getFrame(color);
                }
            }
        }
        //Colorless
        return baseFrame;
    }

    static BufferedImage getTokenFrameType(IRenderableCard cardDef) {
        boolean hasText = cardDef.hasText();
        boolean land = cardDef.hasType(MagicType.Land);
        boolean artifact = cardDef.hasType(MagicType.Artifact);
        List<MagicColor> landColor = new ArrayList<>();
        //Land Colors
        if (land) {
            landColor = getLandColors(cardDef);
        }
        BufferedImage baseFrame = getBaseTokenFrame(cardDef.getTypes(), hasText);
        boolean hybrid = cardDef.isHybrid() || cardDef.getNumColors() == 2;
        //Multi
        if (cardDef.isMulti() || landColor.size() > 1) {
            if (cardDef.getNumColors() > 2 || land && landColor.size() > 2) {
                if (artifact || land) {
                    return hasText ?
                        getBlendedFrame(
                            baseFrame,
                            ResourceManager.newFrame(ResourceManager.gainColorTokenBlendText),
                            ResourceManager.newFrame(ResourceManager.multiTokenFrameText)
                        ) :
                        getBlendedFrame(
                            baseFrame,
                            ResourceManager.newFrame(ResourceManager.gainColorTokenBlend),
                            ResourceManager.newFrame(ResourceManager.multiTokenFrame)
                        );
                } else {
                    return ResourceManager.newFrame(hasText ? ResourceManager.multiTokenFrameText : ResourceManager.multiTokenFrame);
                }
            } else {
                //Hybrid
                List<BufferedImage> colorFrames = new ArrayList<>(2);
                if (hasText) {
                    colorFrames.addAll(getColorPairOrder(cardDef).stream().map(Frame::getTokenFrameText).collect(Collectors.toList()));
                } else {
                    colorFrames.addAll(getColorPairOrder(cardDef).stream().map(Frame::getTokenFrame).collect(Collectors.toList()));
                }
                //A colorless Banner with color piping
                BufferedImage colorlessTokenBanner = hasText ?
                    getTokenBannerFrameText(
                        ResourceManager.newFrame(ResourceManager.colorlessTokenFrameText),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(ResourceManager.gainHybridBlend),
                            ResourceManager.newFrame(colorFrames.get(1)))
                    ) :
                    getTokenBannerFrame(
                        ResourceManager.newFrame(ResourceManager.colorlessTokenFrame),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(ResourceManager.gainHybridBlend),
                            ResourceManager.newFrame(colorFrames.get(1)))
                    );
                //Check for Hybrid + Return colorless banner blend
                if (hybrid) {
                    return colorlessTokenBanner;
                }
                //Check dual color land + Return colorless banner blend
                if (land && landColor.size() == 2) {
                    return getBlendedFrame(
                        baseFrame,
                        ResourceManager.newFrame(hasText ? ResourceManager.gainColorTokenBlendText : ResourceManager.gainColorTokenBlend),
                        ResourceManager.newFrame(colorlessTokenBanner)
                    );
                }
                //Create Gold Banner blend
                BufferedImage goldTokenBanner = hasText ?
                    getTokenBannerFrameText(
                        ResourceManager.newFrame(ResourceManager.multiTokenFrameText),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(ResourceManager.gainHybridBlend),
                            ResourceManager.newFrame(colorFrames.get(1))
                        )
                    ) :
                    getTokenBannerFrame(
                        ResourceManager.newFrame(ResourceManager.multiTokenFrame),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(ResourceManager.gainHybridBlend),
                            ResourceManager.newFrame(colorFrames.get(1))
                        )
                    );
                //Color piping for Dual-Color
                if (artifact || land) {
                    return getBlendedFrame(
                        baseFrame,
                        ResourceManager.newFrame(hasText ? ResourceManager.gainColorTokenBlendText : ResourceManager.gainColorTokenBlend),
                        ResourceManager.newFrame(goldTokenBanner)
                    );
                } else {
                    return hasText ?
                        getBlendedFrame(
                            ResourceManager.newFrame(ResourceManager.multiTokenFrameText),
                            ResourceManager.newFrame(ResourceManager.gainColorTokenBlendText),
                            ResourceManager.newFrame(goldTokenBanner)
                        ) :
                        getBlendedFrame(
                            ResourceManager.newFrame(ResourceManager.multiTokenFrame),
                            ResourceManager.newFrame(ResourceManager.gainColorTokenBlend),
                            ResourceManager.newFrame(goldTokenBanner)
                        );
                }
            }
        }
        //Mono
        for (MagicColor color : MagicColor.values()) {
            if (cardDef.hasColor(color) || landColor.contains(color)) {
                if (artifact || land) {
                    return hasText ? getTokenBlendFrameText(baseFrame, color) : getTokenBlendFrame(baseFrame, color);
                } else {
                    return hasText ? getTokenFrameText(color) : getTokenFrame(color);
                }
            }
        }
        //Colorless
        return baseFrame;
    }

    private static BufferedImage getBannerFrame(BufferedImage frame, BufferedImage banner) {
        return getBlendedFrame(frame,
            ResourceManager.newFrame(ResourceManager.gainHybridBanner),
            banner);
    }

    private static BufferedImage getPlaneswalkerBannerFrame(BufferedImage frame, BufferedImage banner) {
        return getBlendedFrame(frame,
            ResourceManager.newFrame(ResourceManager.gainPlaneswalkerHybridBanner),
            banner);
    }

    private static BufferedImage getPlaneswalker4BannerFrame(BufferedImage frame, BufferedImage banner) {
        return getBlendedFrame(frame,
            ResourceManager.newFrame(ResourceManager.gainPlaneswalker4HybridBanner),
            banner);
    }

    private static BufferedImage getPlaneswalkerTransformBannerFrame(BufferedImage frame, BufferedImage banner) {
        return getBlendedFrame(frame,
            ResourceManager.newFrame(ResourceManager.gainPlaneswalkerTransformHybridBanner),
            banner);
    }

    private static BufferedImage getBaseTokenFrame(Collection<MagicType> types, boolean hasText) {
        if (types.contains(MagicType.Artifact)) {
            return ResourceManager.newFrame(hasText ? ResourceManager.artifactTokenFrameText : ResourceManager.artifactTokenFrame);
        }
        return ResourceManager.newFrame(hasText ? ResourceManager.colorlessTokenFrameText : ResourceManager.colorlessTokenFrame);
    }

    static BufferedImage getBlendedFrame(BufferedImage bottomFrame, BufferedImage blend, BufferedImage colorFrame) {
        //create overlay
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OUT);
        Graphics2D graphics2D = blend.createGraphics();
        graphics2D.setComposite(ac);
        graphics2D.drawImage(colorFrame, null, 0, 0);
        graphics2D.dispose();

        //draw overlay on top of bottomFrame
        Graphics2D graphics2D1 = bottomFrame.createGraphics();
        graphics2D1.drawImage(blend, null, 0, 0);
        graphics2D1.dispose();
        return ResourceManager.newFrame(bottomFrame);
    }

    private static BufferedImage getColorBlendFrame(BufferedImage frame, MagicColor color) {
        return getBlendedFrame(frame, ResourceManager.newFrame(ResourceManager.gainColorBlend), getFrame(color));
    }

    private static BufferedImage getPlaneswalkerBlendFrame(BufferedImage frame, MagicColor color) {
        return getBlendedFrame(frame, ResourceManager.newFrame(ResourceManager.gainPlaneswalkerColorBlend), getPlaneswalkerFrame(color));
    }

    private static BufferedImage getPlaneswalkerTransformBlendFrame(BufferedImage frame, MagicColor color) {
        return getBlendedFrame(frame, ResourceManager.newFrame(ResourceManager.gainPlaneswalkerColorBlend), getPlaneswalkerTransformFrame(color));
    }

    private static BufferedImage getPlaneswalkerHiddenBlendFrame(BufferedImage frame, MagicColor color) {
        return getBlendedFrame(frame, ResourceManager.newFrame(ResourceManager.gainPlaneswalkerColorBlend), getPlaneswalkerHiddenFrame(color));
    }

    private static BufferedImage getPlaneswalker4BlendFrame(BufferedImage frame, MagicColor color) {
        return getBlendedFrame(frame, ResourceManager.newFrame(ResourceManager.gainPlaneswalker4ColorBlend), getPlaneswalkerFrame(color));
    }

    private static BufferedImage getTransformBlendFrame(BufferedImage frame, MagicColor color) {
        return getBlendedFrame(frame, ResourceManager.newFrame(ResourceManager.gainTransformColorBlend), getTransformFrame(color));
    }

    private static BufferedImage getHiddenBlendFrame(BufferedImage frame, MagicColor color) {
        return getBlendedFrame(frame, ResourceManager.newFrame(ResourceManager.gainColorBlend), getHiddenFrame(color));
    }

    // only with color pairs for Hybrid cards and the
    // colored piping for 'dual color' multicolor or land cards
    static List<MagicColor> getColorPairOrder(IRenderableCard cardDef) {
        List<MagicColor> colors = new ArrayList<>();
        //Get colors
        for (MagicColor color : MagicColor.values()) {
            if (cardDef.hasColor(color)) {
                colors.add(color);
            }
        }
        return getColorPairOrder(colors);
    }

    static List<MagicColor> getColorPairOrder(List<MagicColor> colors) {
        assert colors.size() == 2 : "Size of colors should be 2 but it is " + colors.size();
        List<MagicColor> orderedColors = new ArrayList<>(colors);

        // distance from first color to second color in WUBRG order should be less than 2
        // if it is greater than 2, swapping the pair reduces the distance to less than 2

        if (MagicColor.distance(orderedColors.get(0), orderedColors.get(1)) > 2) {
            Collections.swap(orderedColors, 0, 1);
        }
        return orderedColors;
    }

    private static BufferedImage getFrame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(ResourceManager.whiteFrame);
            case Blue:
                return ResourceManager.newFrame(ResourceManager.blueFrame);
            case Black:
                return ResourceManager.newFrame(ResourceManager.blackFrame);
            case Red:
                return ResourceManager.newFrame(ResourceManager.redFrame);
            case Green:
                return ResourceManager.newFrame(ResourceManager.greenFrame);
        }
        return ResourceManager.newFrame(ResourceManager.colorlessFrame);
    }

    private static BufferedImage getNyxFrame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(ResourceManager.whiteNyx);
            case Blue:
                return ResourceManager.newFrame(ResourceManager.blueNyx);
            case Black:
                return ResourceManager.newFrame(ResourceManager.blackNyx);
            case Red:
                return ResourceManager.newFrame(ResourceManager.redNyx);
            case Green:
                return ResourceManager.newFrame(ResourceManager.greenNyx);
        }
        return ResourceManager.newFrame(ResourceManager.colorlessNyx);
    }

    static List<MagicColor> getLandColors(IRenderableCard cardDef) {
        Collection<MagicManaActivation> landActivations = cardDef.getManaActivations();
        List<MagicColor> landColor = new ArrayList<>();
        //Check mana activations
        if (!landActivations.isEmpty()) {
            for (MagicManaActivation activation : landActivations) {
                landColor.addAll(activation.getManaTypes().stream().filter(manaType -> manaType != MagicManaType.Colorless).map(MagicManaType::getColor).collect(Collectors.toList()));
            }
        }
        //Check basic land types
        MagicSubType.ALL_BASIC_LANDS.stream().filter(cardDef::hasSubType).forEach(aSubType -> {
            for (MagicColor color : MagicColor.values()) {
                if (color.getLandSubType() == aSubType) {
                    landColor.add(color);
                }
            }
        });
        //Check oracle for up to two basic land types
        String oracle = cardDef.getText();
        Collection<MagicColor> basicLandCount = EnumSet.noneOf(MagicColor.class);
        if (oracle.toLowerCase().contains("search")) {
            MagicSubType.ALL_BASIC_LANDS.stream().filter(aSubType -> oracle.toLowerCase().contains(aSubType.toString().toLowerCase())).forEach(aSubType -> {
                for (MagicColor color : MagicColor.values()) {
                    if (color.getLandSubType() == aSubType) {
                        basicLandCount.add(color);
                    }
                }
            });
        }
        if (!basicLandCount.isEmpty() && basicLandCount.size() <= 2) {
            landColor.addAll(basicLandCount);
        }
        //Check for duplicate entries and only return those duplicates
        List<MagicColor> toReturn = new ArrayList<>();
        Set<MagicColor> set1 = EnumSet.noneOf(MagicColor.class);
        toReturn.addAll(landColor.stream().filter(color -> !set1.add(color)).collect(Collectors.toList()));
        return toReturn.isEmpty() ? landColor : toReturn;
    }

    private static BufferedImage getLandFrame(MagicColor color) {
        BufferedImage baseFrame = ResourceManager.newFrame(ResourceManager.landFrame);
        return getColorBlendFrame(baseFrame, color);
    }

    private static BufferedImage getLandNyxFrame(MagicColor color) {
        BufferedImage baseFrame = ResourceManager.newFrame(ResourceManager.landNyx);
        return getColorBlendFrame(baseFrame, color);
    }

    private static BufferedImage getHiddenFrame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(ResourceManager.whiteHidden);
            case Blue:
                return ResourceManager.newFrame(ResourceManager.blueHidden);
            case Black:
                return ResourceManager.newFrame(ResourceManager.blackHidden);
            case Red:
                return ResourceManager.newFrame(ResourceManager.redHidden);
            case Green:
                return ResourceManager.newFrame(ResourceManager.greenHidden);
        }
        return ResourceManager.newFrame(ResourceManager.colorlessHidden);
    }

    private static BufferedImage getTransformFrame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(ResourceManager.whiteTransform);
            case Blue:
                return ResourceManager.newFrame(ResourceManager.blueTransform);
            case Black:
                return ResourceManager.newFrame(ResourceManager.blackTransform);
            case Red:
                return ResourceManager.newFrame(ResourceManager.redTransform);
            case Green:
                return ResourceManager.newFrame(ResourceManager.greenTransform);
        }
        return ResourceManager.newFrame(ResourceManager.colorlessTransform);
    }

    private static BufferedImage getLandHiddenFrame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(ResourceManager.whiteLandHidden);
            case Blue:
                return ResourceManager.newFrame(ResourceManager.blueLandHidden);
            case Black:
                return ResourceManager.newFrame(ResourceManager.blackLandHidden);
            case Red:
                return ResourceManager.newFrame(ResourceManager.redLandHidden);
            case Green:
                return ResourceManager.newFrame(ResourceManager.greenLandHidden);
        }
        return ResourceManager.newFrame(ResourceManager.colorlessLandHidden);
    }

    private static BufferedImage getLandTransformFrame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(ResourceManager.whiteLandTransform);
            case Blue:
                return ResourceManager.newFrame(ResourceManager.blueLandTransform);
            case Black:
                return ResourceManager.newFrame(ResourceManager.blackLandTransform);
            case Red:
                return ResourceManager.newFrame(ResourceManager.redLandTransform);
            case Green:
                return ResourceManager.newFrame(ResourceManager.greenLandTransform);
        }
        return ResourceManager.newFrame(ResourceManager.colorlessLandTransform);
    }

    private static BufferedImage getTokenBannerFrame(BufferedImage frame, BufferedImage banner) {
        return getBlendedFrame(frame,
            ResourceManager.newFrame(ResourceManager.gainTokenBanner),
            banner);
    }

    private static BufferedImage getTokenBannerFrameText(BufferedImage frame, BufferedImage banner) {
        return getBlendedFrame(frame,
            ResourceManager.newFrame(ResourceManager.gainTokenBannerText),
            banner);
    }

    private static BufferedImage getTokenBlendFrame(BufferedImage frame, MagicColor color) {
        return getBlendedFrame(frame,
            ResourceManager.newFrame(ResourceManager.gainColorTokenBlend),
            getTokenFrame(color));
    }

    private static BufferedImage getTokenBlendFrameText(BufferedImage frame, MagicColor color) {
        return getBlendedFrame(frame,
            ResourceManager.newFrame(ResourceManager.gainColorTokenBlendText),
            getTokenFrameText(color));
    }

    private static BufferedImage getTokenFrame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(ResourceManager.whiteTokenFrame);
            case Blue:
                return ResourceManager.newFrame(ResourceManager.blueTokenFrame);
            case Black:
                return ResourceManager.newFrame(ResourceManager.blackTokenFrame);
            case Red:
                return ResourceManager.newFrame(ResourceManager.redTokenFrame);
            case Green:
                return ResourceManager.newFrame(ResourceManager.greenTokenFrame);
        }
        return ResourceManager.newFrame(ResourceManager.colorlessTokenFrame);
    }

    private static BufferedImage getPlaneswalkerFrame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(ResourceManager.whitePlaneswalkerFrame);
            case Blue:
                return ResourceManager.newFrame(ResourceManager.bluePlaneswalkerFrame);
            case Black:
                return ResourceManager.newFrame(ResourceManager.blackPlaneswalkerFrame);
            case Red:
                return ResourceManager.newFrame(ResourceManager.redPlaneswalkerFrame);
            case Green:
                return ResourceManager.newFrame(ResourceManager.greenPlaneswalkerFrame);
        }
        return ResourceManager.newFrame(ResourceManager.colorlessPlaneswalkerFrame);
    }

    private static BufferedImage getPlaneswalkerTransformFrame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(ResourceManager.whitePlaneswalkerTransform);
            case Blue:
                return ResourceManager.newFrame(ResourceManager.bluePlaneswalkerTransform);
            case Black:
                return ResourceManager.newFrame(ResourceManager.blackPlaneswalkerTransform);
            case Red:
                return ResourceManager.newFrame(ResourceManager.redPlaneswalkerTransform);
            case Green:
                return ResourceManager.newFrame(ResourceManager.greenPlaneswalkerTransform);
        }
        return ResourceManager.newFrame(ResourceManager.colorlessPlaneswalkerFrame);
    }

    private static BufferedImage getPlaneswalkerHiddenFrame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(ResourceManager.whitePlaneswalkerHidden);
            case Blue:
                return ResourceManager.newFrame(ResourceManager.bluePlaneswalkerHidden);
            case Black:
                return ResourceManager.newFrame(ResourceManager.blackPlaneswalkerHidden);
            case Red:
                return ResourceManager.newFrame(ResourceManager.redPlaneswalkerHidden);
            case Green:
                return ResourceManager.newFrame(ResourceManager.greenPlaneswalkerHidden);
        }
        return ResourceManager.newFrame(ResourceManager.colorlessPlaneswalkerFrame);
    }

    private static BufferedImage getPlaneswalker4Frame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(ResourceManager.whitePlaneswalker4);
            case Blue:
                return ResourceManager.newFrame(ResourceManager.bluePlaneswalker4);
            case Black:
                return ResourceManager.newFrame(ResourceManager.blackPlaneswalker4);
            case Red:
                return ResourceManager.newFrame(ResourceManager.redPlaneswalker4);
            case Green:
                return ResourceManager.newFrame(ResourceManager.greenPlaneswalker4);
        }
        return ResourceManager.newFrame(ResourceManager.colorlessPlaneswalker4);
    }

    private static BufferedImage getTokenFrameText(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(ResourceManager.whiteTokenFrameText);
            case Blue:
                return ResourceManager.newFrame(ResourceManager.blueTokenFrameText);
            case Black:
                return ResourceManager.newFrame(ResourceManager.blackTokenFrameText);
            case Red:
                return ResourceManager.newFrame(ResourceManager.redTokenFrameText);
            case Green:
                return ResourceManager.newFrame(ResourceManager.greenTokenFrameText);
        }
        return ResourceManager.newFrame(ResourceManager.colorlessTokenFrameText);
    }

    static BufferedImage getPlaneswalkerFrameType(IRenderableCard cardDef) {
        boolean land = cardDef.hasType(MagicType.Land);
        boolean artifact = cardDef.hasType(MagicType.Artifact);
        List<MagicColor> landColor = new ArrayList<>();
        if (OracleText.getPlaneswalkerAbilityCount(cardDef) == 3) {
            BufferedImage baseFrame = ResourceManager.newFrame(ResourceManager.colorlessPlaneswalkerFrame);
            if (land) {
                // No frame for land planeswalkers
                //Land Colors
                landColor = getLandColors(cardDef);
            } else if (artifact) {
                baseFrame = ResourceManager.newFrame(ResourceManager.artifactPlaneswalkerFrame);
            }
            //Multi
            if (cardDef.isMulti() || landColor.size() > 1) {
                if (cardDef.getNumColors() > 2 || land && landColor.size() > 2) {
                    return artifact || land ?
                        getBlendedFrame(
                            baseFrame,
                            ResourceManager.newFrame(ResourceManager.gainPlaneswalkerColorBlend),
                            ResourceManager.newFrame(ResourceManager.multiPlaneswalkerFrame)
                        ) :
                        ResourceManager.newFrame(ResourceManager.multiPlaneswalkerFrame);
                } else {
                    //Hybrid
                    List<BufferedImage> colorFrames = new ArrayList<>(2);
                    colorFrames.addAll(getColorPairOrder(cardDef).stream().map(Frame::getPlaneswalkerFrame).collect(Collectors.toList()));
                    //A colorless Banner with color piping
                    BufferedImage colorlessBanner = getPlaneswalkerBannerFrame(
                        ResourceManager.newFrame(ResourceManager.colorlessPlaneswalkerFrame),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(ResourceManager.gainHybridBlend),
                            ResourceManager.newFrame(colorFrames.get(1)))
                    );
                    //Check for Hybrid + Return colorless banner blend
                    if (cardDef.isHybrid()) {
                        return colorlessBanner;
                    }
                    //Check dual color land + Return colorless banner blend
                    if (land && landColor.size() == 2) {
                        return getBlendedFrame(
                            baseFrame,
                            ResourceManager.newFrame(ResourceManager.gainPlaneswalkerColorBlend),
                            ResourceManager.newFrame(colorlessBanner)
                        );
                    }
                    //Create Gold Banner blend
                    BufferedImage goldBanner = getPlaneswalkerBannerFrame(
                        ResourceManager.newFrame(ResourceManager.multiPlaneswalkerFrame),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(ResourceManager.gainHybridBlend),
                            ResourceManager.newFrame(colorFrames.get(1))
                        ));
                    //Color piping for Dual-Color
                    return getBlendedFrame(
                        artifact || land ? baseFrame : ResourceManager.newFrame(ResourceManager.multiPlaneswalkerFrame),
                        ResourceManager.newFrame(ResourceManager.gainPlaneswalkerColorBlend),
                        ResourceManager.newFrame(goldBanner)
                    );
                }
            }
            //Mono
            for (MagicColor color : MagicColor.values()) {
                if (cardDef.hasColor(color) || landColor.contains(color)) {
                    return artifact ? getPlaneswalkerBlendFrame(baseFrame, color) : getPlaneswalkerFrame(color);
                }
            }
            return baseFrame;
        } else {
            BufferedImage baseFrame = ResourceManager.newFrame(ResourceManager.colorlessPlaneswalker4);
            if (land) {
                // No frame for land planeswalkers
                //Land Colors
                landColor = getLandColors(cardDef);
            } else if (artifact) {
                baseFrame = ResourceManager.newFrame(ResourceManager.artifactPlaneswalker4);
            }
            //Multi
            if (cardDef.isMulti() || landColor.size() > 1) {
                if (cardDef.getNumColors() > 2 || land && landColor.size() > 2) {
                    return artifact || land ?
                        getBlendedFrame(
                            baseFrame,
                            ResourceManager.newFrame(ResourceManager.gainPlaneswalker4ColorBlend),
                            ResourceManager.newFrame(ResourceManager.multiPlaneswalker4)
                        ) :
                        ResourceManager.newFrame(ResourceManager.multiPlaneswalker4);
                } else {
                    //Hybrid
                    List<BufferedImage> colorFrames = new ArrayList<>(2);
                    colorFrames.addAll(getColorPairOrder(cardDef).stream().map(Frame::getPlaneswalker4Frame).collect(Collectors.toList()));
                    //A colorless Banner with color piping
                    BufferedImage colorlessBanner = getPlaneswalker4BannerFrame(
                        ResourceManager.newFrame(ResourceManager.colorlessPlaneswalker4),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(ResourceManager.gainHybridBlend),
                            ResourceManager.newFrame(colorFrames.get(1)))
                    );
                    //Check for Hybrid + Return colorless banner blend
                    if (cardDef.isHybrid()) {
                        return colorlessBanner;
                    }
                    //Check dual color land + Return colorless banner blend
                    if (land && landColor.size() == 2) {
                        return getBlendedFrame(
                            baseFrame,
                            ResourceManager.newFrame(ResourceManager.gainPlaneswalker4ColorBlend),
                            ResourceManager.newFrame(colorlessBanner)
                        );
                    }
                    //Create Gold Banner blend
                    BufferedImage goldBanner = getPlaneswalker4BannerFrame(
                        ResourceManager.newFrame(ResourceManager.multiPlaneswalker4),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(ResourceManager.gainHybridBlend),
                            ResourceManager.newFrame(colorFrames.get(1))
                        ));
                    //Color piping for Dual-Color
                    return getBlendedFrame(
                        artifact || land ? baseFrame : ResourceManager.newFrame(ResourceManager.multiPlaneswalker4),
                        ResourceManager.newFrame(ResourceManager.gainPlaneswalker4ColorBlend),
                        ResourceManager.newFrame(goldBanner)
                    );
                }
            }
            //Mono
            for (MagicColor color : MagicColor.values()) {
                if (cardDef.hasColor(color) || landColor.contains(color)) {
                    return artifact ? getPlaneswalker4BlendFrame(baseFrame, color) : getPlaneswalker4Frame(color);
                }
            }
            return baseFrame;
        }
    }

    static BufferedImage getTransformFrameType(IRenderableCard cardDef) {
        boolean land = cardDef.hasType(MagicType.Land);
        boolean artifact = cardDef.hasType(MagicType.Artifact);
        boolean transform = !cardDef.isHidden();
        List<MagicColor> landColor = new ArrayList<>();
        BufferedImage baseFrame = ResourceManager.newFrame(transform ? ResourceManager.colorlessTransform : ResourceManager.colorlessHidden);
        if (land) {
            baseFrame = ResourceManager.newFrame(transform ? ResourceManager.colorlessLandTransform : ResourceManager.colorlessLandHidden);
            //Land Colors
            landColor = getLandColors(cardDef);
        } else if (artifact) {
            baseFrame = ResourceManager.newFrame(transform ? ResourceManager.artifactTransform : ResourceManager.artifactHidden);
        }
        //Multi
        if (cardDef.isMulti() || landColor.size() > 1) {
            if (cardDef.getNumColors() > 2 || land && landColor.size() > 2) {
                if (artifact || land) {
                    if (land) {
                        return transform ?
                            getBlendedFrame(
                                baseFrame,
                                ResourceManager.newFrame(ResourceManager.gainTransformColorBlend),
                                ResourceManager.newFrame(ResourceManager.multiLandTransform)
                            ) :
                            getBlendedFrame(
                                baseFrame,
                                ResourceManager.newFrame(ResourceManager.gainColorBlend),
                                ResourceManager.newFrame(ResourceManager.multiLandHidden)
                            );
                    } else {
                        return transform ?
                            getBlendedFrame(
                                baseFrame,
                                ResourceManager.newFrame(ResourceManager.gainTransformColorBlend),
                                ResourceManager.newFrame(ResourceManager.multiTransform)
                            ) :
                            getBlendedFrame(
                                baseFrame,
                                ResourceManager.newFrame(ResourceManager.gainColorBlend),
                                ResourceManager.newFrame(ResourceManager.multiHidden)
                            );
                    }
                } else {
                    return ResourceManager.newFrame(transform ? ResourceManager.multiTransform : ResourceManager.multiHidden);
                }
            } else {
                //Hybrid
                List<BufferedImage> colorFrames = new ArrayList<>();
                if (land) {
                    if (transform) {
                        colorFrames.addAll(getColorPairOrder(landColor).stream().map(Frame::getLandTransformFrame).collect(Collectors.toList()));
                    } else {
                        colorFrames.addAll(getColorPairOrder(landColor).stream().map(Frame::getLandHiddenFrame).collect(Collectors.toList()));
                    }
                } else {
                    if (transform) {
                        colorFrames.addAll(getColorPairOrder(cardDef).stream().map(Frame::getTransformFrame).collect(Collectors.toList()));
                    } else {
                        colorFrames.addAll(getColorPairOrder(cardDef).stream().map(Frame::getHiddenFrame).collect(Collectors.toList()));
                    }
                }
                //A colorless Banner with color piping
                BufferedImage colorlessHiddenBanner = getBannerFrame(
                    ResourceManager.newFrame(ResourceManager.colorlessHidden),
                    getBlendedFrame(
                        ResourceManager.newFrame(colorFrames.get(0)),
                        ResourceManager.newFrame(ResourceManager.gainHybridBlend),
                        ResourceManager.newFrame(colorFrames.get(1)))
                );
                BufferedImage colorlessTransformBanner = getBannerFrame(
                    ResourceManager.newFrame(ResourceManager.colorlessTransform),
                    getBlendedFrame(
                        ResourceManager.newFrame(colorFrames.get(0)),
                        ResourceManager.newFrame(ResourceManager.gainHybridBlend),
                        ResourceManager.newFrame(colorFrames.get(1)))
                );
                //Check for Hybrid + Return colorless banner blend
                if (cardDef.isHybrid()) {
                    return transform ? colorlessTransformBanner : colorlessHiddenBanner;
                }
                //Check dual color land + Return colorless banner blend
                if (land && landColor.size() == 2) {
                    return transform ? getBlendedFrame(
                        baseFrame,
                        ResourceManager.newFrame(ResourceManager.gainTransformColorBlend),
                        ResourceManager.newFrame(colorlessTransformBanner)
                    ) : getBlendedFrame(
                        baseFrame,
                        ResourceManager.newFrame(ResourceManager.gainColorBlend),
                        ResourceManager.newFrame(colorlessHiddenBanner)
                    );
                }
                //Create Gold Banner blend
                BufferedImage goldHiddenBanner = getBannerFrame(
                    ResourceManager.newFrame(ResourceManager.multiHidden),
                    getBlendedFrame(
                        ResourceManager.newFrame(colorFrames.get(0)),
                        ResourceManager.newFrame(ResourceManager.gainTransformHybridBanner),
                        ResourceManager.newFrame(colorFrames.get(1))
                    ));
                BufferedImage goldTransformBanner = getBannerFrame(
                    ResourceManager.newFrame(ResourceManager.multiTransform),
                    getBlendedFrame(
                        ResourceManager.newFrame(colorFrames.get(0)),
                        ResourceManager.newFrame(ResourceManager.gainTransformHybridBanner),
                        ResourceManager.newFrame(colorFrames.get(1))
                    ));
                //Color piping for Dual-Color
                if (artifact || land) {
                    return transform ?
                        getBlendedFrame(
                            baseFrame,
                            ResourceManager.newFrame(ResourceManager.gainTransformColorBlend),
                            ResourceManager.newFrame(goldTransformBanner)
                        ) :
                        getBlendedFrame(
                            baseFrame,
                            ResourceManager.newFrame(ResourceManager.gainColorBlend),
                            ResourceManager.newFrame(goldHiddenBanner)
                        );
                } else {
                    return transform ?
                        getBlendedFrame(
                            ResourceManager.newFrame(ResourceManager.multiTransform),
                            ResourceManager.newFrame(ResourceManager.gainTransformColorBlend),
                            ResourceManager.newFrame(goldTransformBanner)
                        ) :
                        getBlendedFrame(
                            ResourceManager.newFrame(ResourceManager.multiHidden),
                            ResourceManager.newFrame(ResourceManager.gainColorBlend),
                            ResourceManager.newFrame(goldHiddenBanner)
                        );
                }
            }
        }
        //Mono
        for (MagicColor color : MagicColor.values()) {
            if (cardDef.hasColor(color) || landColor.contains(color)) {
                if (artifact) {
                    return transform ? getTransformBlendFrame(baseFrame, color) : getHiddenBlendFrame(baseFrame, color);
                } else if (land) {
                    return transform ? getLandTransformFrame(color) : getLandHiddenFrame(color);
                } else {
                    return transform ? getTransformFrame(color) : getHiddenFrame(color);
                }
            }
        }
        //Colorless
        return baseFrame;
    }

    static BufferedImage getTransformPlaneswalkerFrameType(IRenderableCard cardDef) {
        boolean land = cardDef.hasType(MagicType.Land);
        boolean artifact = cardDef.hasType(MagicType.Artifact);
        boolean transform = !cardDef.isHidden();
        List<MagicColor> landColor = new ArrayList<>();
        if (OracleText.getPlaneswalkerAbilityCount(cardDef) <= 3) {
            BufferedImage baseFrame = ResourceManager.newFrame(ResourceManager.colorlessPlaneswalkerFrame);
            if (land) {
                // No frame for land planeswalkers
                //Land Colors
                landColor = getLandColors(cardDef);
            } else if (artifact) {
                baseFrame = ResourceManager.newFrame(transform ? ResourceManager.artifactPlaneswalkerTransform : ResourceManager.artifactPlaneswalkerHidden);
            }
            //Multi
            if (cardDef.isMulti() || landColor.size() > 1) {
                if (cardDef.getNumColors() > 2 || land && landColor.size() > 2) {
                    if (transform) {
                        return artifact || land ?
                            getBlendedFrame(
                                baseFrame,
                                ResourceManager.newFrame(ResourceManager.gainPlaneswalkerColorBlend),
                                ResourceManager.newFrame(ResourceManager.multiPlaneswalkerTransform)
                            ) :
                            ResourceManager.newFrame(ResourceManager.multiPlaneswalkerTransform);
                    } else {
                        return artifact || land ?
                            getBlendedFrame(
                                baseFrame,
                                ResourceManager.newFrame(ResourceManager.gainPlaneswalkerColorBlend),
                                ResourceManager.newFrame(ResourceManager.multiPlaneswalkerHidden)
                            ) :
                            ResourceManager.newFrame(ResourceManager.multiPlaneswalkerHidden);
                    }
                } else {
                    //Hybrid
                    List<BufferedImage> colorFrames = new ArrayList<>(2);
                    colorFrames.addAll(getColorPairOrder(cardDef).stream().map(Frame::getPlaneswalkerFrame).collect(Collectors.toList()));
                    //A colorless Banner with color piping
                    BufferedImage colorlessBanner = getPlaneswalkerTransformBannerFrame(
                        ResourceManager.newFrame(ResourceManager.colorlessPlaneswalkerFrame),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(ResourceManager.gainHybridBlend),
                            ResourceManager.newFrame(colorFrames.get(1)))
                    );
                    //Check for Hybrid + Return colorless banner blend
                    if (cardDef.isHybrid()) {
                        return colorlessBanner;
                    }
                    //Check dual color land + Return colorless banner blend
                    if (land && landColor.size() == 2) {
                        return getBlendedFrame(
                            baseFrame,
                            ResourceManager.newFrame(ResourceManager.gainPlaneswalkerColorBlend),
                            ResourceManager.newFrame(colorlessBanner)
                        );
                    }
                    //Create Gold Banner blend
                    BufferedImage goldBanner = getPlaneswalkerBannerFrame(
                        ResourceManager.newFrame(transform ? ResourceManager.multiPlaneswalkerTransform : ResourceManager.multiPlaneswalkerHidden),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(ResourceManager.gainHybridBlend),
                            ResourceManager.newFrame(colorFrames.get(1))
                        ));
                    //Color piping for Dual-Color
                    return getBlendedFrame(
                        transform ? artifact || land ? baseFrame : ResourceManager.newFrame(ResourceManager.multiPlaneswalkerTransform) : artifact || land ? baseFrame : ResourceManager.newFrame(ResourceManager.multiPlaneswalkerHidden),
                        ResourceManager.newFrame(ResourceManager.gainPlaneswalkerColorBlend),
                        ResourceManager.newFrame(goldBanner)
                    );
                }
            }
            //Mono
            for (MagicColor color : MagicColor.values()) {
                if (cardDef.hasColor(color) || landColor.contains(color)) {
                    if (transform) {
                        return artifact ? getPlaneswalkerTransformBlendFrame(baseFrame, color) : getPlaneswalkerTransformFrame(color);
                    } else {
                        return artifact ? getPlaneswalkerHiddenBlendFrame(baseFrame, color) : getPlaneswalkerHiddenFrame(color);
                    }
                }
            }
            return baseFrame;
        } else {
            BufferedImage baseFrame = ResourceManager.newFrame(ResourceManager.colorlessPlaneswalker4);
            if (land) {
                // No frame for land planeswalkers
                //Land Colors
                landColor = getLandColors(cardDef);
            } else if (artifact) {
                baseFrame = ResourceManager.newFrame(ResourceManager.artifactPlaneswalker4);
            }
            //Multi
            if (cardDef.isMulti() || landColor.size() > 1) {
                if (cardDef.getNumColors() > 2 || land && landColor.size() > 2) {
                    return artifact || land ?
                        getBlendedFrame(
                            baseFrame,
                            ResourceManager.newFrame(ResourceManager.gainPlaneswalker4ColorBlend),
                            ResourceManager.newFrame(ResourceManager.multiPlaneswalker4)
                        ) :
                        ResourceManager.newFrame(ResourceManager.multiPlaneswalker4);
                } else {
                    //Hybrid
                    List<BufferedImage> colorFrames = new ArrayList<>(2);
                    colorFrames.addAll(getColorPairOrder(cardDef).stream().map(Frame::getPlaneswalker4Frame).collect(Collectors.toList()));
                    //A colorless Banner with color piping
                    BufferedImage colorlessBanner = getPlaneswalker4BannerFrame(
                        ResourceManager.newFrame(ResourceManager.colorlessPlaneswalker4),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(ResourceManager.gainHybridBlend),
                            ResourceManager.newFrame(colorFrames.get(1)))
                    );
                    //Check for Hybrid + Return colorless banner blend
                    if (cardDef.isHybrid()) {
                        return colorlessBanner;
                    }
                    //Check dual color land + Return colorless banner blend
                    if (land && landColor.size() == 2) {
                        return getBlendedFrame(
                            baseFrame,
                            ResourceManager.newFrame(ResourceManager.gainPlaneswalker4ColorBlend),
                            ResourceManager.newFrame(colorlessBanner)
                        );
                    }
                    //Create Gold Banner blend
                    BufferedImage goldBanner = getPlaneswalker4BannerFrame(
                        ResourceManager.newFrame(ResourceManager.multiPlaneswalker4),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(ResourceManager.gainHybridBlend),
                            ResourceManager.newFrame(colorFrames.get(1))
                        ));
                    //Color piping for Dual-Color
                    return getBlendedFrame(
                        artifact || land ? baseFrame : ResourceManager.newFrame(ResourceManager.multiPlaneswalker4),
                        ResourceManager.newFrame(ResourceManager.gainPlaneswalker4ColorBlend),
                        ResourceManager.newFrame(goldBanner)
                    );
                }
            }
            //Mono
            for (MagicColor color : MagicColor.values()) {
                if (cardDef.hasColor(color) || landColor.contains(color)) {
                    return artifact ? getPlaneswalker4BlendFrame(baseFrame, color) : getPlaneswalker4Frame(color);
                }
            }
            return baseFrame;
        }
    }

    static BufferedImage getColorlessFrameType(IRenderableCard cardDef) {
        if (cardDef.isLand()) {
            return getBasicFrameType(cardDef);
        }
        if (cardDef.isArtifact()) {
            return ResourceManager.newFrame(ResourceManager.artifactFrame);
        }
        return ResourceManager.newFrame(ResourceManager.colorlessFrame);
    }

}
