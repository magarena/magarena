package magic.ui.cardBuilder.renderers;

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

import magic.model.MagicAbility;
import magic.model.MagicColor;
import magic.model.MagicType;
import magic.ui.cardBuilder.IRenderableCard;
import magic.ui.cardBuilder.ResourceManager;

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
            g2d.setColor(Color.BLACK);
            FontRenderContext frc2 = g2d.getFontRenderContext();

            g2d.drawImage(ptImage, 273, 466, null);

            //draw ptText
            Rectangle2D box = new Rectangle(286, 469, 60, 28); //ptText dimensions (Can't use ptPanel due to shadow distorting size)
            Point centre = new Point((int) box.getCenterX(), (int) box.getCenterY()); //Centre of box


            TextLayout layout;
            if (ptText.length() >= 6) { //power or toughness of 100+
                layout = new TextLayout(ptText, cardPTFontSmall, frc2);
            } else {
                layout = new TextLayout(ptText, cardPTFont, frc2);
            }
            Point textCentre = new Point((int) layout.getBounds().getWidth() / 2, (int) layout.getBounds().getHeight() / 2); //Centre of text

            layout.draw(g2d, (float) centre.getX() - (float) textCentre.getX(), (float) centre.getY() + (float) textCentre.getY());

            g2d.dispose();
        }
    }

    static void drawLoyaltyPanels(BufferedImage cardImage, IRenderableCard cardDef) {
        int xPos = 32;
        int width = 12;
        int height = 34;
        String loyaltyText = getLoyaltyText(cardDef);
        Graphics2D g2d = cardImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setColor(Color.WHITE);
        String panelText;

        // Draw main Loyalty Panel
        if (!loyaltyText.isEmpty()) {
            BufferedImage loyaltyImage = ResourceManager.loyaltyPanel;
            g2d.drawImage(loyaltyImage, 302, 460, null);
            drawLoyaltyPanelText(g2d, new Rectangle(326, 462, width, height), loyaltyText);
        }
        // Draw activation panels
        if (OracleText.getPlaneswalkerAbilityCount(cardDef) == 3) {
            String[] activations = getPlaneswalkerActivationCosts(cardDef);
            panelText = activations[0];
            if (panelText != "") {
                //Panel 1
                g2d.drawImage(getLoyaltyPanel(activations[0]), 18, 333, null);
                drawLoyaltyPanelText(g2d, new Rectangle(xPos, 335, width, height), panelText);
            }
            //Panel 2
            g2d.drawImage(getLoyaltyPanel(activations[1]), 18, 383, null);
            drawLoyaltyPanelText(g2d, new Rectangle(xPos, 386, width, height), activations[1]);
            //Panel 3
            g2d.drawImage(getLoyaltyPanel(activations[2]), 18, 432, null);
            drawLoyaltyPanelText(g2d, new Rectangle(xPos, 435, width, height), activations[2]);
        } else {
            String[] activations = getPlaneswalkerActivationCosts(cardDef);
            panelText = activations[0];
            if (panelText != "") {
                //Panel 1
                g2d.drawImage(getLoyaltyPanel(activations[0]), 18, 294, null);
                drawLoyaltyPanelText(g2d, new Rectangle(xPos, 297, width, height), panelText);
            }
            //Panel 2
            g2d.drawImage(getLoyaltyPanel(activations[1]), 18, 341, null);
            drawLoyaltyPanelText(g2d, new Rectangle(xPos, 344, width, height), activations[1]);
            //Panel 3
            g2d.drawImage(getLoyaltyPanel(activations[2]), 18, 388, null);
            drawLoyaltyPanelText(g2d, new Rectangle(xPos, 391, width, height), activations[2]);
            //Panel 4
            if (!activations[3].isEmpty()) {
                g2d.drawImage(getLoyaltyPanel(activations[3]), 18, 435, null);
                drawLoyaltyPanelText(g2d, new Rectangle(xPos, 438, width, height), activations[3]);
            }
        }
        g2d.dispose();
    }

    private static void drawLoyaltyPanelText(Graphics2D g2d, Rectangle box, String text) {
        TextLayout layout = new TextLayout(text, cardLoyaltyFont, g2d.getFontRenderContext());
        layout.draw(g2d, (float) box.getCenterX() - (float) layout.getBounds().getCenterX(), (float) box.getCenterY() - (float) layout.getBounds().getCenterY());
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
        return cardDef.hasType(MagicType.Creature) ? cardDef.getPowerToughnessText() : "";
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
            Point centre = new Point((int) box.getCenterX(), (int) box.getCenterY()); //Centre of box


            TextLayout layout;
            if (ptText.length() >= 6) { //power or toughness of 100+
                layout = new TextLayout(ptText, cardPTFontSmall, frc2);
            } else {
                layout = new TextLayout(ptText, cardPTFont, frc2);
            }
            Point textCentre = new Point((int) layout.getBounds().getWidth() / 2, (int) layout.getBounds().getHeight() / 2); //Centre of text

            layout.draw(g2d, (float) centre.getX() - (float) textCentre.getX(), (float) centre.getY() + (float) textCentre.getY());

            g2d.dispose();
        }
    }

    static void drawTransformSymbol(BufferedImage cardImage, IRenderableCard cardDef) {
        Graphics2D g2d = cardImage.createGraphics();
        if (cardDef.isHidden()) {
            BufferedImage typeSymbol = ResourceManager.moonSymbol;
            g2d.drawImage(typeSymbol, 19, 25, null);
        } else {
            BufferedImage typeSymbol = ResourceManager.sunSymbol;
            g2d.drawImage(typeSymbol, 19, 25, null);
        }

        g2d.dispose();
    }
}
