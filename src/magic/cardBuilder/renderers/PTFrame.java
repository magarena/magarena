package magic.cardBuilder.renderers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import magic.model.MagicAbility;
import magic.model.MagicColor;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.IRenderableCard;
import magic.cardBuilder.ResourceManager;

public class PTFrame {

    private static final Font cardPTFont = ResourceManager.getFont("Beleren-Bold.ttf").deriveFont(Font.PLAIN, 19);
    private static final Font cardPTFontSmall = ResourceManager.getFont("Beleren-Bold.ttf").deriveFont(Font.PLAIN, 15);//scale down when triple figures
    private static final Font cardLoyaltyFont = ResourceManager.getFont("Beleren-Bold.ttf").deriveFont(Font.PLAIN, 16);

    //draw ptPanel - The only layering requirement besides frame. Ability text must wrap around ptpanel intrusion if any.
    static void drawPTPanel(BufferedImage cardImage, IRenderableCard cardDef) {
        String ptText = getPTText(cardDef);

        if (!ptText.isEmpty()) {
            BufferedImage ptImage = getPTPanelImage(cardDef);
            Graphics2D g2d = cardImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            if (cardDef.hasSubType(MagicSubType.Vehicle)) {
                g2d.setColor(Color.WHITE);
            } else {
                g2d.setColor(Color.BLACK);
            }
            FontRenderContext frc2 = g2d.getFontRenderContext();

            g2d.drawImage(ptImage, 273, 466, null);

            //draw ptText
            Rectangle2D box = new Rectangle(286, 469, 60, 28); //ptText dimensions (Can't use ptPanel due to shadow distorting size)
            Point centre = new Point((int)box.getCenterX(), (int)box.getCenterY()); //Centre of box


            TextLayout layout;
            layout = new TextLayout(ptText, ptText.length() >= 6 ? cardPTFontSmall : cardPTFont, frc2);
            Point textCentre = new Point((int)layout.getBounds().getWidth() / 2, (int)layout.getBounds().getHeight() / 2); //Centre of text

            layout.draw(g2d, (float)centre.getX() - (float)textCentre.getX(), (float)centre.getY() + (float)textCentre.getY());

            g2d.dispose();
        }
    }

    static void drawLoyaltyPanels(BufferedImage cardImage, IRenderableCard cardDef) {
        String loyaltyText = getLoyaltyText(cardDef);
        Graphics2D g2d = cardImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setColor(Color.WHITE);

        // Draw main Loyalty Panel
        int width = 12;
        int height = 34;
        if (!loyaltyText.equals("0")) {
            BufferedImage loyaltyImage = ResourceManager.loyaltyPanel;
            g2d.drawImage(loyaltyImage, 302, 460, null);
            drawPanelText(g2d, new Rectangle(326, 462, width, height), loyaltyText, cardLoyaltyFont);
        }
        // Draw activation panels
        String panelText;
        int xPos = 32;
        if (OracleText.getPlaneswalkerAbilityCount(cardDef) <= 3) {
            String[] activations = getPlaneswalkerActivationCosts(cardDef);
            panelText = activations[0];
            if (!panelText.isEmpty()) {
                //Panel 1
                g2d.drawImage(getLoyaltyPanel(activations[0]), 18, 333, null);
                drawPanelText(g2d, new Rectangle(xPos, 335, width, height), panelText, cardLoyaltyFont);
            }
            //Panel 2
            g2d.drawImage(getLoyaltyPanel(activations[1]), 18, 383, null);
            drawPanelText(g2d, new Rectangle(xPos, 386, width, height), activations[1], cardLoyaltyFont);
            //Panel 3
            if (activations.length > 2) {
                g2d.drawImage(getLoyaltyPanel(activations[2]), 18, 432, null);
                drawPanelText(g2d, new Rectangle(xPos, 435, width, height), activations[2], cardLoyaltyFont);
            }
        } else {
            String[] activations = getPlaneswalkerActivationCosts(cardDef);
            panelText = activations[0];
            if (!panelText.isEmpty()) {
                //Panel 1
                g2d.drawImage(getLoyaltyPanel(activations[0]), 18, 294, null);
                drawPanelText(g2d, new Rectangle(xPos, 297, width, height), panelText, cardLoyaltyFont);
            }
            //Panel 2
            g2d.drawImage(getLoyaltyPanel(activations[1]), 18, 341, null);
            drawPanelText(g2d, new Rectangle(xPos, 344, width, height), activations[1], cardLoyaltyFont);
            //Panel 3
            g2d.drawImage(getLoyaltyPanel(activations[2]), 18, 388, null);
            drawPanelText(g2d, new Rectangle(xPos, 391, width, height), activations[2], cardLoyaltyFont);
            //Panel 4
            if (!activations[3].isEmpty()) {
                g2d.drawImage(getLoyaltyPanel(activations[3]), 18, 435, null);
                drawPanelText(g2d, new Rectangle(xPos, 438, width, height), activations[3], cardLoyaltyFont);
            }
        }
        g2d.dispose();
    }

