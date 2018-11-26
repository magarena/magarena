package magic.cardBuilder.renderers;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Locale;
import java.util.stream.Collectors;

import magic.model.MagicColor;
import magic.model.MagicManaType;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.event.MagicManaActivation;
import magic.model.IRenderableCard;
import magic.cardBuilder.ResourceManager;
import magic.cardBuilder.CardResource;

public class Frame {

    static BufferedImage getBasicFrameType(IRenderableCard cardDef) {
        BufferedImage baseFrame = ResourceManager.newFrame(CardResource.colorlessFrame);
        boolean land = cardDef.hasType(MagicType.Land);
        boolean artifact = cardDef.hasType(MagicType.Artifact);
        boolean enchantmentPermanent = cardDef.hasType(MagicType.Enchantment) &&
            (cardDef.hasType(MagicType.Creature) || cardDef.hasType(MagicType.Artifact));
        Set<MagicColor> landColor = new HashSet<>();
        if (land) {
            baseFrame = ResourceManager.newFrame(enchantmentPermanent ? CardResource.landNyx : CardResource.landFrame);
            //Land Colors
            landColor = getLandColors(cardDef);
        } else if (artifact) {
            baseFrame = ResourceManager.newFrame(enchantmentPermanent ? CardResource.artifactNyx : CardResource.artifactFrame);
        }
        //Multi
        if (cardDef.isMulti() || landColor.size() > 1) {
            if (cardDef.getNumColors() > 2 || land && landColor.size() > 2) {
                return artifact || land ? getBlendedFrame(
                    baseFrame,
                    ResourceManager.newFrame(CardResource.gainColorBlend),
                    ResourceManager.newFrame(CardResource.multiFrame)) : ResourceManager.newFrame(enchantmentPermanent ? CardResource.multiNyx : CardResource.multiFrame);
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
                    ResourceManager.newFrame(CardResource.colorlessFrame),
                    getBlendedFrame(
                        ResourceManager.newFrame(colorFrames.get(0)),
                        ResourceManager.newFrame(CardResource.gainHybridBlend),
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
                        ResourceManager.newFrame(CardResource.gainColorBlend),
                        ResourceManager.newFrame(colorlessBanner)
                    );
                }
                //Create Gold Banner blend
                BufferedImage goldBanner = getBannerFrame(
                    ResourceManager.newFrame(CardResource.multiFrame),
                    getBlendedFrame(
                        ResourceManager.newFrame(colorFrames.get(0)),
                        ResourceManager.newFrame(CardResource.gainHybridBlend),
                        ResourceManager.newFrame(colorFrames.get(1))
                    ));
                //Color piping for Dual-Color
                return getBlendedFrame(
                    artifact || land ? baseFrame : ResourceManager.newFrame(enchantmentPermanent ? CardResource.multiNyx : CardResource.multiFrame),
                    ResourceManager.newFrame(CardResource.gainColorBlend),
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
        boolean enchantmentPermanent = cardDef.hasType(MagicType.Enchantment) && (cardDef.hasType(MagicType.Creature) || cardDef.hasType(MagicType.Artifact));
        Set<MagicColor> landColor = new HashSet<>();
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
                    if (enchantmentPermanent) {
                        return hasText ?
                            getBlendedFrame(
                                baseFrame,
                                ResourceManager.newFrame(CardResource.gainColorTokenBlendText),
                                ResourceManager.newFrame(CardResource.multiTokenNyxText)
                            ) :
                            getBlendedFrame(
                                baseFrame,
                                ResourceManager.newFrame(CardResource.gainColorTokenBlend),
                                ResourceManager.newFrame(CardResource.multiTokenNyx)
                            );
                    } else {
                        return hasText ?
                            getBlendedFrame(
                                baseFrame,
                                ResourceManager.newFrame(CardResource.gainColorTokenBlendText),
                                ResourceManager.newFrame(CardResource.multiTokenFrameText)
                            ) :
                            getBlendedFrame(
                                baseFrame,
                                ResourceManager.newFrame(CardResource.gainColorTokenBlend),
                                ResourceManager.newFrame(CardResource.multiTokenFrame)
                            );
                    }
                } else {
                    if (enchantmentPermanent) {
                        return ResourceManager.newFrame(hasText ? CardResource.multiTokenNyxText : CardResource.multiTokenNyx);
                    } else {
                        return ResourceManager.newFrame(hasText ? CardResource.multiTokenFrameText : CardResource.multiTokenFrame);
                    }
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
                        ResourceManager.newFrame(CardResource.colorlessTokenFrameText),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(CardResource.gainHybridBlend),
                            ResourceManager.newFrame(colorFrames.get(1)))
                    ) :
                    getTokenBannerFrame(
                        ResourceManager.newFrame(CardResource.colorlessTokenFrame),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(CardResource.gainHybridBlend),
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
                        ResourceManager.newFrame(hasText ? CardResource.gainColorTokenBlendText : CardResource.gainColorTokenBlend),
                        ResourceManager.newFrame(colorlessTokenBanner)
                    );
                }
                //Create Gold Banner blend
                BufferedImage goldTokenBanner = hasText ?
                    getTokenBannerFrameText(
                        ResourceManager.newFrame(CardResource.multiTokenFrameText),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(CardResource.gainHybridBlend),
                            ResourceManager.newFrame(colorFrames.get(1))
                        )
                    ) :
                    getTokenBannerFrame(
                        ResourceManager.newFrame(CardResource.multiTokenFrame),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(CardResource.gainHybridBlend),
                            ResourceManager.newFrame(colorFrames.get(1))
                        )
                    );
                //Color piping for Dual-Color
                if (artifact || land) {
                    return getBlendedFrame(
                        baseFrame,
                        ResourceManager.newFrame(hasText ? CardResource.gainColorTokenBlendText : CardResource.gainColorTokenBlend),
                        ResourceManager.newFrame(goldTokenBanner)
                    );
                } else {
                    return hasText ?
                        getBlendedFrame(
                            ResourceManager.newFrame(CardResource.multiTokenFrameText),
                            ResourceManager.newFrame(CardResource.gainColorTokenBlendText),
                            ResourceManager.newFrame(goldTokenBanner)
                        ) :
                        getBlendedFrame(
                            ResourceManager.newFrame(CardResource.multiTokenFrame),
                            ResourceManager.newFrame(CardResource.gainColorTokenBlend),
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
            ResourceManager.newFrame(CardResource.gainHybridBanner),
            banner);
    }

    private static BufferedImage getPlaneswalkerBannerFrame(BufferedImage frame, BufferedImage banner) {
        return getBlendedFrame(frame,
            ResourceManager.newFrame(CardResource.gainPlaneswalkerHybridBanner),
            banner);
    }

    private static BufferedImage getPlaneswalker4BannerFrame(BufferedImage frame, BufferedImage banner) {
        return getBlendedFrame(frame,
            ResourceManager.newFrame(CardResource.gainPlaneswalker4HybridBanner),
            banner);
    }

    private static BufferedImage getPlaneswalkerTransformBannerFrame(BufferedImage frame, BufferedImage banner) {
        return getBlendedFrame(frame,
            ResourceManager.newFrame(CardResource.gainPlaneswalkerTransformHybridBanner),
            banner);
    }

    private static BufferedImage getBaseTokenFrame(Collection<MagicType> types, boolean hasText) {
        if (types.contains(MagicType.Artifact)) {
            if (types.contains(MagicType.Enchantment)) {
                return ResourceManager.newFrame(hasText ? CardResource.artifactTokenNyxText : CardResource.artifactTokenNyx);
            } else {
                return ResourceManager.newFrame(hasText ? CardResource.artifactTokenFrameText : CardResource.artifactTokenFrame);
            }
        } else {
            return ResourceManager.newFrame(hasText ? CardResource.colorlessTokenFrameText : CardResource.colorlessTokenFrame);
        }
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
        return getBlendedFrame(frame, ResourceManager.newFrame(CardResource.gainColorBlend), getFrame(color));
    }

    private static BufferedImage getPlaneswalkerBlendFrame(BufferedImage frame, MagicColor color) {
        return getBlendedFrame(frame, ResourceManager.newFrame(CardResource.gainPlaneswalkerColorBlend), getPlaneswalkerFrame(color));
    }

    private static BufferedImage getPlaneswalkerTransformBlendFrame(BufferedImage frame, MagicColor color) {
        return getBlendedFrame(frame, ResourceManager.newFrame(CardResource.gainPlaneswalkerColorBlend), getPlaneswalkerTransformFrame(color));
    }

    private static BufferedImage getPlaneswalkerHiddenBlendFrame(BufferedImage frame, MagicColor color) {
        return getBlendedFrame(frame, ResourceManager.newFrame(CardResource.gainPlaneswalkerColorBlend), getPlaneswalkerHiddenFrame(color));
    }

    private static BufferedImage getPlaneswalker4BlendFrame(BufferedImage frame, MagicColor color) {
        return getBlendedFrame(frame, ResourceManager.newFrame(CardResource.gainPlaneswalker4ColorBlend), getPlaneswalkerFrame(color));
    }

    private static BufferedImage getTransformBlendFrame(BufferedImage frame, MagicColor color) {
        return getBlendedFrame(frame, ResourceManager.newFrame(CardResource.gainTransformColorBlend), getTransformFrame(color));
    }

    private static BufferedImage getHiddenBlendFrame(BufferedImage frame, MagicColor color) {
        return getBlendedFrame(frame, ResourceManager.newFrame(CardResource.gainColorBlend), getHiddenFrame(color));
    }

    // only with color pairs for Hybrid cards and the
    // colored piping for 'dual color' multicolor or land cards
    static List<MagicColor> getColorPairOrder(IRenderableCard cardDef) {
        Set<MagicColor> colors = new HashSet<>();
        //Get colors
        for (MagicColor color : MagicColor.values()) {
            if (cardDef.hasColor(color)) {
                colors.add(color);
            }
        }
        return getColorPairOrder(colors);
    }

    static List<MagicColor> getColorPairOrder(Set<MagicColor> colors) {
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
                return ResourceManager.newFrame(CardResource.whiteFrame);
            case Blue:
                return ResourceManager.newFrame(CardResource.blueFrame);
            case Black:
                return ResourceManager.newFrame(CardResource.blackFrame);
            case Red:
                return ResourceManager.newFrame(CardResource.redFrame);
            case Green:
                return ResourceManager.newFrame(CardResource.greenFrame);
        }
        return ResourceManager.newFrame(CardResource.colorlessFrame);
    }

    private static BufferedImage getNyxFrame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(CardResource.whiteNyx);
            case Blue:
                return ResourceManager.newFrame(CardResource.blueNyx);
            case Black:
                return ResourceManager.newFrame(CardResource.blackNyx);
            case Red:
                return ResourceManager.newFrame(CardResource.redNyx);
            case Green:
                return ResourceManager.newFrame(CardResource.greenNyx);
        }
        return ResourceManager.newFrame(CardResource.colorlessNyx);
    }

    static Set<MagicColor> getLandColors(IRenderableCard cardDef) {
        Collection<MagicManaActivation> landActivations = cardDef.getManaActivations();
        Set<MagicColor> landColor = new HashSet<>();
        //Add mana activations
        if (!landActivations.isEmpty()) {
            for (MagicManaActivation activation : landActivations) {
                landColor.addAll(activation.getManaTypes().stream().filter(manaType -> manaType != MagicManaType.Colorless).map(MagicManaType::getColor).collect(Collectors.toList()));
            }
        }
        //Add basic land types
        landColor.addAll(getBasicLandColors(cardDef));
        //Check oracle for up to two basic land types if no other mana generation present
        String oracle = cardDef.getText().toLowerCase(Locale.ENGLISH);
        Collection<MagicColor> basicLandCount = EnumSet.noneOf(MagicColor.class);
        if (oracle.contains("search") && landColor.isEmpty()) {
            MagicSubType.ALL_BASIC_LANDS
                .stream()
                .filter(aSubType -> oracle.contains(aSubType.toString().toLowerCase(Locale.ENGLISH)))
                .forEach(aSubType -> {
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
        return landColor;
    }

    static Set<MagicColor> getBasicLandColors(IRenderableCard cardDef) {
        Set<MagicColor> basicColor = new HashSet<>();
        MagicSubType.ALL_BASIC_LANDS.stream().filter(cardDef::hasSubType).forEach(aSubType -> {
            for (MagicColor color : MagicColor.values()) {
                if (color.getLandSubType() == aSubType) {
                    basicColor.add(color);
                }
            }
        });
        return basicColor;
    }

    private static BufferedImage getLandFrame(MagicColor color) {
        BufferedImage baseFrame = ResourceManager.newFrame(CardResource.landFrame);
        return getColorBlendFrame(baseFrame, color);
    }

    private static BufferedImage getLandNyxFrame(MagicColor color) {
        BufferedImage baseFrame = ResourceManager.newFrame(CardResource.landNyx);
        return getColorBlendFrame(baseFrame, color);
    }

    private static BufferedImage getHiddenFrame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(CardResource.whiteHidden);
            case Blue:
                return ResourceManager.newFrame(CardResource.blueHidden);
            case Black:
                return ResourceManager.newFrame(CardResource.blackHidden);
            case Red:
                return ResourceManager.newFrame(CardResource.redHidden);
            case Green:
                return ResourceManager.newFrame(CardResource.greenHidden);
        }
        return ResourceManager.newFrame(CardResource.colorlessHidden);
    }

    private static BufferedImage getTransformFrame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(CardResource.whiteTransform);
            case Blue:
                return ResourceManager.newFrame(CardResource.blueTransform);
            case Black:
                return ResourceManager.newFrame(CardResource.blackTransform);
            case Red:
                return ResourceManager.newFrame(CardResource.redTransform);
            case Green:
                return ResourceManager.newFrame(CardResource.greenTransform);
        }
        return ResourceManager.newFrame(CardResource.colorlessTransform);
    }

    private static BufferedImage getLandHiddenFrame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(CardResource.whiteLandHidden);
            case Blue:
                return ResourceManager.newFrame(CardResource.blueLandHidden);
            case Black:
                return ResourceManager.newFrame(CardResource.blackLandHidden);
            case Red:
                return ResourceManager.newFrame(CardResource.redLandHidden);
            case Green:
                return ResourceManager.newFrame(CardResource.greenLandHidden);
        }
        return ResourceManager.newFrame(CardResource.colorlessLandHidden);
    }

    private static BufferedImage getLandTransformFrame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(CardResource.whiteLandTransform);
            case Blue:
                return ResourceManager.newFrame(CardResource.blueLandTransform);
            case Black:
                return ResourceManager.newFrame(CardResource.blackLandTransform);
            case Red:
                return ResourceManager.newFrame(CardResource.redLandTransform);
            case Green:
                return ResourceManager.newFrame(CardResource.greenLandTransform);
        }
        return ResourceManager.newFrame(CardResource.colorlessLandTransform);
    }

    private static BufferedImage getTokenBannerFrame(BufferedImage frame, BufferedImage banner) {
        return getBlendedFrame(frame,
            ResourceManager.newFrame(CardResource.gainTokenBanner),
            banner);
    }

    private static BufferedImage getTokenBannerFrameText(BufferedImage frame, BufferedImage banner) {
        return getBlendedFrame(frame,
            ResourceManager.newFrame(CardResource.gainTokenBannerText),
            banner);
    }

    private static BufferedImage getTokenBlendFrame(BufferedImage frame, MagicColor color) {
        return getBlendedFrame(frame,
            ResourceManager.newFrame(CardResource.gainColorTokenBlend),
            getTokenFrame(color));
    }

    private static BufferedImage getTokenBlendFrameText(BufferedImage frame, MagicColor color) {
        return getBlendedFrame(frame,
            ResourceManager.newFrame(CardResource.gainColorTokenBlendText),
            getTokenFrameText(color));
    }

    private static BufferedImage getTokenFrame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(CardResource.whiteTokenFrame);
            case Blue:
                return ResourceManager.newFrame(CardResource.blueTokenFrame);
            case Black:
                return ResourceManager.newFrame(CardResource.blackTokenFrame);
            case Red:
                return ResourceManager.newFrame(CardResource.redTokenFrame);
            case Green:
                return ResourceManager.newFrame(CardResource.greenTokenFrame);
        }
        return ResourceManager.newFrame(CardResource.colorlessTokenFrame);
    }

    private static BufferedImage getPlaneswalkerFrame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(CardResource.whitePlaneswalkerFrame);
            case Blue:
                return ResourceManager.newFrame(CardResource.bluePlaneswalkerFrame);
            case Black:
                return ResourceManager.newFrame(CardResource.blackPlaneswalkerFrame);
            case Red:
                return ResourceManager.newFrame(CardResource.redPlaneswalkerFrame);
            case Green:
                return ResourceManager.newFrame(CardResource.greenPlaneswalkerFrame);
        }
        return ResourceManager.newFrame(CardResource.colorlessPlaneswalkerFrame);
    }

    private static BufferedImage getPlaneswalkerTransformFrame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(CardResource.whitePlaneswalkerTransform);
            case Blue:
                return ResourceManager.newFrame(CardResource.bluePlaneswalkerTransform);
            case Black:
                return ResourceManager.newFrame(CardResource.blackPlaneswalkerTransform);
            case Red:
                return ResourceManager.newFrame(CardResource.redPlaneswalkerTransform);
            case Green:
                return ResourceManager.newFrame(CardResource.greenPlaneswalkerTransform);
        }
        return ResourceManager.newFrame(CardResource.colorlessPlaneswalkerFrame);
    }

    private static BufferedImage getPlaneswalkerHiddenFrame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(CardResource.whitePlaneswalkerHidden);
            case Blue:
                return ResourceManager.newFrame(CardResource.bluePlaneswalkerHidden);
            case Black:
                return ResourceManager.newFrame(CardResource.blackPlaneswalkerHidden);
            case Red:
                return ResourceManager.newFrame(CardResource.redPlaneswalkerHidden);
            case Green:
                return ResourceManager.newFrame(CardResource.greenPlaneswalkerHidden);
        }
        return ResourceManager.newFrame(CardResource.colorlessPlaneswalkerFrame);
    }

    private static BufferedImage getPlaneswalker4Frame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(CardResource.whitePlaneswalker4);
            case Blue:
                return ResourceManager.newFrame(CardResource.bluePlaneswalker4);
            case Black:
                return ResourceManager.newFrame(CardResource.blackPlaneswalker4);
            case Red:
                return ResourceManager.newFrame(CardResource.redPlaneswalker4);
            case Green:
                return ResourceManager.newFrame(CardResource.greenPlaneswalker4);
        }
        return ResourceManager.newFrame(CardResource.colorlessPlaneswalker4);
    }

    private static BufferedImage getTokenFrameText(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(CardResource.whiteTokenFrameText);
            case Blue:
                return ResourceManager.newFrame(CardResource.blueTokenFrameText);
            case Black:
                return ResourceManager.newFrame(CardResource.blackTokenFrameText);
            case Red:
                return ResourceManager.newFrame(CardResource.redTokenFrameText);
            case Green:
                return ResourceManager.newFrame(CardResource.greenTokenFrameText);
        }
        return ResourceManager.newFrame(CardResource.colorlessTokenFrameText);
    }

    private static BufferedImage getNyxTokenFrame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(CardResource.whiteTokenNyx);
            case Blue:
                return ResourceManager.newFrame(CardResource.blueTokenNyx);
            case Black:
                return ResourceManager.newFrame(CardResource.blackTokenNyx);
            case Red:
                return ResourceManager.newFrame(CardResource.redTokenNyx);
            case Green:
                return ResourceManager.newFrame(CardResource.greenTokenNyx);
        }
        return ResourceManager.newFrame(CardResource.colorlessTokenFrame);
    }

    private static BufferedImage getNyxTokenFrameText(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(CardResource.whiteTokenNyxText);
            case Blue:
                return ResourceManager.newFrame(CardResource.blueTokenNyxText);
            case Black:
                return ResourceManager.newFrame(CardResource.blackTokenNyxText);
            case Red:
                return ResourceManager.newFrame(CardResource.redTokenNyxText);
            case Green:
                return ResourceManager.newFrame(CardResource.greenTokenNyxText);
        }
        return ResourceManager.newFrame(CardResource.colorlessTokenFrameText);
    }

    static BufferedImage getPlaneswalkerFrameType(IRenderableCard cardDef) {
        boolean land = cardDef.hasType(MagicType.Land);
        boolean artifact = cardDef.hasType(MagicType.Artifact);
        Set<MagicColor> landColor = new HashSet<>();
        if (OracleText.getPlaneswalkerAbilityCount(cardDef) == 3) {
            BufferedImage baseFrame = ResourceManager.newFrame(CardResource.colorlessPlaneswalkerFrame);
            if (land) {
                // No frame for land planeswalkers
                //Land Colors
                landColor = getLandColors(cardDef);
            } else if (artifact) {
                baseFrame = ResourceManager.newFrame(CardResource.artifactPlaneswalkerFrame);
            }
            //Multi
            if (cardDef.isMulti() || landColor.size() > 1) {
                if (cardDef.getNumColors() > 2 || land && landColor.size() > 2) {
                    return artifact || land ?
                        getBlendedFrame(
                            baseFrame,
                            ResourceManager.newFrame(CardResource.gainPlaneswalkerColorBlend),
                            ResourceManager.newFrame(CardResource.multiPlaneswalkerFrame)
                        ) :
                        ResourceManager.newFrame(CardResource.multiPlaneswalkerFrame);
                } else {
                    //Hybrid
                    List<BufferedImage> colorFrames = new ArrayList<>(2);
                    colorFrames.addAll(getColorPairOrder(cardDef).stream().map(Frame::getPlaneswalkerFrame).collect(Collectors.toList()));
                    //A colorless Banner with color piping
                    BufferedImage colorlessBanner = getPlaneswalkerBannerFrame(
                        ResourceManager.newFrame(CardResource.colorlessPlaneswalkerFrame),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(CardResource.gainHybridBlend),
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
                            ResourceManager.newFrame(CardResource.gainPlaneswalkerColorBlend),
                            ResourceManager.newFrame(colorlessBanner)
                        );
                    }
                    //Create Gold Banner blend
                    BufferedImage goldBanner = getPlaneswalkerBannerFrame(
                        ResourceManager.newFrame(CardResource.multiPlaneswalkerFrame),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(CardResource.gainHybridBlend),
                            ResourceManager.newFrame(colorFrames.get(1))
                        ));
                    //Color piping for Dual-Color
                    return getBlendedFrame(
                        artifact || land ? baseFrame : ResourceManager.newFrame(CardResource.multiPlaneswalkerFrame),
                        ResourceManager.newFrame(CardResource.gainPlaneswalkerColorBlend),
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
            BufferedImage baseFrame = ResourceManager.newFrame(CardResource.colorlessPlaneswalker4);
            if (land) {
                // No frame for land planeswalkers
                //Land Colors
                landColor = getLandColors(cardDef);
            } else if (artifact) {
                baseFrame = ResourceManager.newFrame(CardResource.artifactPlaneswalker4);
            }
            //Multi
            if (cardDef.isMulti() || landColor.size() > 1) {
                if (cardDef.getNumColors() > 2 || land && landColor.size() > 2) {
                    return artifact || land ?
                        getBlendedFrame(
                            baseFrame,
                            ResourceManager.newFrame(CardResource.gainPlaneswalker4ColorBlend),
                            ResourceManager.newFrame(CardResource.multiPlaneswalker4)
                        ) :
                        ResourceManager.newFrame(CardResource.multiPlaneswalker4);
                } else {
                    //Hybrid
                    List<BufferedImage> colorFrames = new ArrayList<>(2);
                    colorFrames.addAll(getColorPairOrder(cardDef).stream().map(Frame::getPlaneswalker4Frame).collect(Collectors.toList()));
                    //A colorless Banner with color piping
                    BufferedImage colorlessBanner = getPlaneswalker4BannerFrame(
                        ResourceManager.newFrame(CardResource.colorlessPlaneswalker4),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(CardResource.gainHybridBlend),
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
                            ResourceManager.newFrame(CardResource.gainPlaneswalker4ColorBlend),
                            ResourceManager.newFrame(colorlessBanner)
                        );
                    }
                    //Create Gold Banner blend
                    BufferedImage goldBanner = getPlaneswalker4BannerFrame(
                        ResourceManager.newFrame(CardResource.multiPlaneswalker4),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(CardResource.gainHybridBlend),
                            ResourceManager.newFrame(colorFrames.get(1))
                        ));
                    //Color piping for Dual-Color
                    return getBlendedFrame(
                        artifact || land ? baseFrame : ResourceManager.newFrame(CardResource.multiPlaneswalker4),
                        ResourceManager.newFrame(CardResource.gainPlaneswalker4ColorBlend),
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
        Set<MagicColor> landColor = new HashSet<>();
        BufferedImage baseFrame = ResourceManager.newFrame(transform ? CardResource.colorlessTransform : CardResource.colorlessHidden);
        if (land) {
            baseFrame = ResourceManager.newFrame(transform ? CardResource.colorlessLandTransform : CardResource.colorlessLandHidden);
            //Land Colors
            landColor = getLandColors(cardDef);
        } else if (artifact) {
            baseFrame = ResourceManager.newFrame(transform ? CardResource.artifactTransform : CardResource.artifactHidden);
        }
        //Multi
        if (cardDef.isMulti() || landColor.size() > 1) {
            if (cardDef.getNumColors() > 2 || land && landColor.size() > 2) {
                if (artifact || land) {
                    if (land) {
                        return transform ?
                            getBlendedFrame(
                                baseFrame,
                                ResourceManager.newFrame(CardResource.gainTransformColorBlend),
                                ResourceManager.newFrame(CardResource.multiLandTransform)
                            ) :
                            getBlendedFrame(
                                baseFrame,
                                ResourceManager.newFrame(CardResource.gainColorBlend),
                                ResourceManager.newFrame(CardResource.multiLandHidden)
                            );
                    } else {
                        return transform ?
                            getBlendedFrame(
                                baseFrame,
                                ResourceManager.newFrame(CardResource.gainTransformColorBlend),
                                ResourceManager.newFrame(CardResource.multiTransform)
                            ) :
                            getBlendedFrame(
                                baseFrame,
                                ResourceManager.newFrame(CardResource.gainColorBlend),
                                ResourceManager.newFrame(CardResource.multiHidden)
                            );
                    }
                } else {
                    return ResourceManager.newFrame(transform ? CardResource.multiTransform : CardResource.multiHidden);
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
                    ResourceManager.newFrame(CardResource.colorlessHidden),
                    getBlendedFrame(
                        ResourceManager.newFrame(colorFrames.get(0)),
                        ResourceManager.newFrame(CardResource.gainHybridBlend),
                        ResourceManager.newFrame(colorFrames.get(1)))
                );
                BufferedImage colorlessTransformBanner = getBannerFrame(
                    ResourceManager.newFrame(CardResource.colorlessTransform),
                    getBlendedFrame(
                        ResourceManager.newFrame(colorFrames.get(0)),
                        ResourceManager.newFrame(CardResource.gainHybridBlend),
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
                        ResourceManager.newFrame(CardResource.gainTransformColorBlend),
                        ResourceManager.newFrame(colorlessTransformBanner)
                    ) : getBlendedFrame(
                        baseFrame,
                        ResourceManager.newFrame(CardResource.gainColorBlend),
                        ResourceManager.newFrame(colorlessHiddenBanner)
                    );
                }
                //Create Gold Banner blend
                BufferedImage goldHiddenBanner = getBannerFrame(
                    ResourceManager.newFrame(CardResource.multiHidden),
                    getBlendedFrame(
                        ResourceManager.newFrame(colorFrames.get(0)),
                        ResourceManager.newFrame(CardResource.gainTransformHybridBanner),
                        ResourceManager.newFrame(colorFrames.get(1))
                    ));
                BufferedImage goldTransformBanner = getBannerFrame(
                    ResourceManager.newFrame(CardResource.multiTransform),
                    getBlendedFrame(
                        ResourceManager.newFrame(colorFrames.get(0)),
                        ResourceManager.newFrame(CardResource.gainTransformHybridBanner),
                        ResourceManager.newFrame(colorFrames.get(1))
                    ));
                //Color piping for Dual-Color
                if (artifact || land) {
                    return transform ?
                        getBlendedFrame(
                            baseFrame,
                            ResourceManager.newFrame(CardResource.gainTransformColorBlend),
                            ResourceManager.newFrame(goldTransformBanner)
                        ) :
                        getBlendedFrame(
                            baseFrame,
                            ResourceManager.newFrame(CardResource.gainColorBlend),
                            ResourceManager.newFrame(goldHiddenBanner)
                        );
                } else {
                    return transform ?
                        getBlendedFrame(
                            ResourceManager.newFrame(CardResource.multiTransform),
                            ResourceManager.newFrame(CardResource.gainTransformColorBlend),
                            ResourceManager.newFrame(goldTransformBanner)
                        ) :
                        getBlendedFrame(
                            ResourceManager.newFrame(CardResource.multiHidden),
                            ResourceManager.newFrame(CardResource.gainColorBlend),
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
        Set<MagicColor> landColor = new HashSet<>();
        if (OracleText.getPlaneswalkerAbilityCount(cardDef) <= 3) {
            BufferedImage baseFrame = ResourceManager.newFrame(CardResource.colorlessPlaneswalkerFrame);
            if (land) {
                // No frame for land planeswalkers
                //Land Colors
                landColor = getLandColors(cardDef);
            } else if (artifact) {
                baseFrame = ResourceManager.newFrame(transform ? CardResource.artifactPlaneswalkerTransform : CardResource.artifactPlaneswalkerHidden);
            }
            //Multi
            if (cardDef.isMulti() || landColor.size() > 1) {
                if (cardDef.getNumColors() > 2 || land && landColor.size() > 2) {
                    if (transform) {
                        return artifact || land ?
                            getBlendedFrame(
                                baseFrame,
                                ResourceManager.newFrame(CardResource.gainPlaneswalkerColorBlend),
                                ResourceManager.newFrame(CardResource.multiPlaneswalkerTransform)
                            ) :
                            ResourceManager.newFrame(CardResource.multiPlaneswalkerTransform);
                    } else {
                        return artifact || land ?
                            getBlendedFrame(
                                baseFrame,
                                ResourceManager.newFrame(CardResource.gainPlaneswalkerColorBlend),
                                ResourceManager.newFrame(CardResource.multiPlaneswalkerHidden)
                            ) :
                            ResourceManager.newFrame(CardResource.multiPlaneswalkerHidden);
                    }
                } else {
                    //Hybrid
                    List<BufferedImage> colorFrames = new ArrayList<>(2);
                    colorFrames.addAll(getColorPairOrder(cardDef).stream().map(Frame::getPlaneswalkerFrame).collect(Collectors.toList()));
                    //A colorless Banner with color piping
                    BufferedImage colorlessBanner = getPlaneswalkerTransformBannerFrame(
                        ResourceManager.newFrame(CardResource.colorlessPlaneswalkerFrame),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(CardResource.gainHybridBlend),
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
                            ResourceManager.newFrame(CardResource.gainPlaneswalkerColorBlend),
                            ResourceManager.newFrame(colorlessBanner)
                        );
                    }
                    //Create Gold Banner blend
                    BufferedImage goldBanner = getPlaneswalkerBannerFrame(
                        ResourceManager.newFrame(transform ? CardResource.multiPlaneswalkerTransform : CardResource.multiPlaneswalkerHidden),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(CardResource.gainHybridBlend),
                            ResourceManager.newFrame(colorFrames.get(1))
                        ));
                    //Color piping for Dual-Color
                    return getBlendedFrame(
                        transform ? artifact || land ? baseFrame : ResourceManager.newFrame(CardResource.multiPlaneswalkerTransform) : artifact || land ? baseFrame : ResourceManager.newFrame(CardResource.multiPlaneswalkerHidden),
                        ResourceManager.newFrame(CardResource.gainPlaneswalkerColorBlend),
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
            BufferedImage baseFrame = ResourceManager.newFrame(CardResource.colorlessPlaneswalker4);
            if (land) {
                // No frame for land planeswalkers
                //Land Colors
                landColor = getLandColors(cardDef);
            } else if (artifact) {
                baseFrame = ResourceManager.newFrame(CardResource.artifactPlaneswalker4);
            }
            //Multi
            if (cardDef.isMulti() || landColor.size() > 1) {
                if (cardDef.getNumColors() > 2 || land && landColor.size() > 2) {
                    return artifact || land ?
                        getBlendedFrame(
                            baseFrame,
                            ResourceManager.newFrame(CardResource.gainPlaneswalker4ColorBlend),
                            ResourceManager.newFrame(CardResource.multiPlaneswalker4)
                        ) :
                        ResourceManager.newFrame(CardResource.multiPlaneswalker4);
                } else {
                    //Hybrid
                    List<BufferedImage> colorFrames = new ArrayList<>(2);
                    colorFrames.addAll(getColorPairOrder(cardDef).stream().map(Frame::getPlaneswalker4Frame).collect(Collectors.toList()));
                    //A colorless Banner with color piping
                    BufferedImage colorlessBanner = getPlaneswalker4BannerFrame(
                        ResourceManager.newFrame(CardResource.colorlessPlaneswalker4),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(CardResource.gainHybridBlend),
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
                            ResourceManager.newFrame(CardResource.gainPlaneswalker4ColorBlend),
                            ResourceManager.newFrame(colorlessBanner)
                        );
                    }
                    //Create Gold Banner blend
                    BufferedImage goldBanner = getPlaneswalker4BannerFrame(
                        ResourceManager.newFrame(CardResource.multiPlaneswalker4),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(CardResource.gainHybridBlend),
                            ResourceManager.newFrame(colorFrames.get(1))
                        ));
                    //Color piping for Dual-Color
                    return getBlendedFrame(
                        artifact || land ? baseFrame : ResourceManager.newFrame(CardResource.multiPlaneswalker4),
                        ResourceManager.newFrame(CardResource.gainPlaneswalker4ColorBlend),
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
            return ResourceManager.newFrame(CardResource.artifactFrame);
        }
        return ResourceManager.newFrame(CardResource.colorlessFrame);
    }

    static BufferedImage getVehicleFrameType(IRenderableCard cardDef) {
        return ResourceManager.newFrame(CardResource.vehicleFrame);
    }

}
