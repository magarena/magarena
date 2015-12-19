package magic.ui.cardBuilder.renderers;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
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

public class CardFrame {

    private static BufferedImage baseFrame;

    static BufferedImage getBasicFrameType(IRenderableCard cardDef) {
        boolean land = cardDef.hasType(MagicType.Land);
        boolean artifact = cardDef.hasType(MagicType.Artifact);
        boolean enchantmentPermanent = cardDef.hasType(MagicType.Enchantment) &&
            (cardDef.hasType(MagicType.Creature) || cardDef.hasType(MagicType.Artifact));
        Set<MagicColor> landColor = new HashSet<>();
        if (land) {
            baseFrame = ResourceManager.newFrame(ResourceManager.landFrame);
            //Land Colors
            landColor = getLandColors(cardDef);
        } else if (artifact) {
            if (enchantmentPermanent) {
                baseFrame = ResourceManager.newFrame(ResourceManager.artifactGod);
            } else {
                baseFrame = ResourceManager.newFrame(ResourceManager.artifactFrame);
            }
        }
        //Multi
        if (cardDef.isMulti() || landColor.size() > 1) {
            if (cardDef.getNumColors() > 2 || land && landColor.size() > 2) {
                if (artifact || land && !enchantmentPermanent) {
                    return getBlendedFrame(
                        baseFrame,
                        ResourceManager.newFrame(ResourceManager.gainColorBlend),
                        ResourceManager.newFrame(ResourceManager.multiFrame));
                } else if (land) {
                    return ResourceManager.newFrame(ResourceManager.multiLandGod);
                } else {
                    if (enchantmentPermanent) {
                        return ResourceManager.newFrame(ResourceManager.multiGod);
                    } else {
                        return ResourceManager.newFrame(ResourceManager.multiFrame);
                    }
                }
            } else {
                //Hybrid
                List<BufferedImage> colorFrames = new ArrayList<>();
                if (land) {
                    if (enchantmentPermanent) {
                        colorFrames.addAll(getColorOrder(landColor).stream().map(CardFrame::getLandGodFrame).collect(Collectors.toList()));
                    } else {
                        colorFrames.addAll(getColorOrder(landColor).stream().map(CardFrame::getLandFrame).collect(Collectors.toList()));
                    }
                } else {
                    if (enchantmentPermanent) {
                        colorFrames.addAll(getColorOrder(cardDef).stream().map(CardFrame::getGodFrame).collect(Collectors.toList()));
                    } else {
                        colorFrames.addAll(getColorOrder(cardDef).stream().map(CardFrame::getFrame).collect(Collectors.toList()));
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
                if (artifact || land) {
                    return getBlendedFrame(
                        baseFrame,
                        ResourceManager.newFrame(ResourceManager.gainColorBlend),
                        ResourceManager.newFrame(goldBanner));
                } else {
                    if (enchantmentPermanent) {
                        return getBlendedFrame(
                            ResourceManager.newFrame(ResourceManager.multiGod),
                            ResourceManager.newFrame(ResourceManager.gainColorBlend),
                            ResourceManager.newFrame(goldBanner));
                    } else {
                        return getBlendedFrame(
                            ResourceManager.newFrame(ResourceManager.multiFrame),
                            ResourceManager.newFrame(ResourceManager.gainColorBlend),
                            ResourceManager.newFrame(goldBanner));
                    }
                }
            }
        }
        //Mono
        for (MagicColor color : MagicColor.values()) {
            if (cardDef.hasColor(color) || landColor.contains(color)) {
                if (artifact) {
                    return getBlendFrame(baseFrame, color);
                } else if (land) {
                    if (enchantmentPermanent) {
                        return getLandGodFrame(color);
                    } else {
                        return getLandFrame(color);
                    }
                } else {
                    if (enchantmentPermanent) {
                        return getGodFrame(color);
                    } else {
                        return getFrame(color);
                    }
                }
            }
        }
        //Colorless
        if (land || artifact) {
            return baseFrame;
        }
        return ResourceManager.newFrame(ResourceManager.colorlessFrame);
    }

    static BufferedImage getTokenFrameType(IRenderableCard cardDef) {
        boolean hasText = cardDef.hasText();
        boolean land = cardDef.hasType(MagicType.Land);
        boolean artifact = cardDef.hasType(MagicType.Artifact);
        Set<MagicColor> landColor = new HashSet<>();
        //Land Colors
        if (land) {
            landColor = getLandColors(cardDef);
        }
        baseFrame = getBaseTokenFrame(cardDef.getTypes(), hasText);
        boolean hybrid = cardDef.isHybrid() || cardDef.getNumColors() == 2;
        //Multi
        if (cardDef.isMulti() || landColor.size() > 1) {
            if (cardDef.getNumColors() > 2 || land && landColor.size() > 2) {
                if (artifact || land) {
                    if (hasText) {
                        return getBlendedFrame(
                            baseFrame,
                            ResourceManager.newFrame(ResourceManager.gainColorTokenBlendText),
                            ResourceManager.newFrame(ResourceManager.multiTokenFrameText));
                    } else {
                        return getBlendedFrame(
                            baseFrame,
                            ResourceManager.newFrame(ResourceManager.gainColorTokenBlend),
                            ResourceManager.newFrame(ResourceManager.multiTokenFrame));
                    }
                } else {
                    if (hasText) {
                        return ResourceManager.newFrame(ResourceManager.multiTokenFrameText);
                    } else {
                        return ResourceManager.newFrame(ResourceManager.multiTokenFrame);
                    }
                }
            } else {
                //Hybrid
                List<BufferedImage> colorFrames = new ArrayList<>();
                if (hasText) {
                    colorFrames.addAll(getColorOrder(cardDef).stream().map(CardFrame::getTokenFrameText).collect(Collectors.toList()));
                } else {
                    colorFrames.addAll(getColorOrder(cardDef).stream().map(CardFrame::getTokenFrame).collect(Collectors.toList()));
                }
                //A colorless Banner with color piping
                BufferedImage colorlessTokenBanner;
                if (hasText) {
                    colorlessTokenBanner = getTokenBannerFrameText(
                        ResourceManager.newFrame(ResourceManager.colorlessTokenFrameText),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(ResourceManager.gainHybridBlend),
                            ResourceManager.newFrame(colorFrames.get(1)))
                    );
                } else {
                    colorlessTokenBanner = getTokenBannerFrame(
                        ResourceManager.newFrame(ResourceManager.colorlessTokenFrame),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(ResourceManager.gainHybridBlend),
                            ResourceManager.newFrame(colorFrames.get(1)))
                    );
                }
                //Check for Hybrid + Return colorless banner blend
                if (hybrid) {
                    return colorlessTokenBanner;
                }
                //Check dual color land + Return colorless banner blend
                if (land && landColor.size() == 2) {
                    if (hasText) {
                        return getBlendedFrame(
                            baseFrame,
                            ResourceManager.newFrame(ResourceManager.gainColorTokenBlendText),
                            ResourceManager.newFrame(colorlessTokenBanner)
                        );
                    } else {
                        return getBlendedFrame(
                            baseFrame,
                            ResourceManager.newFrame(ResourceManager.gainColorTokenBlend),
                            ResourceManager.newFrame(colorlessTokenBanner)
                        );
                    }
                }
                //Create Gold Banner blend
                BufferedImage goldTokenBanner;
                if (hasText) {
                    goldTokenBanner = getTokenBannerFrameText(
                        ResourceManager.newFrame(ResourceManager.multiTokenFrameText),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(ResourceManager.gainHybridBlend),
                            ResourceManager.newFrame(colorFrames.get(1))
                        ));
                } else {
                    goldTokenBanner = getTokenBannerFrame(
                        ResourceManager.newFrame(ResourceManager.multiTokenFrame),
                        getBlendedFrame(
                            ResourceManager.newFrame(colorFrames.get(0)),
                            ResourceManager.newFrame(ResourceManager.gainHybridBlend),
                            ResourceManager.newFrame(colorFrames.get(1))
                        ));
                }
                //Color piping for Dual-Color
                if (artifact || land) {
                    if (hasText) {
                        return getBlendedFrame(
                            baseFrame,
                            ResourceManager.newFrame(ResourceManager.gainColorTokenBlendText),
                            ResourceManager.newFrame(goldTokenBanner));
                    } else {
                        return getBlendedFrame(
                            baseFrame,
                            ResourceManager.newFrame(ResourceManager.gainColorTokenBlend),
                            ResourceManager.newFrame(goldTokenBanner));
                    }
                } else {
                    if (hasText) {
                        return getBlendedFrame(
                            ResourceManager.newFrame(ResourceManager.multiTokenFrameText),
                            ResourceManager.newFrame(ResourceManager.gainColorTokenBlendText),
                            ResourceManager.newFrame(goldTokenBanner));
                    } else {
                        return getBlendedFrame(
                            ResourceManager.newFrame(ResourceManager.multiTokenFrame),
                            ResourceManager.newFrame(ResourceManager.gainColorTokenBlend),
                            ResourceManager.newFrame(goldTokenBanner));
                    }
                }
            }
        }
        //Mono
        for (MagicColor color : MagicColor.values()) {
            if (cardDef.hasColor(color) || landColor.contains(color)) {
                if (artifact || land) {
                    if (hasText) {
                        return getTokenBlendFrameText(baseFrame, color);
                    } else {
                        return getTokenBlendFrame(baseFrame, color);
                    }
                } else {
                    if (hasText) {
                        return getTokenFrameText(color);
                    } else {
                        return getTokenFrame(color);
                    }
                }
            }
        }
        //Colorless
        if (land || artifact) {
            return baseFrame;
        }
        if (hasText) {
            return ResourceManager.newFrame(ResourceManager.colorlessTokenFrameText);
        } else {
            return ResourceManager.newFrame(ResourceManager.colorlessTokenFrame);
        }
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