    private static void drawPanelText(Graphics2D g2d, Rectangle box, String text, Font font) {
        TextLayout layout = new TextLayout(text, font, g2d.getFontRenderContext());
        layout.draw(g2d, (float)box.getCenterX() - (float)layout.getBounds().getCenterX(), (float)box.getCenterY() - (float)layout.getBounds().getCenterY());
    }

    private static BufferedImage getLoyaltyPanel(String activation) {
        if (activation.startsWith("+")) {
            return ResourceManager.loyaltyUp;
        } else if (activation.startsWith("0")) {
            return ResourceManager.loyaltyEven;
        } else {
            return ResourceManager.loyaltyDown;
        }
    }

    static void drawLevellerPTPanels(BufferedImage cardImage, IRenderableCard cardDef) {
        int xPosImage = 284;
        int xPosText = xPosImage + 13;
        int width = 60;
        int height = 28;
        String[] ptText = getLevellerPTText(cardDef);
        BufferedImage ptImage = getPTPanelImage(cardDef);
        Graphics2D g2d = cardImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setColor(Color.BLACK);

        //Panel 1
        g2d.drawImage(ptImage, xPosImage, 339, null);
        drawPanelText(g2d, new Rectangle(xPosText, 342, width, height), ptText[0], cardPTFont);
        //Panel 2
        g2d.drawImage(ptImage, xPosImage, 390, null);
        drawPanelText(g2d, new Rectangle(xPosText, 393, width, height), ptText[1], cardPTFont);
        //Panel 3
        g2d.drawImage(ptImage, xPosImage, 441, null);
        drawPanelText(g2d, new Rectangle(xPosText, 444, width, height), ptText[2], cardPTFont);

        g2d.dispose();
    }

    static void drawLevellerArrowText(BufferedImage cardImage, IRenderableCard cardDef) {
        String[] levelText = getLevellerArrowText(cardDef);
        Graphics2D g2d = cardImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setColor(Color.BLACK);

        //Arrow 1
        int xPosText = 35;
        int width = 39;
        int height = 39;
        OracleText.drawTextToCard(cardImage, xPosText, 386, levelText[0], new Rectangle(0, 0, width, height));
        //Arrow 2
        OracleText.drawTextToCard(cardImage, xPosText, 437, levelText[1], new Rectangle(0, 0, width, height));

        g2d.dispose();
    }

    private static String[] getLevellerPTText(IRenderableCard cardDef) {
        String[] abilities = OracleText.getOracleAsLines(cardDef);
        ArrayList<String> text = new ArrayList<>(3);
        text.add(getPTText(cardDef));
        for (String ability : abilities) {
            if (ability.matches("\\d+/\\d+")) {
                text.add(ability);
            }
        }
        return text.toArray(new String[3]);
    }

    private static String[] getLevellerArrowText(IRenderableCard cardDef) {
        String[] abilities = OracleText.getOracleAsLines(cardDef);
        ArrayList<String> text = new ArrayList<>(2);
        for (String ability : abilities) {
            if (ability.contains("LEVEL")) {
                text.add(ability.replace("LEVEL ", "LEVEL\n"));
            }
        }
        return text.toArray(new String[2]);
    }

