package magic.ui.cardBuilder.renderers;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import magic.ui.cardBuilder.IRenderableCard;
import magic.model.MagicType;
import magic.ui.cardBuilder.ResourceManager;

public class TypeLine {
    private TypeLine() {
    }

    private static final Font cardTypeFont = ResourceManager.getFont("Beleren-Bold.ttf").deriveFont(Font.PLAIN, 16);
    private static final Font cardTypeFontSmall = ResourceManager.getFont("Beleren-Bold.ttf").deriveFont(Font.PLAIN, 15);
    private static final Font cardTypeFontVerySmall = ResourceManager.getFont("Beleren-Bold.ttf").deriveFont(Font.PLAIN, 14);
    private static final Font cardTypeFontSmallest = ResourceManager.getFont("Beleren-Bold.ttf").deriveFont(Font.PLAIN, 13);
    private static int padding;

    static void drawCardTypeLine(BufferedImage cardImage, IRenderableCard cardDef) {
        String cardType = getTypeLine(cardDef);
        if (!cardType.isEmpty()) {
            Graphics2D g2d = cardImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2d.setColor(Color.BLACK);
            g2d.setFont(getFontSize(cardType));
            FontMetrics metrics = g2d.getFontMetrics();
            int yPos;
            if (cardDef.isToken()) {
                yPos = cardDef.hasText() ? 356 : 431;
            } else {
                yPos = 298;
            }
            g2d.drawString(cardType, 32, yPos + metrics.getAscent() + padding);// 298+Plus height of text
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
        if (cardDef.hasType(MagicType.Tribal)){
            typeLine.append("Tribal ");
        }
        if (cardDef.isToken()) {
            typeLine.append("Token ");
        }
        MagicType.ALL_CARD_TYPES.stream().filter(cardDef::hasType).forEach(aType -> {
            if (aType != MagicType.Tribal) {
                typeLine.append(aType).append(" ");
            }
        });
        if (!subtype.isEmpty()) {
            typeLine.append("— ");
            typeLine.append(subtype.replaceAll(",", ""));
        }
        return typeLine.toString();
    }


    public static void drawHiddenCardTypeLine(BufferedImage cardImage, IRenderableCard cardDef) {
        String cardType = getTypeLine(cardDef);
        if (!cardType.isEmpty()) {
            Graphics2D g2d = cardImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2d.setColor(Color.WHITE);
            g2d.setFont(getFontSize(cardType));
            FontMetrics metrics = g2d.getFontMetrics();
            int yPos;
            if (cardDef.isToken()) {
                yPos = cardDef.hasText() ? 356 : 431;
            } else {
                yPos = 298;
            }
            g2d.drawString(cardType, 32, yPos + metrics.getAscent() + padding);// 298+Plus height of text
            g2d.dispose();
        }
    }
}