    private static BufferedImage getBaseTokenFrame(Collection<MagicType> types, boolean hasText) {
        if (types.contains(MagicType.Land)) {
            if (hasText) {
                return ResourceManager.newFrame(ResourceManager.colorlessTokenFrameText);
            } else {
                return ResourceManager.newFrame(ResourceManager.colorlessTokenFrame);
            }
        }
        if (types.contains(MagicType.Artifact)) {
            if (hasText) {
                return ResourceManager.newFrame(ResourceManager.artifactTokenFrameText);
            } else {
                return ResourceManager.newFrame(ResourceManager.artifactTokenFrame);
            }
        }
        return baseFrame;
    }

    static BufferedImage getBlendedFrame(
        BufferedImage baseFrame,
        BufferedImage blend,
        BufferedImage colorFrame) {
        //create overlay
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OUT);
        Graphics2D graphics2D = blend.createGraphics();
        graphics2D.setComposite(ac);
        graphics2D.drawImage(colorFrame, null, 0, 0);
        graphics2D.dispose();

        //draw overlay on top of baseFrame
        Graphics2D graphics2D1 = baseFrame.createGraphics();
        graphics2D1.drawImage(blend, null, 0, 0);
        graphics2D1.dispose();
        return ResourceManager.newFrame(baseFrame);
    }

    private static BufferedImage getBlendFrame(BufferedImage frame, MagicColor color) {
        return getBlendedFrame(frame,
            ResourceManager.newFrame(ResourceManager.gainColorBlend),
            getFrame(color));
    }

    private static BufferedImage getPlaneswalkerBlendFrame(BufferedImage frame, MagicColor color) {
        return getBlendedFrame(frame,
            ResourceManager.newFrame(ResourceManager.gainPlaneswalkerColorBlend),
            getPlaneswalkerFrame(color));
    }

    static List<MagicColor> getColorOrder(IRenderableCard cardDef) {
        Set<MagicColor> colors = new HashSet<>();
        //Get colors
        for (MagicColor color : MagicColor.values()) {
            if (cardDef.hasColor(color)) {
                colors.add(color);
            }
        }
        return getColorOrder(colors);
    }

    static List<MagicColor> getColorOrder(Set<MagicColor> colors) {
        //Color order
        List<MagicColor> orderedColors = new ArrayList<>(colors);
        //Non-color order pairings
        if (orderedColors.get(0) == MagicColor.White && orderedColors.get(1) != MagicColor.Blue && orderedColors.get(1) != MagicColor.Black) {
            Collections.swap(orderedColors, 0, 1);
        }
        if (orderedColors.get(0) == MagicColor.Blue && orderedColors.get(1) != MagicColor.Black) {
            Collections.swap(orderedColors, 0, 1);
        }
        if (orderedColors.get(0) == MagicColor.Black && orderedColors.get(1) != MagicColor.Red && orderedColors.get(1) != MagicColor.Green) {
            Collections.swap(orderedColors, 0, 1);
        }
        if (orderedColors.get(0) == MagicColor.Red && orderedColors.get(1) != MagicColor.White && orderedColors.get(1) != MagicColor.Blue && orderedColors.get(1) != MagicColor.Green) {
            Collections.swap(orderedColors, 0, 1);
        }
        if (orderedColors.get(0) == MagicColor.Green && orderedColors.get(1) != MagicColor.White && orderedColors.get(1) != MagicColor.Blue) {
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
            default:
                return ResourceManager.newFrame(ResourceManager.colorlessFrame);
        }
    }

    private static BufferedImage getGodFrame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(ResourceManager.whiteGod);
            case Blue:
                return ResourceManager.newFrame(ResourceManager.blueGod);
            case Black:
                return ResourceManager.newFrame(ResourceManager.blackGod);
            case Red:
                return ResourceManager.newFrame(ResourceManager.redGod);
            case Green:
                return ResourceManager.newFrame(ResourceManager.greenGod);
            default:
                return null;
        }
    }

    public static Set<MagicColor> getLandColors(IRenderableCard cardDef) {
        Collection<MagicManaActivation> landActivations = cardDef.getManaActivations();
        Set<MagicColor> landColor = new HashSet<>();
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
        Set<MagicColor> basicLandCount = new HashSet<>();
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
        return landColor;
    }

    private static BufferedImage getLandFrame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(ResourceManager.whiteLandFrame);
            case Blue:
                return ResourceManager.newFrame(ResourceManager.blueLandFrame);
            case Black:
                return ResourceManager.newFrame(ResourceManager.blackLandFrame);
            case Red:
                return ResourceManager.newFrame(ResourceManager.redLandFrame);
            case Green:
                return ResourceManager.newFrame(ResourceManager.greenLandFrame);
            default:
                return ResourceManager.newFrame(ResourceManager.landFrame);
        }
    }

    private static BufferedImage getLandGodFrame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(ResourceManager.whiteLandGod);
            case Blue:
                return ResourceManager.newFrame(ResourceManager.blueLandGod);
            case Black:
                return ResourceManager.newFrame(ResourceManager.blackLandGod);
            case Red:
                return ResourceManager.newFrame(ResourceManager.redLandGod);
            case Green:
                return ResourceManager.newFrame(ResourceManager.greenLandGod);
            default:
                return null;
        }
    }

    private static BufferedImage getLandLevellerFrame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(ResourceManager.whiteLandLevellerFrame);
            case Blue:
                return ResourceManager.newFrame(ResourceManager.blueLandLevellerFrame);
            case Black:
                return ResourceManager.newFrame(ResourceManager.blackLandLevellerFrame);
            case Red:
                return ResourceManager.newFrame(ResourceManager.redLandLevellerFrame);
            case Green:
                return ResourceManager.newFrame(ResourceManager.greenLandLevellerFrame);
            default:
                return ResourceManager.newFrame(ResourceManager.colorlessLandLevellerFrame);
        }
    }

    private static BufferedImage getDevoidFrame(MagicColor color) {
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
            default:
                return ResourceManager.newFrame(ResourceManager.colorlessDevoidFrame);
        }
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
            default:
                return ResourceManager.newFrame(ResourceManager.colorlessTokenFrame);
        }
    }

    private static BufferedImage getLevellerFrame(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.newFrame(ResourceManager.whiteLevellerFrame);
            case Blue:
                return ResourceManager.newFrame(ResourceManager.blueLevellerFrame);
            case Black:
                return ResourceManager.newFrame(ResourceManager.blackLevellerFrame);
            case Red:
                return ResourceManager.newFrame(ResourceManager.redLevellerFrame);
            case Green:
                return ResourceManager.newFrame(ResourceManager.greenLevellerFrame);
            default:
                return ResourceManager.newFrame(ResourceManager.colorlessLevellerFrame);
        }
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
            default:
                return ResourceManager.newFrame(ResourceManager.colorlessPlaneswalkerFrame);
        }
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
            default:
                return ResourceManager.newFrame(ResourceManager.colorlessTokenFrameText);
        }
    }

    static BufferedImage getLevellerFrameType(IRenderableCard cardDef) {
        boolean land = cardDef.hasType(MagicType.Land);
        boolean artifact = cardDef.hasType(MagicType.Artifact);
        Set<MagicColor> landColor = new HashSet<>();
        if (land) {
            baseFrame = ResourceManager.newFrame(ResourceManager.colorlessLandLevellerFrame);
            //Land Colors
            landColor = getLandColors(cardDef);
        } else if (artifact) {
            baseFrame = ResourceManager.newFrame(ResourceManager.artifactLevellerFrame);
        }
        //Multi
        if (cardDef.isMulti() || landColor.size() > 1) {
            if (cardDef.getNumColors() > 2 || land && landColor.size() > 2) {
                if (artifact || land) {
                    return getBlendedFrame(
                        baseFrame,
                        ResourceManager.newFrame(ResourceManager.gainColorBlend),
                        ResourceManager.newFrame(ResourceManager.multiLevellerFrame));
                } else {
                    return ResourceManager.newFrame(ResourceManager.multiLevellerFrame);
                }
            } else {
                //Hybrid
                List<BufferedImage> colorFrames = new ArrayList<>();
                if (land) {
                    colorFrames.addAll(getColorOrder(landColor).stream().map(CardFrame::getLandLevellerFrame).collect(Collectors.toList()));
                } else {
                    colorFrames.addAll(getColorOrder(cardDef).stream().map(CardFrame::getLevellerFrame).collect(Collectors.toList()));
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
                    ResourceManager.newFrame(ResourceManager.multiLevellerFrame),
                    getBlendedFrame(
                        ResourceManager.newFrame(colorFrames.get(0)),
                        ResourceManager.newFrame(ResourceManager.gainHybridBlend),
                        ResourceManager.newFrame(colorFrames.get(1))
                    ));
                //Color piping for Dual-Color
                if (artifact || land) {
                    return getBlendedFrame(
                        baseFrame,
                        ResourceManager.newFrame(ResourceManager.gainColorBlend),
                        ResourceManager.newFrame(goldBanner));
                } else {
                    return getBlendedFrame(
                        ResourceManager.newFrame(ResourceManager.multiLevellerFrame),
                        ResourceManager.newFrame(ResourceManager.gainColorBlend),
                        ResourceManager.newFrame(goldBanner));
                }
            }
        }
        //Mono
        for (MagicColor color : MagicColor.values()) {
            if (cardDef.hasColor(color) || landColor.contains(color)) {
                if (artifact) {
                    return getBlendFrame(baseFrame, color);
                } else if (land) {
                    return getLandLevellerFrame(color);
                } else {
                    return getLevellerFrame(color);
                }
            }
        }
        //Colorless
        if (land || artifact) {
            return baseFrame;
        }
        return ResourceManager.newFrame(ResourceManager.colorlessLevellerFrame);
    }

    static BufferedImage getDevoidFrameType(IRenderableCard cardDef) {
        boolean land = cardDef.hasType(MagicType.Land);
        boolean artifact = cardDef.hasType(MagicType.Artifact);
        Set<MagicColor> landColor = new HashSet<>();
        if (land) {
            baseFrame = ResourceManager.newFrame(ResourceManager.colorlessDevoidFrame);
            //Land Colors
            landColor = getLandColors(cardDef);
        } else if (artifact) {
            baseFrame = ResourceManager.newFrame(ResourceManager.artifactDevoidFrame);
        }
        //Multi
        if (cardDef.isMulti() || landColor.size() > 1) {
            if (cardDef.getNumColors() > 2 || land && landColor.size() > 2) {
                if (artifact || land) {
                    return getBlendedFrame(
                        baseFrame,
                        ResourceManager.newFrame(ResourceManager.gainColorBlend),
                        ResourceManager.newFrame(ResourceManager.multiDevoidFrame));
                } else {
                    return ResourceManager.newFrame(ResourceManager.multiDevoidFrame);
                }
            } else {
                //Hybrid
                List<BufferedImage> colorFrames = new ArrayList<>();
                if (land) {
                    colorFrames.addAll(getColorOrder(landColor).stream().map(CardFrame::getDevoidFrame).collect(Collectors.toList()));
                } else {
                    colorFrames.addAll(getColorOrder(cardDef).stream().map(CardFrame::getDevoidFrame).collect(Collectors.toList()));
                }
                //A colorless Banner with color piping
                BufferedImage colorlessBanner = getBannerFrame(
                    ResourceManager.newFrame(ResourceManager.colorlessDevoidFrame),
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
                    ResourceManager.newFrame(ResourceManager.multiDevoidFrame),
                    getBlendedFrame(
                        ResourceManager.newFrame(colorFrames.get(0)),
                        ResourceManager.newFrame(ResourceManager.gainHybridBlend),
                        ResourceManager.newFrame(colorFrames.get(1))
                    ));
                //Color piping for Dual-Color
                if (artifact || land) {
                    return getBlendedFrame(
                        baseFrame,
                        ResourceManager.newFrame(ResourceManager.gainColorBlend),
                        ResourceManager.newFrame(goldBanner));
                } else {
                    return getBlendedFrame(
                        ResourceManager.newFrame(ResourceManager.multiDevoidFrame),
                        ResourceManager.newFrame(ResourceManager.gainColorBlend),
                        ResourceManager.newFrame(goldBanner));
                }
            }
        }
        //Mono
        for (MagicColor color : MagicColor.values()) {
            if (cardDef.hasColor(color) || landColor.contains(color)) {
                if (artifact) {
                    return getBlendFrame(baseFrame, color);
                } else {
                    return getDevoidFrame(color);
                }
            }
        }
        //Colorless
        if (land || artifact) {
            return baseFrame;
        }
        return ResourceManager.newFrame(ResourceManager.colorlessDevoidFrame);
    }

    static BufferedImage getPlaneswalkerFrameType(IRenderableCard cardDef) {
        if (OracleText.getPlaneswalkerAbilityCount(cardDef) == 3) {
            boolean land = cardDef.hasType(MagicType.Land);
            boolean artifact = cardDef.hasType(MagicType.Artifact);
            Set<MagicColor> landColor = new HashSet<>();
            if (land) {
                baseFrame = ResourceManager.newFrame(ResourceManager.colorlessLandLevellerFrame); // No frame for land planeswalkers
                //Land Colors
                landColor = getLandColors(cardDef);
            } else if (artifact) {
                baseFrame = ResourceManager.newFrame(ResourceManager.artifactPlaneswalkerFrame);
            }
            //Multi
            if (cardDef.isMulti() || landColor.size() > 1) {
                if (cardDef.getNumColors() > 2 || land && landColor.size() > 2) {
                    if (artifact || land) {
                        return getBlendedFrame(
                            baseFrame,
                            ResourceManager.newFrame(ResourceManager.gainPlaneswalkerColorBlend),
                            ResourceManager.newFrame(ResourceManager.multiPlaneswalkerFrame));
                    } else {
                        return ResourceManager.newFrame(ResourceManager.multiPlaneswalkerFrame);
                    }
                } else {
                    //Hybrid
                    List<BufferedImage> colorFrames = new ArrayList<>();
                    if (land) {
                        colorFrames.addAll(getColorOrder(landColor).stream().map(CardFrame::getLandLevellerFrame).collect(Collectors.toList()));
                    } else {
                        colorFrames.addAll(getColorOrder(cardDef).stream().map(CardFrame::getPlaneswalkerFrame).collect(Collectors.toList()));
                    }
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
                    if (artifact || land) {
                        return getBlendedFrame(
                            baseFrame,
                            ResourceManager.newFrame(ResourceManager.gainPlaneswalkerColorBlend),
                            ResourceManager.newFrame(goldBanner));
                    } else {
                        return getBlendedFrame(
                            ResourceManager.newFrame(ResourceManager.multiPlaneswalkerFrame),
                            ResourceManager.newFrame(ResourceManager.gainPlaneswalkerColorBlend),
                            ResourceManager.newFrame(goldBanner));
                    }
                }
            }
            //Mono
            for (MagicColor color : MagicColor.values()) {
                if (cardDef.hasColor(color) || landColor.contains(color)) {
                    if (artifact) {
                        return getPlaneswalkerBlendFrame(baseFrame, color);
                    } else if (land) {
                        return getLandLevellerFrame(color);
                    } else {
                        return getPlaneswalkerFrame(color);
                    }
                }
            }
            return ResourceManager.newFrame(ResourceManager.artifactPlaneswalkerFrame);
        }
        return getLevellerFrameType(cardDef); //Catch 4 ability planeswalkers
    }
}
