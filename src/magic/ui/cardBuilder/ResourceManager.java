package magic.ui.cardBuilder;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import magic.ui.utility.GraphicsUtils;

public class ResourceManager {

    // Used as reference class for accessing JAR resources.
    private static final ResourceManager instance = new ResourceManager();

    public static final BufferedImage multiFrame = getFrame("mcard.jpg");
    public static final BufferedImage artifactFrame = getFrame("acard.jpg");
    public static final BufferedImage colorlessFrame = getFrame("ccard.jpg");
    public static final BufferedImage redFrame = getFrame("rcard.jpg");
    public static final BufferedImage blackFrame = getFrame("bcard.jpg");
    public static final BufferedImage blueFrame = getFrame("ucard.jpg");
    public static final BufferedImage greenFrame = getFrame("gcard.jpg");
    public static final BufferedImage whiteFrame = getFrame("wcard.jpg");
    public static final BufferedImage landFrame = getFrame("lcard.jpg");
    public static final BufferedImage whiteLandFrame = getFrame("wlcard.jpg");
    public static final BufferedImage blueLandFrame = getFrame("ulcard.jpg");
    public static final BufferedImage blackLandFrame = getFrame("blcard.jpg");
    public static final BufferedImage redLandFrame = getFrame("rlcard.jpg");
    public static final BufferedImage greenLandFrame = getFrame("glcard.jpg");
    public static final BufferedImage multiLandFrame = getFrame("mlcard.jpg");
    public static final BufferedImage redPTPanel = getPTBuffered("rpt.png");
    public static final BufferedImage blackPTPanel = getPTBuffered("bpt.png");
    public static final BufferedImage bluePTPanel = getPTBuffered("upt.png");
    public static final BufferedImage greenPTPanel = getPTBuffered("gpt.png");
    public static final BufferedImage whitePTPanel = getPTBuffered("wpt.png");
    public static final BufferedImage colorlessPTPanel = getPTBuffered("cpt.png");
    public static final BufferedImage artifactPTPanel = getPTBuffered("apt.png");
    public static final BufferedImage multiPTPanel = getPTBuffered("mpt.png");
    public static final BufferedImage gainColorBlend = getFrame("color_blend.png");
    public static final BufferedImage gainHybridBlend = getFrame("blendingMask.png");
    public static final BufferedImage gainHybridBanner = getFrame("hybrid_blend.png");
    public static final BufferedImage defaultWhite = getImage("default_white.jpg");
    public static final BufferedImage defaultBlue = getImage("default_blue.jpg");
    public static final BufferedImage defaultBlack = getImage("default_black.jpg");
    public static final BufferedImage defaultRed = getImage("default_red.jpg");
    public static final BufferedImage defaultGreen = getImage("default_green.jpg");
    public static final BufferedImage defaultArtifact = getImage("default_artifact.jpg");
    public static final BufferedImage defaultLand = getImage("default_land.jpg");
    public static final BufferedImage defaultColorless = getImage("default_colorless.jpg");
    public static final BufferedImage defaultMulti = getImage("default_multi.jpg");
    public static final BufferedImage defaultHybridBlend = getImage("imageHybridBlend.png");
    public static final BufferedImage whiteLandImage = getWatermark("watermark_w.png");
    public static final BufferedImage blueLandImage = getWatermark("watermark_u.png");
    public static final BufferedImage blackLandImage = getWatermark("watermark_b.png");
    public static final BufferedImage redLandImage = getWatermark("watermark_r.png");
    public static final BufferedImage greenLandImage = getWatermark("watermark_g.png");
    public static final BufferedImage bgLandImage = getWatermark("watermark_bg.png");
    public static final BufferedImage brLandImage = getWatermark("watermark_br.png");
    public static final BufferedImage guLandImage = getWatermark("watermark_gu.png");
    public static final BufferedImage gwLandImage = getWatermark("watermark_gw.png");
    public static final BufferedImage rgLandImage = getWatermark("watermark_rg.png");
    public static final BufferedImage rwLandImage = getWatermark("watermark_rw.png");
    public static final BufferedImage ubLandImage = getWatermark("watermark_ub.png");
    public static final BufferedImage urLandImage = getWatermark("watermark_ur.png");
    public static final BufferedImage wbLandImage = getWatermark("watermark_wb.png");
    public static final BufferedImage wuLandImage = getWatermark("watermark_wu.png");
    public static final BufferedImage artifactNyx = getFrame("nyx/acard.jpg");
    public static final BufferedImage colorlessNyx = getFrame("nyx/ccard.jpg");
    public static final BufferedImage landNyx = getFrame("nyx/clcard.jpg");
    public static final BufferedImage whiteNyx = getFrame("nyx/wcard.jpg");
    public static final BufferedImage whiteLandNyx = getFrame("nyx/wlcard.jpg");
    public static final BufferedImage blueNyx = getFrame("nyx/ucard.jpg");
    public static final BufferedImage blueLandNyx = getFrame("nyx/ulcard.jpg");
    public static final BufferedImage blackNyx = getFrame("nyx/bcard.jpg");
    public static final BufferedImage blackLandNyx = getFrame("nyx/blcard.jpg");
    public static final BufferedImage redNyx = getFrame("nyx/rcard.jpg");
    public static final BufferedImage redLandNyx = getFrame("nyx/rlcard.jpg");
    public static final BufferedImage greenNyx = getFrame("nyx/gcard.jpg");
    public static final BufferedImage greenLandNyx = getFrame("nyx/glcard.jpg");
    public static final BufferedImage multiNyx = getFrame("nyx/mcard.jpg");
    public static final BufferedImage multiLandNyx = getFrame("nyx/mlcard.jpg");
    public static final BufferedImage colorlessTokenFrame = getFrame("token/ccard.jpg");
    public static final BufferedImage colorlessTokenFrameText = getFrame("token/ccard2.jpg");
    public static final BufferedImage artifactTokenFrame = getFrame("token/acard.jpg");
    public static final BufferedImage artifactTokenFrameText = getFrame("token/acard2.jpg");
    public static final BufferedImage whiteTokenFrame = getFrame("token/wcard.jpg");
    public static final BufferedImage whiteTokenFrameText = getFrame("token/wcard2.jpg");
    public static final BufferedImage blueTokenFrame = getFrame("token/ucard.jpg");
    public static final BufferedImage blueTokenFrameText = getFrame("token/ucard2.jpg");
    public static final BufferedImage blackTokenFrame = getFrame("token/bcard.jpg");
    public static final BufferedImage blackTokenFrameText = getFrame("token/bcard2.jpg");
    public static final BufferedImage redTokenFrame = getFrame("token/rcard.jpg");
    public static final BufferedImage redTokenFrameText = getFrame("token/rcard2.jpg");
    public static final BufferedImage greenTokenFrame = getFrame("token/gcard.jpg");
    public static final BufferedImage greenTokenFrameText = getFrame("token/gcard2.jpg");
    public static final BufferedImage multiTokenFrame = getFrame("token/mcard.jpg");
    public static final BufferedImage multiTokenFrameText = getFrame("token/mcard2.jpg");
    public static final BufferedImage gainColorTokenBlend = getFrame("token/color_blend.png");
    public static final BufferedImage gainColorTokenBlendText = getFrame("token/color_blend2.png");
    public static final BufferedImage gainTokenBanner = getFrame("token/hybrid_blend.png");
    public static final BufferedImage gainTokenBannerText = getFrame("token/hybrid_blend2.png");
    public static final BufferedImage tokenImageBlend = getFrame("token/imagemask.png");
    public static final BufferedImage tokenImageBlendText = getFrame("token/imagemask2.png");
    public static final BufferedImage artifactLevellerFrame = getFrame("leveller/acard.jpg");
    public static final BufferedImage whiteLevellerFrame = getFrame("leveller/wcard.jpg");
    public static final BufferedImage whiteLandLevellerFrame = getFrame("leveller/wlcard.jpg");
    public static final BufferedImage blueLevellerFrame = getFrame("leveller/ucard.jpg");
    public static final BufferedImage blueLandLevellerFrame = getFrame("leveller/ulcard.jpg");
    public static final BufferedImage blackLevellerFrame = getFrame("leveller/bcard.jpg");
    public static final BufferedImage blackLandLevellerFrame = getFrame("leveller/blcard.jpg");
    public static final BufferedImage redLevellerFrame = getFrame("leveller/rcard.jpg");
    public static final BufferedImage redLandLevellerFrame = getFrame("leveller/rlcard.jpg");
    public static final BufferedImage greenLevellerFrame = getFrame("leveller/gcard.jpg");
    public static final BufferedImage greenLandLevellerFrame = getFrame("leveller/glcard.jpg");
    public static final BufferedImage multiLevellerFrame = getFrame("leveller/mcard.jpg");
    public static final BufferedImage multiLandLevellerFrame = getFrame("leveller/mlcard.jpg");
    public static final BufferedImage colorlessLevellerFrame = getFrame("leveller/ccard.jpg");
    public static final BufferedImage colorlessLandLevellerFrame = getFrame("leveller/clcard.jpg");
    public static final BufferedImage artifactPlaneswalkerFrame = getFrame("planeswalker/acard.jpg");
    public static final BufferedImage whitePlaneswalkerFrame = getFrame("planeswalker/wcard.jpg");
    public static final BufferedImage bluePlaneswalkerFrame = getFrame("planeswalker/ucard.jpg");
    public static final BufferedImage blackPlaneswalkerFrame = getFrame("planeswalker/bcard.jpg");
    public static final BufferedImage redPlaneswalkerFrame = getFrame("planeswalker/rcard.jpg");
    public static final BufferedImage greenPlaneswalkerFrame = getFrame("planeswalker/gcard.jpg");
    public static final BufferedImage multiPlaneswalkerFrame = getFrame("planeswalker/mcard.jpg");
    public static final BufferedImage colorlessPlaneswalkerFrame = getFrame("planeswalker/clear.jpg");
    public static final BufferedImage gainPlaneswalkerHybridBanner = getFrame("planeswalker/hybrid_blend.png");
    public static final BufferedImage gainPlaneswalkerColorBlend = getFrame("planeswalker/color_blend.png");
    public static final BufferedImage loyaltyPanel = getLoyaltyPanelBuffered("planeswalker/loyalty.png");
    public static final BufferedImage loyaltyUp = getLoyaltyBuffered("planeswalker/loyaltyup.png");
    public static final BufferedImage loyaltyDown = getLoyaltyBuffered("planeswalker/loyaltydown.png");
    public static final BufferedImage loyaltyEven = getLoyaltyBuffered("planeswalker/loyaltynaught.png");
    public static final BufferedImage whiteDevoidFrame = getFrame("devoid/wcard.jpg");
    public static final BufferedImage blueDevoidFrame = getFrame("devoid/ucard.jpg");
    public static final BufferedImage blackDevoidFrame = getFrame("devoid/bcard.jpg");
    public static final BufferedImage redDevoidFrame = getFrame("devoid/rcard.jpg");
    public static final BufferedImage greenDevoidFrame = getFrame("devoid/gcard.jpg");
    public static final BufferedImage colorlessDevoidFrame = getFrame("devoid/ccard.jpg");
    public static final BufferedImage artifactDevoidFrame = getFrame("devoid/acard.jpg");
    public static final BufferedImage multiDevoidFrame = getFrame("devoid/mcard.jpg");
    public static final BufferedImage artifactSymbol = getImage("artifactSymbol.png");
    public static final BufferedImage creatureSymbol = getImage("creatureSymbol.png");
    public static final BufferedImage enchantmentSymbol = getImage("enchantmentSymbol.png");
    public static final BufferedImage instantSymbol = getImage("instantSymbol.png");
    public static final BufferedImage landSymbol = getImage("landSymbol.png");
    public static final BufferedImage multiSymbol = getImage("multiSymbol.png");
    public static final BufferedImage planeswalkerSymbol = getImage("planeswalkerSymbol.png");
    public static final BufferedImage sorcerySymbol = getImage("sorcerySymbol.png");
    public static final BufferedImage magarenaSymbol = getImage("magarenaSymbol.png");
    public static final BufferedImage artifactTransform = getFrame("transform/acard.jpg");
    public static final BufferedImage artifactHidden = getFrame("transform/acard2.jpg");
    public static final BufferedImage colorlessTransform = getFrame("transform/ccard.jpg");
    public static final BufferedImage colorlessHidden = getFrame("transform/ccard2.jpg");
    public static final BufferedImage colorlessLandTransform = getFrame("transform/clcard.jpg");
    public static final BufferedImage colorlessLandHidden = getFrame("transform/clcard2.jpg");
    public static final BufferedImage whiteTransform = getFrame("transform/wcard.jpg");
    public static final BufferedImage whiteHidden = getFrame("transform/wcard2.jpg");
    public static final BufferedImage whiteLandTransform = getFrame("transform/wlcard.jpg");
    public static final BufferedImage whiteLandHidden = getFrame("transform/wlcard2.jpg");
    public static final BufferedImage blueTransform = getFrame("transform/ucard.jpg");
    public static final BufferedImage blueHidden = getFrame("transform/ucard2.jpg");
    public static final BufferedImage blueLandTransform = getFrame("transform/ulcard.jpg");
    public static final BufferedImage blueLandHidden = getFrame("transform/ulcard2.jpg");
    public static final BufferedImage blackTransform = getFrame("transform/bcard.jpg");
    public static final BufferedImage blackHidden = getFrame("transform/bcard2.jpg");
    public static final BufferedImage blackLandTransform = getFrame("transform/blcard.jpg");
    public static final BufferedImage blackLandHidden = getFrame("transform/blcard2.jpg");
    public static final BufferedImage redTransform = getFrame("transform/rcard.jpg");
    public static final BufferedImage redHidden = getFrame("transform/rcard2.jpg");
    public static final BufferedImage redLandTransform = getFrame("transform/rlcard.jpg");
    public static final BufferedImage redLandHidden = getFrame("transform/rlcard2.jpg");
    public static final BufferedImage greenTransform = getFrame("transform/gcard.jpg");
    public static final BufferedImage greenHidden = getFrame("transform/gcard2.jpg");
    public static final BufferedImage greenLandTransform = getFrame("transform/glcard.jpg");
    public static final BufferedImage greenLandHidden = getFrame("transform/glcard2.jpg");
    public static final BufferedImage multiTransform = getFrame("transform/mcard.jpg");
    public static final BufferedImage multiHidden = getFrame("transform/mcard2.jpg");
    public static final BufferedImage multiLandTransform = getFrame("transform/mlcard.jpg");
    public static final BufferedImage multiLandHidden = getFrame("transform/mlcard2.jpg");
    public static final BufferedImage gainTransformColorBlend = getFrame("transform/color_blend.png");
    public static final BufferedImage gainTransformHybridBanner = getFrame("transform/hybrid_blend.png");
    public static final BufferedImage artifactPlaneswalkerTransform = getFrame("transform/planeswalker/acard.jpg");
    public static final BufferedImage artifactPlaneswalkerHidden = getFrame("transform/planeswalker/acard2.jpg");
    public static final BufferedImage whitePlaneswalkerTransform = getFrame("transform/planeswalker/wcard.jpg");
    public static final BufferedImage whitePlaneswalkerHidden = getFrame("transform/planeswalker/wcard2.jpg");
    public static final BufferedImage bluePlaneswalkerTransform = getFrame("transform/planeswalker/ucard.jpg");
    public static final BufferedImage bluePlaneswalkerHidden = getFrame("transform/planeswalker/ucard2.jpg");
    public static final BufferedImage blackPlaneswalkerTransform = getFrame("transform/planeswalker/bcard.jpg");
    public static final BufferedImage blackPlaneswalkerHidden = getFrame("transform/planeswalker/bcard2.jpg");
    public static final BufferedImage redPlaneswalkerTransform = getFrame("transform/planeswalker/rcard.jpg");
    public static final BufferedImage redPlaneswalkerHidden = getFrame("transform/planeswalker/rcard2.jpg");
    public static final BufferedImage greenPlaneswalkerTransform = getFrame("transform/planeswalker/gcard.jpg");
    public static final BufferedImage greenPlaneswalkerHidden = getFrame("transform/planeswalker/gcard2.jpg");
    public static final BufferedImage multiPlaneswalkerTransform = getFrame("transform/planeswalker/mcard.jpg");
    public static final BufferedImage multiPlaneswalkerHidden = getFrame("transform/planeswalker/mcard2.jpg");
    public static final BufferedImage getPlaneswalkerImageBlend = getFrame("planeswalker/imageMask.png");
    public static final BufferedImage artifactHiddenPTPanel = getPTBuffered("transform/apt2.png");
    public static final BufferedImage colorlessHiddenPTPanel = getPTBuffered("transform/cpt2.png");
    public static final BufferedImage whiteHiddenPTPanel = getPTBuffered("transform/wpt2.png");
    public static final BufferedImage blueHiddenPTPanel = getPTBuffered("transform/upt2.png");
    public static final BufferedImage blackHiddenPTPanel = getPTBuffered("transform/bpt2.png");
    public static final BufferedImage redHiddenPTPanel = getPTBuffered("transform/rpt2.png");
    public static final BufferedImage greenHiddenPTPanel = getPTBuffered("transform/gpt2.png");
    public static final BufferedImage multiHiddenPTPanel = getPTBuffered("transform/mpt2.png");
    public static final BufferedImage sunSymbol = getSymbolBuffered("transform/sun_circle.png");
    public static final BufferedImage moonSymbol = getSymbolBuffered("transform/night_circle.png");
    public static final BufferedImage planeswalkerTypeSymbol = getSymbolBuffered("transform/planeswalker.png");
    public static final BufferedImage sparkSymbol = getSymbolBuffered("transform/ccorner.png");



