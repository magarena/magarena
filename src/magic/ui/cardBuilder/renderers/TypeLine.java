package magic.ui.cardBuilder.renderers;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import magic.model.MagicType;
import magic.ui.cardBuilder.IRenderableCard;
import magic.ui.cardBuilder.ResourceManager;

public class TypeLine {
    private static final Font cardTypeFont = ResourceManager.getFont("JaceBeleren-Bold.ttf").deriveFont(Font.PLAIN, 16);
    private static final Font cardTypeFontSmall = ResourceManager.getFont("JaceBeleren-Bold.ttf").deriveFont(Font.PLAIN, 15);
    private static final Font cardTypeFontVerySmall = ResourceManager.getFont("JaceBeleren-Bold.ttf").deriveFont(Font.PLAIN, 14);
    private static final Font cardTypeFontSmallest = ResourceManager.getFont("JaceBeleren-Bold.ttf").deriveFont(Font.PLAIN, 13);
    private static int padding;

    static void drawCardTypeLine(BufferedImage cardImage, IRenderableCard cardDef) {
        String cardType = getTypeLine(cardDef);
        if (!cardType.isEmpty()) {
            Graphics2D g2d = cardImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2d.setColor(cardDef.isHidden() ? Color.WHITE : Color.BLACK);
            g2d.setFont(getFontSize(cardType));
            FontMetrics metrics = g2d.getFontMetrics();
            int yPos;
            if (cardDef.isToken()) {
                yPos = cardDef.hasText() ? 359 : 434;
            } else if (cardDef.isPlaneswalker() && OracleText.getPlaneswalkerAbilityCount(cardDef) == 4) {
                yPos = 266;
            } else {
                yPos = 301;
            }
            g2d.drawString(cardType + " ", 32, yPos + metrics.getAscent() + padding);
            g2d.dispose();
        }
    }

    private static Font getFontSize(CharSequence cardType) {
        if (cardType.length() > 46) {
            padding = 3;
            return cardTypeFontSmallest;
        }
        if (cardType.length() > 43) {
            padding = 2;
            return cardTypeFontVerySmall;

        }
        if (cardType.length() > 40) {
            padding = 2;
            return cardTypeFontSmall;
        }
        padding = 1;
        return cardTypeFont;
    }

    public static String getTypeLine(IRenderableCard cardDef) {
        StringBuilder typeLine = new StringBuilder();
        String subtype = cardDef.getSubTypeText();
        MagicType.SUPERTYPES.stream().filter(cardDef::hasType).forEach(aSuperType -> {
            typeLine.append(aSuperType).append(" ");
        });
        if (cardDef.isToken()) {
            typeLine.append("Token ");
        }
        MagicType.TYPE_ORDER.stream().filter(cardDef::hasType).forEach(aType -> typeLine.append(aType).append(' '));
        if (!subtype.isEmpty()) {
            typeLine.append("— ");
            typeLine.append(subtype.replaceAll(",", ""));
        }
        return typeLine.toString();
    }

    public static void drawRarity(BufferedImage cardImage, IRenderableCard cardDef) {
        BufferedImage rarity = ResourceManager.common;
        if (cardDef.getRarityChar() == 'U') {
            rarity = ResourceManager.uncommon;
        }
        if (cardDef.getRarityChar() == 'R') {
            rarity = ResourceManager.rare;
        }
        if (cardDef.getRarityChar() == 'M') {
            rarity = ResourceManager.mythic;
        }
        Graphics2D g2d = cardImage.createGraphics();
        int ypos = 297;
        if (cardDef.isPlaneswalker() && OracleText.getPlaneswalkerAbilityCount(cardDef) > 3) {
            ypos = 263;
        }
        if (cardDef.isToken()) {
            ypos = cardDef.hasText() ? 356 : 431;
        }
        int xpos = 322;
        g2d.drawImage(rarity, xpos, ypos, null);
    }

}
