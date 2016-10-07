package magic.cardBuilder;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;

import magic.ui.helpers.ImageHelper;

public class ResourceManager {

    // Used as reference class for accessing JAR resources.
    private static final ResourceManager instance = new ResourceManager();

    public static final BufferedImage multiFrame = getComponent("mcard.jpg");
    public static final BufferedImage artifactFrame = getComponent("acard.jpg");
    public static final BufferedImage colorlessFrame = getComponent("ccard.jpg");
    public static final BufferedImage redFrame = getComponent("rcard.jpg");
    public static final BufferedImage blackFrame = getComponent("bcard.jpg");
    public static final BufferedImage blueFrame = getComponent("ucard.jpg");
    public static final BufferedImage greenFrame = getComponent("gcard.jpg");
    public static final BufferedImage whiteFrame = getComponent("wcard.jpg");
    public static final BufferedImage landFrame = getComponent("lcard.jpg");
    public static final BufferedImage vehicleFrame = getComponent("vcard.jpg");
    public static final BufferedImage redPTPanel = getComponent("rpt.png");
    public static final BufferedImage blackPTPanel = getComponent("bpt.png");
    public static final BufferedImage bluePTPanel = getComponent("upt.png");
    public static final BufferedImage greenPTPanel = getComponent("gpt.png");
    public static final BufferedImage whitePTPanel = getComponent("wpt.png");
    public static final BufferedImage colorlessPTPanel = getComponent("cpt.png");
    public static final BufferedImage artifactPTPanel = getComponent("apt.png");
    public static final BufferedImage multiPTPanel = getComponent("mpt.png");
    public static final BufferedImage vehiclePTPanel = getComponent("vpt.png");
    public static final BufferedImage gainColorBlend = getComponent("color_blend.png");
    public static final BufferedImage gainHybridBlend = getComponent("blendingMask.png");
    public static final BufferedImage gainHybridBanner = getComponent("hybrid_blend.png");
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
    public static final BufferedImage whiteLandImage = getComponent("watermark/watermark_w.png");
    public static final BufferedImage blueLandImage = getComponent("watermark/watermark_u.png");
    public static final BufferedImage blackLandImage = getComponent("watermark/watermark_b.png");
    public static final BufferedImage redLandImage = getComponent("watermark/watermark_r.png");
    public static final BufferedImage greenLandImage = getComponent("watermark/watermark_g.png");
    public static final BufferedImage bgLandImage = getComponent("watermark/watermark_bg.png");
    public static final BufferedImage brLandImage = getComponent("watermark/watermark_br.png");
    public static final BufferedImage guLandImage = getComponent("watermark/watermark_gu.png");
    public static final BufferedImage gwLandImage = getComponent("watermark/watermark_gw.png");
    public static final BufferedImage rgLandImage = getComponent("watermark/watermark_rg.png");
    public static final BufferedImage rwLandImage = getComponent("watermark/watermark_rw.png");
    public static final BufferedImage ubLandImage = getComponent("watermark/watermark_ub.png");
    public static final BufferedImage urLandImage = getComponent("watermark/watermark_ur.png");
    public static final BufferedImage wbLandImage = getComponent("watermark/watermark_wb.png");
    public static final BufferedImage wuLandImage = getComponent("watermark/watermark_wu.png");
    public static final BufferedImage artifactNyx = getComponent("nyx/acard.jpg");
    public static final BufferedImage colorlessNyx = getComponent("nyx/ccard.jpg");
    public static final BufferedImage landNyx = getComponent("nyx/clcard.jpg");
    public static final BufferedImage whiteNyx = getComponent("nyx/wcard.jpg");
    public static final BufferedImage blueNyx = getComponent("nyx/ucard.jpg");
    public static final BufferedImage blackNyx = getComponent("nyx/bcard.jpg");
    public static final BufferedImage redNyx = getComponent("nyx/rcard.jpg");
    public static final BufferedImage greenNyx = getComponent("nyx/gcard.jpg");
    public static final BufferedImage multiNyx = getComponent("nyx/mcard.jpg");
    public static final BufferedImage colorlessTokenFrame = getComponent("token/ccard.jpg");
    public static final BufferedImage colorlessTokenFrameText = getComponent("token/ccard2.jpg");
    public static final BufferedImage artifactTokenFrame = getComponent("token/acard.jpg");
    public static final BufferedImage artifactTokenFrameText = getComponent("token/acard2.jpg");
    public static final BufferedImage whiteTokenFrame = getComponent("token/wcard.jpg");
    public static final BufferedImage whiteTokenFrameText = getComponent("token/wcard2.jpg");
    public static final BufferedImage blueTokenFrame = getComponent("token/ucard.jpg");
    public static final BufferedImage blueTokenFrameText = getComponent("token/ucard2.jpg");
    public static final BufferedImage blackTokenFrame = getComponent("token/bcard.jpg");
    public static final BufferedImage blackTokenFrameText = getComponent("token/bcard2.jpg");
    public static final BufferedImage redTokenFrame = getComponent("token/rcard.jpg");
    public static final BufferedImage redTokenFrameText = getComponent("token/rcard2.jpg");
    public static final BufferedImage greenTokenFrame = getComponent("token/gcard.jpg");
    public static final BufferedImage greenTokenFrameText = getComponent("token/gcard2.jpg");
    public static final BufferedImage multiTokenFrame = getComponent("token/mcard.jpg");
    public static final BufferedImage multiTokenFrameText = getComponent("token/mcard2.jpg");
    public static final BufferedImage gainColorTokenBlend = getComponent("token/color_blend.png");
    public static final BufferedImage gainColorTokenBlendText = getComponent("token/color_blend2.png");
    public static final BufferedImage gainTokenBanner = getComponent("token/hybrid_blend.png");
    public static final BufferedImage gainTokenBannerText = getComponent("token/hybrid_blend2.png");
    public static final BufferedImage tokenImageBlend = getComponent("token/imagemask.png");
    public static final BufferedImage tokenImageBlendText = getComponent("token/imagemask2.png");
    public static final BufferedImage artifactLevellerBox = getComponent("leveller/aLeveller.jpg");
    public static final BufferedImage whiteLevellerBox = getComponent("leveller/wLeveller.jpg");
    public static final BufferedImage whiteLandLevellerBox = getComponent("leveller/wlLeveller.jpg");
    public static final BufferedImage blueLevellerBox = getComponent("leveller/uLeveller.jpg");
    public static final BufferedImage blueLandLevellerBox = getComponent("leveller/ulLeveller.jpg");
    public static final BufferedImage blackLevellerBox = getComponent("leveller/bLeveller.jpg");
    public static final BufferedImage blackLandLevellerBox = getComponent("leveller/blLeveller.jpg");
    public static final BufferedImage redLevellerBox = getComponent("leveller/rLeveller.jpg");
    public static final BufferedImage redLandLevellerBox = getComponent("leveller/rlLeveller.jpg");
    public static final BufferedImage greenLevellerBox = getComponent("leveller/gLeveller.jpg");
    public static final BufferedImage greenLandLevellerBox = getComponent("leveller/glLeveller.jpg");
    public static final BufferedImage gainTextBoxHybridBlend = getComponent("leveller/blendingMaskBox.png");
    public static final BufferedImage multiLevellerBox = getComponent("leveller/mLeveller.jpg");
    public static final BufferedImage multiLandLevellerBox = getComponent("leveller/mlLeveller.jpg");
    public static final BufferedImage colorlessLevellerBox = getComponent("leveller/cLeveller.jpg");
    public static final BufferedImage colorlessLandLevellerBox = getComponent("leveller/clLeveller.jpg");
    public static final BufferedImage artifactPlaneswalkerFrame = getComponent("planeswalker/acard.jpg");
    public static final BufferedImage whitePlaneswalkerFrame = getComponent("planeswalker/wcard.jpg");
    public static final BufferedImage bluePlaneswalkerFrame = getComponent("planeswalker/ucard.jpg");
    public static final BufferedImage blackPlaneswalkerFrame = getComponent("planeswalker/bcard.jpg");
    public static final BufferedImage redPlaneswalkerFrame = getComponent("planeswalker/rcard.jpg");
    public static final BufferedImage greenPlaneswalkerFrame = getComponent("planeswalker/gcard.jpg");
    public static final BufferedImage multiPlaneswalkerFrame = getComponent("planeswalker/mcard.jpg");
    public static final BufferedImage colorlessPlaneswalkerFrame = getComponent("planeswalker/clear.jpg");
    public static final BufferedImage gainPlaneswalkerHybridBanner = getComponent("planeswalker/hybrid_blend.png");
    public static final BufferedImage gainPlaneswalkerColorBlend = getComponent("planeswalker/color_blend.png");
    public static final BufferedImage artifactPlaneswalker4 = getComponent("planeswalker/acard2.jpg");
    public static final BufferedImage whitePlaneswalker4 = getComponent("planeswalker/wcard2.jpg");
    public static final BufferedImage bluePlaneswalker4 = getComponent("planeswalker/ucard2.jpg");
    public static final BufferedImage blackPlaneswalker4 = getComponent("planeswalker/bcard2.jpg");
    public static final BufferedImage redPlaneswalker4 = getComponent("planeswalker/rcard2.jpg");
    public static final BufferedImage greenPlaneswalker4 = getComponent("planeswalker/gcard2.jpg");
    public static final BufferedImage multiPlaneswalker4 = getComponent("planeswalker/mcard2.jpg");
    public static final BufferedImage colorlessPlaneswalker4 = getComponent("planeswalker/clear2.jpg");
    public static final BufferedImage gainPlaneswalker4HybridBanner = getComponent("planeswalker/hybrid_blend2.png");
    public static final BufferedImage gainPlaneswalker4ColorBlend = getComponent("planeswalker/color_blend2.png");
    public static final BufferedImage loyaltyPanel = getComponent("planeswalker/loyalty.png");
    public static final BufferedImage loyaltyUp = getComponent("planeswalker/loyaltyup.png");
    public static final BufferedImage loyaltyDown = getComponent("planeswalker/loyaltydown.png");
    public static final BufferedImage loyaltyEven = getComponent("planeswalker/loyaltynaught.png");
    public static final BufferedImage whiteDevoidFrame = getComponent("devoid/wcard.png");
    public static final BufferedImage blueDevoidFrame = getComponent("devoid/ucard.png");
    public static final BufferedImage blackDevoidFrame = getComponent("devoid/bcard.png");
    public static final BufferedImage redDevoidFrame = getComponent("devoid/rcard.png");
    public static final BufferedImage greenDevoidFrame = getComponent("devoid/gcard.png");
    public static final BufferedImage colorlessDevoidFrame = getComponent("devoid/ccard.png");
    public static final BufferedImage artifactDevoidFrame = getComponent("devoid/acard.png");
    public static final BufferedImage multiDevoidFrame = getComponent("devoid/mcard.png");
    public static final BufferedImage artifactSymbol = getImage("artifactSymbol.png");
    public static final BufferedImage creatureSymbol = getImage("creatureSymbol.png");
    public static final BufferedImage enchantmentSymbol = getImage("enchantmentSymbol.png");
    public static final BufferedImage instantSymbol = getImage("instantSymbol.png");
    public static final BufferedImage landSymbol = getImage("landSymbol.png");
    public static final BufferedImage multiSymbol = getImage("multiSymbol.png");
    public static final BufferedImage planeswalkerSymbol = getImage("planeswalkerSymbol.png");
    public static final BufferedImage sorcerySymbol = getImage("sorcerySymbol.png");
    public static final BufferedImage magarenaSymbol = getImage("magarenaSymbol.png");
    public static final BufferedImage artifactTransform = getComponent("transform/acard.jpg");
    public static final BufferedImage artifactHidden = getComponent("transform/acard2.jpg");
    public static final BufferedImage colorlessTransform = getComponent("transform/ccard.jpg");
    public static final BufferedImage colorlessHidden = getComponent("transform/ccard2.jpg");
    public static final BufferedImage colorlessLandTransform = getComponent("transform/clcard.jpg");
    public static final BufferedImage colorlessLandHidden = getComponent("transform/clcard2.jpg");
    public static final BufferedImage whiteTransform = getComponent("transform/wcard.jpg");
    public static final BufferedImage whiteHidden = getComponent("transform/wcard2.jpg");
    public static final BufferedImage whiteLandTransform = getComponent("transform/wlcard.jpg");
    public static final BufferedImage whiteLandHidden = getComponent("transform/wlcard2.jpg");
    public static final BufferedImage blueTransform = getComponent("transform/ucard.jpg");
    public static final BufferedImage blueHidden = getComponent("transform/ucard2.jpg");
    public static final BufferedImage blueLandTransform = getComponent("transform/ulcard.jpg");
    public static final BufferedImage blueLandHidden = getComponent("transform/ulcard2.jpg");
    public static final BufferedImage blackTransform = getComponent("transform/bcard.jpg");
    public static final BufferedImage blackHidden = getComponent("transform/bcard2.jpg");
    public static final BufferedImage blackLandTransform = getComponent("transform/blcard.jpg");
    public static final BufferedImage blackLandHidden = getComponent("transform/blcard2.jpg");
    public static final BufferedImage redTransform = getComponent("transform/rcard.jpg");
    public static final BufferedImage redHidden = getComponent("transform/rcard2.jpg");
    public static final BufferedImage redLandTransform = getComponent("transform/rlcard.jpg");
    public static final BufferedImage redLandHidden = getComponent("transform/rlcard2.jpg");
    public static final BufferedImage greenTransform = getComponent("transform/gcard.jpg");
    public static final BufferedImage greenHidden = getComponent("transform/gcard2.jpg");
    public static final BufferedImage greenLandTransform = getComponent("transform/glcard.jpg");
    public static final BufferedImage greenLandHidden = getComponent("transform/glcard2.jpg");
    public static final BufferedImage multiTransform = getComponent("transform/mcard.jpg");
    public static final BufferedImage multiHidden = getComponent("transform/mcard2.jpg");
    public static final BufferedImage multiLandTransform = getComponent("transform/mlcard.jpg");
    public static final BufferedImage multiLandHidden = getComponent("transform/mlcard2.jpg");
    public static final BufferedImage gainTransformColorBlend = getComponent("transform/color_blend.png");
    public static final BufferedImage gainTransformHybridBanner = getComponent("transform/hybrid_blend.png");
    public static final BufferedImage artifactPlaneswalkerTransform = getComponent("transform/planeswalker/acard.jpg");
    public static final BufferedImage artifactPlaneswalkerHidden = getComponent("transform/planeswalker/acard2.jpg");
    public static final BufferedImage whitePlaneswalkerTransform = getComponent("transform/planeswalker/wcard.jpg");
    public static final BufferedImage whitePlaneswalkerHidden = getComponent("transform/planeswalker/wcard2.jpg");
    public static final BufferedImage bluePlaneswalkerTransform = getComponent("transform/planeswalker/ucard.jpg");
    public static final BufferedImage bluePlaneswalkerHidden = getComponent("transform/planeswalker/ucard2.jpg");
    public static final BufferedImage blackPlaneswalkerTransform = getComponent("transform/planeswalker/bcard.jpg");
    public static final BufferedImage blackPlaneswalkerHidden = getComponent("transform/planeswalker/bcard2.jpg");
    public static final BufferedImage redPlaneswalkerTransform = getComponent("transform/planeswalker/rcard.jpg");
    public static final BufferedImage redPlaneswalkerHidden = getComponent("transform/planeswalker/rcard2.jpg");
    public static final BufferedImage greenPlaneswalkerTransform = getComponent("transform/planeswalker/gcard.jpg");
    public static final BufferedImage greenPlaneswalkerHidden = getComponent("transform/planeswalker/gcard2.jpg");
    public static final BufferedImage multiPlaneswalkerTransform = getComponent("transform/planeswalker/mcard.jpg");
    public static final BufferedImage multiPlaneswalkerHidden = getComponent("transform/planeswalker/mcard2.jpg");
    public static final BufferedImage gainPlaneswalkerTransformHybridBanner = getComponent("transform/planeswalker/hybrid_blend.png");
    public static final BufferedImage getPlaneswalkerImageBlend = getComponent("planeswalker/imageMask.png");
    public static final BufferedImage artifactHiddenPTPanel = getComponent("transform/apt2.png");
    public static final BufferedImage colorlessHiddenPTPanel = getComponent("transform/cpt2.png");
    public static final BufferedImage whiteHiddenPTPanel = getComponent("transform/wpt2.png");
    public static final BufferedImage blueHiddenPTPanel = getComponent("transform/upt2.png");
    public static final BufferedImage blackHiddenPTPanel = getComponent("transform/bpt2.png");
    public static final BufferedImage redHiddenPTPanel = getComponent("transform/rpt2.png");
    public static final BufferedImage greenHiddenPTPanel = getComponent("transform/gpt2.png");
    public static final BufferedImage multiHiddenPTPanel = getComponent("transform/mpt2.png");
    public static final BufferedImage daySymbol = getComponent("transform/sun_circle.png");
    public static final BufferedImage nightSymbol = getComponent("transform/night_circle.png");
    public static final BufferedImage moonSymbol = getComponent("transform/moon.png");
    public static final BufferedImage eldraziSymbol = getComponent("transform/eldrazi.png");
    public static final BufferedImage planeswalkerTypeSymbol = getComponent("transform/planeswalker.png");
    public static final BufferedImage sparkSymbol = getComponent("transform/ccorner.png");
    public static final BufferedImage common = getComponent("rarity/Common.png");
    public static final BufferedImage uncommon = getComponent("rarity/Uncommon.png");
    public static final BufferedImage rare = getComponent("rarity/Rare.png");
    public static final BufferedImage mythic = getComponent("rarity/Mythic.png");
    public static final BufferedImage artifactMiracle = getComponent("miracle/aMiracleOverlay.png");
    public static final BufferedImage blackMiracle = getComponent("miracle/bMiracleOverlay.png");
    public static final BufferedImage colorlessMiracle = getComponent("miracle/cMiracleOverlay.png");
    public static final BufferedImage multiMiracle = getComponent("miracle/mMiracleOverlay.png");
    public static final BufferedImage redMiracle = getComponent("miracle/rMiracleOverlay.png");
    public static final BufferedImage blueMiracle = getComponent("miracle/uMiracleOverlay.png");
    public static final BufferedImage whiteMiracle = getComponent("miracle/wMiracleOverlay.png");
    public static final BufferedImage greenMiracle = getComponent("miracle/gMiracleOverlay.png");
    public static final BufferedImage blackLandBox = getComponent("land/bLandBox.jpg");
    public static final BufferedImage blueLandBox = getComponent("land/uLandBox.jpg");
    public static final BufferedImage redLandBox = getComponent("land/rLandBox.jpg");
    public static final BufferedImage greenLandBox = getComponent("land/gLandBox.jpg");
    public static final BufferedImage whiteLandBox = getComponent("land/wLandBox.jpg");
    public static final BufferedImage multiLandBox = getComponent("land/mLandBox.jpg");
    public static final BufferedImage tokenImageMaskSmall = getComponent("token/imagemask2.png");
    public static final BufferedImage tokenImageMaskLarge = getComponent("token/imagemask.png");

    //Font Map
    private static final String[] names = {};
    private static final Map<String, Font> fontCache = new ConcurrentHashMap<>(names.length);
    private static final String FRAMES_FOLDER = "/cardbuilder/frames/";

    static {
        for (String name : names) {
            fontCache.put(name, getFont(name));
        }
    }

    private static InputStream getJarResourceStream(String filename) {
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

    private static BufferedImage getComponent(String imageName) {
        String fName = FRAMES_FOLDER + imageName;
        try (final InputStream is = getJarResourceStream(fName)) {
            return ImageHelper.getOptimizedImage(ImageIO.read(is));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static BufferedImage getImage(String name) {
        String fName = "/cardbuilder/images/" + name;
        try (final InputStream is = getJarResourceStream(fName)) {
            return ImageHelper.getOptimizedImage(ImageIO.read(is));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static BufferedImage newFrame(BufferedImage bi) {
        return ImageHelper.getOptimizedImage(bi);
    }

}