    //Font Map
    private static final String[] names = { };
    private static final Map<String, Font> fontCache = new ConcurrentHashMap<>(names.length);

    static {
        for (String name : names) {
            fontCache.put(name, getFont(name));
        }
    }

    public static void initialize(){}

    public static InputStream getJarResourceStream(String filename) {
        return instance.getClass().getResourceAsStream(filename);
    }

    public static Font getFont(String name) {
        Font font = fontCache.get(name);
        if (font != null) {
            return font;
        }
        String fName = "/cardbuilder/fonts/" + name;
        try (final InputStream is = getJarResourceStream(fName)) {
            font = Font.createFont(Font.TRUETYPE_FONT, is);
            Map<TextAttribute, Object> map = new Hashtable<>();
            map.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
            font = font.deriveFont(map);
        } catch (Exception ex) {
            System.err.println(fName + " not loaded.  Using serif font.");
            font = new Font("serif", Font.PLAIN, 24);
        }
        return font;
    }

    public static BufferedImage getFrame(String imageName) {
        String fName = "/cardbuilder/frames/" + imageName;
        try (final InputStream is = getJarResourceStream(fName)) {
            return GraphicsUtils.getOptimizedImage(ImageIO.read(is));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static BufferedImage getImage(String name) {
        String fName = "/cardbuilder/images/" + name;
        try (final InputStream is = getJarResourceStream(fName)) {
            BufferedImage image = GraphicsUtils.getOptimizedImage(ImageIO.read(is));
            return GraphicsUtils.scale(image, 316, 231);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static BufferedImage getWatermark(String name) {
        String fName = "/cardbuilder/frames/watermark/" + name;
        try (final InputStream is = getJarResourceStream(fName)) {
            BufferedImage image = GraphicsUtils.getOptimizedImage(ImageIO.read(is));
            return GraphicsUtils.scale(image, 145, 145);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static BufferedImage getPTBuffered(String imageName) {
        String fName = "/cardbuilder/frames/" + imageName;
        try (final InputStream is = getJarResourceStream(fName)) {
            BufferedImage image = GraphicsUtils.getOptimizedImage(ImageIO.read(is));
            return GraphicsUtils.scale(image, 81, 42);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static BufferedImage getSymbolBuffered(String imageName) {
        String fName = "/cardbuilder/frames/" + imageName;
        try (final InputStream is = getJarResourceStream(fName)) {
            BufferedImage image = GraphicsUtils.getOptimizedImage(ImageIO.read(is));
            return GraphicsUtils.scale(image, 31, 31);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    public static BufferedImage getLoyaltyBuffered(String imageName) {
        String fName = "/cardbuilder/frames/" + imageName;
        try (final InputStream is = getJarResourceStream(fName)) {
            BufferedImage image = GraphicsUtils.getOptimizedImage(ImageIO.read(is));
            return GraphicsUtils.scale(image, 42, 40);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static BufferedImage getLoyaltyPanelBuffered(String imageName) {
        String fName = "/cardbuilder/frames/" + imageName;
        try (final InputStream is = getJarResourceStream(fName)) {
            BufferedImage image = GraphicsUtils.getOptimizedImage(ImageIO.read(is));
            return GraphicsUtils.scale(image, 60, 38);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Image resizeMana(ImageIcon mana) {
        BufferedImage newImage = new BufferedImage(19,19,BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = newImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics2D.drawImage(mana.getImage(),0,0,18,18,null);
        graphics2D.dispose();
        return newImage;
    }

    public static BufferedImage newFrame(BufferedImage bi) {
        return GraphicsUtils.getOptimizedImage(bi);
    }

}