    private static BufferedImage getPTPanel(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.whitePTPanel;
            case Blue:
                return ResourceManager.bluePTPanel;
            case Black:
                return ResourceManager.blackPTPanel;
            case Red:
                return ResourceManager.redPTPanel;
            case Green:
                return ResourceManager.greenPTPanel;
            default:
                return ResourceManager.colorlessPTPanel;
        }
    }

    private static BufferedImage getHiddenPTPanel(MagicColor color) {
        switch (color) {
            case White:
                return ResourceManager.whiteHiddenPTPanel;
            case Blue:
                return ResourceManager.blueHiddenPTPanel;
            case Black:
                return ResourceManager.blackHiddenPTPanel;
            case Red:
                return ResourceManager.redHiddenPTPanel;
            case Green:
                return ResourceManager.greenHiddenPTPanel;
            default:
                return ResourceManager.colorlessHiddenPTPanel;
        }
    }

    private static BufferedImage getPTPanelImage(IRenderableCard cardDef) {
        if (cardDef.hasAbility(MagicAbility.Devoid)) {
            return ResourceManager.colorlessPTPanel;
        }
        if (cardDef.hasSubType(MagicSubType.Vehicle)) {
            return ResourceManager.vehiclePTPanel;
        }
        //Hybrid cards use colorless PT panel and banners
        if (cardDef.isMulti()) {
            return cardDef.isHybrid() || cardDef.isToken() && cardDef.getNumColors() == 2 ? ResourceManager.colorlessPTPanel : ResourceManager.multiPTPanel;
        }
        for (MagicColor color : MagicColor.values()) {
            if (cardDef.hasColor(color)) {
                return getPTPanel(color);
            }
        }
        if (cardDef.hasType(MagicType.Artifact)) {
            return ResourceManager.artifactPTPanel;
        }
        return ResourceManager.colorlessPTPanel;
    }

    private static BufferedImage getHiddenPTPanelImage(IRenderableCard cardDef) {
        if (cardDef.hasAbility(MagicAbility.Devoid)) {
            return ResourceManager.colorlessHiddenPTPanel;
        }
        //Hybrid cards use colorless PT panel and banners
        if (cardDef.isMulti()) {
            return cardDef.isHybrid() || cardDef.isToken() && cardDef.getNumColors() == 2 ? ResourceManager.colorlessHiddenPTPanel : ResourceManager.multiHiddenPTPanel;
        }
        for (MagicColor color : MagicColor.values()) {
            if (cardDef.hasColor(color)) {
                return getHiddenPTPanel(color);
            }
        }
        if (cardDef.hasType(MagicType.Artifact)) {
            return ResourceManager.artifactHiddenPTPanel;
        }
        return ResourceManager.colorlessHiddenPTPanel;
    }

    private static String getPTText(IRenderableCard cardDef) {
        return cardDef.hasType(MagicType.Creature) || cardDef.hasSubType(MagicSubType.Vehicle) ? cardDef.getPowerToughnessText() : "";
    }

    private static String getLoyaltyText(IRenderableCard cardDef) {
        return cardDef.hasType(MagicType.Planeswalker) ? Integer.toString(cardDef.getStartingLoyalty()) : "";
    }

    static String[] getPlaneswalkerActivationCosts(IRenderableCard cardDef) {
        String[] abilities = OracleText.getOracleAsLines(cardDef);
        String[] costs = new String[abilities.length];
        for (int i = 0; i < abilities.length; i++) {
            String[] parts = abilities[i].split(": ", 2);
            costs[i] = parts.length == 2 ? parts[0].replace("\u2212", "-") : "";
        }
        return costs;
    }

    static void drawHiddenPTPanel(BufferedImage cardImage, IRenderableCard cardDef) {
        String ptText = getPTText(cardDef);

        if (!ptText.isEmpty()) {
            BufferedImage ptImage = getHiddenPTPanelImage(cardDef);
            Graphics2D g2d = cardImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2d.setColor(Color.WHITE);
            FontRenderContext frc2 = g2d.getFontRenderContext();

            g2d.drawImage(ptImage, 273, 466, null);

            //draw ptText
            Rectangle2D box = new Rectangle(286, 469, 60, 28); //ptText dimensions (Can't use ptPanel due to shadow distorting size)
            Point centre = new Point((int)box.getCenterX(), (int)box.getCenterY()); //Centre of box

            TextLayout layout = new TextLayout(ptText, ptText.length() >= 6 ? cardPTFontSmall : cardPTFont, frc2); //Power or Toughness over 99
            Point textCentre = new Point((int)layout.getBounds().getWidth() / 2, (int)layout.getBounds().getHeight() / 2); //Centre of text

            layout.draw(g2d, (float)centre.getX() - (float)textCentre.getX(), (float)centre.getY() + (float)textCentre.getY());

            g2d.dispose();
        }
    }

    static void drawTransformSymbol(BufferedImage cardImage, IRenderableCard cardDef) {
        Graphics2D g2d = cardImage.createGraphics();
        BufferedImage typeSymbol = ResourceManager.daySymbol;
        if (cardDef.isHidden()) {
            if (cardDef.hasSubType(MagicSubType.Eldrazi)){
                typeSymbol = ResourceManager.eldraziSymbol;
            }
            else if (cardDef.isPlaneswalker() && !cardDef.getTransformedDefinition().isPlaneswalker()){
                typeSymbol = ResourceManager.planeswalkerTypeSymbol;
            } else {
                typeSymbol = ResourceManager.nightSymbol;
            }
        } else if (cardDef.isCreature() && cardDef.getTransformedDefinition().isPlaneswalker()) {
            typeSymbol = ResourceManager.sparkSymbol;
        } else if (cardDef.getTransformedDefinition().hasSubType(MagicSubType.Eldrazi)) {
            typeSymbol = ResourceManager.moonSymbol;
        }
        if (cardDef.isPlaneswalker()) {
            g2d.drawImage(typeSymbol, 21, 18, null);
        } else {
            g2d.drawImage(typeSymbol, 19, 25, null);
        }
        g2d.dispose();
    }
}
