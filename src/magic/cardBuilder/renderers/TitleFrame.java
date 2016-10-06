package magic.cardBuilder.renderers;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.text.AttributedString;
import java.util.List;
import javax.swing.ImageIcon;

import magic.data.MagicIcon;
import magic.model.MagicType;
import magic.ui.MagicImages;
import magic.model.IRenderableCard;
import magic.cardBuilder.ResourceManager;

public class TitleFrame {

    private static final Font cardNameFont = ResourceManager.getFont("JaceBeleren-Bold.ttf").deriveFont(Font.PLAIN, 19);
    private static final Font cardNameTokenFont = ResourceManager.getFont("Beleren Small Caps.ttf").deriveFont(Font.PLAIN, 20);

    static void drawManaCost(BufferedImage cardImage, IRenderableCard cardDef) {
        List<MagicIcon> manaCost = getManaCost(cardDef);
        if (manaCost != null) {
            int posY = 33;
            if (cardDef.isPlaneswalker()) {
                posY = 25;
            }
            Graphics2D g2d = cardImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.BLACK);
            //draw casting Cost + Shadow
            int posX = 347;
            Point start = new Point(posX, posY); //Top right of card shifted for size and shadow
            int x = (int) start.getX();
            int y = (int) start.getY();
            //in reverse order
            for (int i = manaCost.size() - 1; i >= 0; i--) {
                ImageIcon mana = MagicImages.getBigManaIcon(manaCost.get(i));
                Image manaSymbol = resizeMana(mana);
                x -= manaSymbol.getWidth(null);
                int size = manaSymbol.getWidth(null) - 2; //shadow size
                Shape shadow = new Ellipse2D.Double(x, y, size, size);
                g2d.fill(shadow);
                g2d.drawImage(manaSymbol, x, y - 2, null); //shift for shadow
            }
            g2d.dispose();
        }
    }

    private static List<MagicIcon> getManaCost(IRenderableCard cardDef) {
        return cardDef.isDoubleFaced() && cardDef.isHidden() || cardDef.hasType(MagicType.Land) ? null : cardDef.getCost().getIcons();
    }

    static void drawCardName(BufferedImage cardImage, IRenderableCard cardDef) {
        String plainName = cardDef.getName();
        if (!plainName.isEmpty()) {
            //Add space to end of name to enable .end character ligatures
            AttributedString cardName = new AttributedString(plainName+" ");
            Graphics2D g2d = cardImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            if (cardDef.isToken()) {
                g2d.setColor(Color.getHSBColor(54, 45, 100));
                cardName.addAttribute(TextAttribute.FONT, cardNameTokenFont);
            } else {
                g2d.setColor(Color.BLACK);
                cardName.addAttribute(TextAttribute.FONT, cardNameFont);
            }
            TextLayout metrics = new TextLayout(cardName.getIterator(), g2d.getFontRenderContext()); //to allow calculation of Ascent + length
            int xPos = 30;
            int yPos = 33;
            if (cardDef.isToken()) {
                xPos = (int) (cardImage.getWidth() / 2 - metrics.getBounds().getWidth() / 2);
                yPos = 27;
            } else if (cardDef.isPlaneswalker()) {
                yPos = 25;
            }
            g2d.drawString(cardName.getIterator(), xPos, yPos + metrics.getAscent());
            g2d.dispose();
        }
    }

    static void drawTransformCardName(BufferedImage cardImage, IRenderableCard cardDef) {
        String cardName = cardDef.getName();
        if (!cardName.isEmpty()) {
            Graphics2D g2d = cardImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            if (cardDef.isHidden()) {
                g2d.setColor(Color.WHITE);
            } else {
                g2d.setColor(Color.BLACK);
            }
            g2d.setFont(cardNameFont);
            FontMetrics metrics = g2d.getFontMetrics(); //to allow calculation of Ascent + length
            int yPos = 32;
            if (cardDef.isPlaneswalker()) {
                yPos = 24;
            }
            int xPos = 56;
            g2d.drawString(cardName, xPos, yPos + metrics.getAscent());
            g2d.dispose();
        }
    }

    private static Image resizeMana(ImageIcon mana) {
        BufferedImage newImage = new BufferedImage(19, 19, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = newImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics2D.drawImage(mana.getImage(), 0, 0, 18, 18, null);
        graphics2D.dispose();
        return newImage;
    }
}
