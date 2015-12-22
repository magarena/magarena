package magic.ui.cardBuilder.renderers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.font.FontRenderContext;
import java.awt.font.GraphicAttribute;
import java.awt.font.ImageGraphicAttribute;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Collection;
import java.util.Set;

import magic.model.MagicColor;
import magic.model.MagicType;
import magic.ui.MagicImages;
import magic.ui.cardBuilder.IRenderableCard;
import magic.ui.cardBuilder.ResourceManager;
import magic.ui.utility.GraphicsUtils;

public class OracleText {

    private static final int maxDistance = 260100;
    private static final double transparencyTolerance = 0.001;
    private static final int padding = 1;
    private static final Font cardTextFont = ResourceManager.getFont("MPlantin.ttf").deriveFont(Font.PLAIN, 18);//scale down when long string
    private static final int topPadding = 7;
    private static final int leftPadding = 3;
    public static final char NEWLINE = '\n';
    private static Rectangle textBoxBounds;

    static void drawOracleText(BufferedImage cardImage, IRenderableCard cardDef) {
        if (cardDef.hasText()) {
            String oracleText = cardDef.getText();
            textBoxBounds = getTextBoxSize(cardDef);

            AttributedString realOracle = textIconReplace(new AttributedString(oracleText), oracleText);

            BufferedImage textBoxText = drawTextToBox(
                realOracle,
                oracleText,
                cardTextFont,
                textBoxBounds,
                leftPadding,
                topPadding
            );

            Graphics2D g2d = cardImage.createGraphics();
            BufferedImage trimmedTextBox = trimTransparency(textBoxText);
            int heightPadding = (int) ((textBoxBounds.getHeight() - trimmedTextBox.getHeight()) / 2);
            int widthPadding = (int) Math.min((textBoxBounds.getWidth() - trimmedTextBox.getWidth()) / 2, 3);
            int yPos = getYPosition(cardDef);
            g2d.drawImage(trimmedTextBox, 30 + widthPadding, yPos + heightPadding, null);//29,327 = top left textbox position.
            g2d.dispose();

        } else if (cardDef.hasType(MagicType.Land)) {
            Set<MagicColor> landColors = Frame.getLandColors(cardDef);
            BufferedImage landImage = null;
            if (landColors.size() == 1) {
                for (MagicColor color : landColors) {
                    landImage = getLandImage(color);
                }
            } else {
                landImage = getHybridLandImage(landColors);
            }
            if (landImage != null) { //Currently ignoring colorless basic lands with no type - no watermark for colorless yet
                Graphics2D g2d = cardImage.createGraphics();
                int heightPadding = (int) ((textBoxBounds.getHeight() - landImage.getHeight()) / 2);
                int widthPadding = (int) ((textBoxBounds.getWidth() - landImage.getWidth()) / 2);
                g2d.drawImage(landImage, 30 + widthPadding, 327 + heightPadding, null);
                g2d.dispose();
            }
        }
    }

    static void drawPlaneswalkerOracleText(BufferedImage cardImage, IRenderableCard cardDef) {
        if (cardDef.hasText() && getPlaneswalkerAbilityCount(cardDef) == 3) {
            String[] abilityActivation = getPlaneswalkerActivationText(cardDef);
            textBoxBounds = new Rectangle(0, 0, 282, 49);
            for (int i = 0; i < 3; i++) {
                String oracleText = abilityActivation[i];
                AttributedString realOracle = textIconReplace(new AttributedString(oracleText), oracleText);

                BufferedImage textBoxText = drawTextToBox(
                    realOracle,
                    oracleText,
                    cardTextFont,
                    textBoxBounds,
                    leftPadding,
                    topPadding
                );

                Graphics2D g2d = cardImage.createGraphics();
                BufferedImage trimmedTextBox = trimTransparency(textBoxText);
                int heightPadding = (int) ((textBoxBounds.getHeight() - trimmedTextBox.getHeight()) / 2);
                int widthPadding = (int) Math.min((textBoxBounds.getWidth() - trimmedTextBox.getWidth()) / 2, 3);
                int yPos = (int) (330 + i * textBoxBounds.getHeight());
                g2d.drawImage(trimmedTextBox, 63 + widthPadding, yPos + heightPadding, null);
                g2d.dispose();
            }

        }
    }

    private static int getYPosition(IRenderableCard cardDef) {
        if (cardDef.isToken()) {
            return 388;
        }
        if (cardDef.isPlaneswalker()) {
            return 330;
        }
        return 327;
    }

    private static Rectangle getTextBoxSize(IRenderableCard cardDef) {
        if (cardDef.isToken()) {
            return new Rectangle(0, 0, 314, 94);
        } else if (cardDef.isPlaneswalker()) {
            return new Rectangle(0, 0, 282, 148);
        }
        return new Rectangle(0, 0, 314, 154);
    }

    private static BufferedImage getLandImage(MagicColor color) {
        switch (color) {
            case Black:
                return ResourceManager.blackLandImage;
            case Blue:
                return ResourceManager.blueLandImage;
            case Green:
                return ResourceManager.greenLandImage;
            case Red:
                return ResourceManager.redLandImage;
            case White:
                return ResourceManager.whiteLandImage;
            default:
                return null;
        }
    }

    private static BufferedImage getHybridLandImage(Collection<MagicColor> colors) {
        if (colors.contains(MagicColor.Black) && colors.contains(MagicColor.Green)) {
            return ResourceManager.bgLandImage;
        }
        if (colors.contains(MagicColor.Black) && colors.contains(MagicColor.Red)) {
            return ResourceManager.brLandImage;
        }
        if (colors.contains(MagicColor.Green) && colors.contains(MagicColor.Blue)) {
            return ResourceManager.guLandImage;
        }
        if (colors.contains(MagicColor.Green) && colors.contains(MagicColor.White)) {
            return ResourceManager.gwLandImage;
        }
        if (colors.contains(MagicColor.Red) && colors.contains(MagicColor.Green)) {
            return ResourceManager.rgLandImage;
        }
        if (colors.contains(MagicColor.Red) && colors.contains(MagicColor.White)) {
            return ResourceManager.rwLandImage;
        }
        if (colors.contains(MagicColor.Blue) && colors.contains(MagicColor.Black)) {
            return ResourceManager.ubLandImage;
        }
        if (colors.contains(MagicColor.Blue) && colors.contains(MagicColor.Red)) {
            return ResourceManager.urLandImage;
        }
        if (colors.contains(MagicColor.White) && colors.contains(MagicColor.Black)) {
            return ResourceManager.wbLandImage;
        }
        if (colors.contains(MagicColor.White) && colors.contains(MagicColor.Blue)) {
            return ResourceManager.wuLandImage;
        }
        return null;
    }

    public static String[] getOracleAsLines(IRenderableCard cardDef) {
        String text = cardDef.getText();
        return text.split("\n");
    }

    static int getPlaneswalkerAbilityCount(IRenderableCard cardDef) {
        String[] abilities = getOracleAsLines(cardDef);
        return abilities.length;
    }

    static String[] getPlaneswalkerActivationText(IRenderableCard cardDef) {
        String[] abilities = getOracleAsLines(cardDef);
        String[] text = new String[abilities.length];
        for (int i = 0; i < abilities.length; i++) {
            String[] fulltext;
            fulltext = abilities[i].split(":");
            try {
                text[i] = fulltext[1].trim();
            } catch (IndexOutOfBoundsException e) {
                text[i] = fulltext[0].trim();
            }
        }
        return text;
    }

    private static BufferedImage drawTextToBox(
        AttributedString attrString,
        String string,
        Font font,
        Rectangle box,
        int leftPadding,
        int topPadding) {

        //setUp baseImage and Graphics2D
        BufferedImage baseImage = GraphicsUtils.getCompatibleBufferedImage(
            (int) box.getWidth(),
            (int) box.getHeight(),
            Transparency.TRANSLUCENT);
        Graphics2D g2d = baseImage.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setColor(Color.BLACK);

        FontRenderContext frc = g2d.getFontRenderContext();

        //get String and Font
        attrString.addAttribute(TextAttribute.FONT, font);
        AttributedCharacterIterator text = attrString.getIterator();
        int paragraphStart = text.getBeginIndex();
        int paragraphEnd = text.getEndIndex();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(text, frc);
        float boxWidth = baseImage.getWidth();
        float boxHeight = baseImage.getHeight();
        float posY = topPadding;
        lineMeasurer.setPosition(paragraphStart);
        Boolean newParagraph = false;

        //Measure length of string to fit in box
        boolean retry = false;
        while (lineMeasurer.getPosition() < paragraphEnd) {
            //Check for ptPanel overlap
            int next;
            next = posY >= 123 ? lineMeasurer.nextOffset(boxWidth - (leftPadding << 1) - 100) : lineMeasurer.nextOffset(boxWidth - (leftPadding << 1));
            int limit = next;
            //Check for newlines
            for (int i = lineMeasurer.getPosition(); i < next; ++i) {
                char c = string.charAt(i);
                if (c == NEWLINE && i > lineMeasurer.getPosition()) {
                    limit = i;
                    newParagraph = true;
                    break;
                }
            }

            //get+draw measured length
            TextLayout layout = lineMeasurer.nextLayout(boxWidth, limit, false);
            posY += layout.getAscent();
            layout.draw(g2d, (float) leftPadding, posY);

            //add extra space between paragraphs
            if (newParagraph) {
                posY += layout.getLeading() + layout.getDescent();
                newParagraph = false;
            }

            //check if out of room
            if (posY + layout.getDescent() > boxHeight) {
                //try again with smaller font
                retry = true;
                break;
            }

            //move to next line
            posY += layout.getDescent();

        }

        //cleanup + return
        g2d.dispose();

        if (retry) {
            //font size 12.5 should be minimum
            Font resize = font.deriveFont((float) (font.getSize2D() - 0.5));
            return drawTextToBox(attrString, string, resize, box, leftPadding, topPadding);
        }

        return baseImage;
    }

    public static BufferedImage trimTransparency(BufferedImage source) {
        int height = source.getHeight();
        int width = source.getWidth();
        int x0 = width;
        int y0 = height;
        int x1 = 0;
        int y1 = 0;
        int color = Transparency.TRANSLUCENT;
        BufferedImage tmp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        // Copy over the pixel grid to the temporary image
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = source.getRGB(j, i);
                tmp.setRGB(j, i, pixel);
                // If not within tolerance, update coordinates;
                if (!isWithinTolerance(color, pixel, transparencyTolerance)) {
                    x0 = j < x0 ? j : x0;
                    x1 = j > x1 ? j : x1;
                    y0 = i < y0 ? i : y0;
                    y1 = i > y1 ? i : y1;
                }
            }
        }
        if (!(x0 == width && y0 == height)) {
            x0 -= x0 - padding < 0 ? 0 : padding;
            x1 += x1 + padding > width ? 1 : padding;
            y0 -= y0 - padding < 0 ? 0 : padding;
            y1 += y1 + padding > height ? 1 : padding;

            // Recalculate height and width
            tmp = tmp.getSubimage(x0, y0, x1 - x0, y1 - y0);
        }
        return tmp;
    }

    public static AttributedString textIconReplace(AttributedString string, String originalString) {
        AttributedCharacterIterator text = string.getIterator();
        for (int i = 0; i < text.getEndIndex(); ++i) {
            if (i < originalString.length()){
                char c = originalString.charAt(i);
                if (c == '{') {
                    int endMana = i+2;
                    if (originalString.charAt(endMana) == '/') {
                        endMana+=2;
                    }
                    String iconString = originalString.substring(i, endMana + 1); //get mana-string //substring returns at -1 value
                    Image iconImage = MagicImages.getIcon(iconString).getImage(); //get related Icon as Image
                    ImageGraphicAttribute icon = new ImageGraphicAttribute(iconImage, GraphicAttribute.BOTTOM_ALIGNMENT); //define replacement icon
                    BufferedImage nullImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB); //create null image
                    ImageGraphicAttribute nulled = new ImageGraphicAttribute(nullImage,GraphicAttribute.BOTTOM_ALIGNMENT); //define null image
                    string.addAttribute(TextAttribute.CHAR_REPLACEMENT,nulled, i, i +1); //replace first bracket
                    if (endMana- i >3){
                        //for hybrid + phyrexian
                        string.addAttribute(TextAttribute.CHAR_REPLACEMENT,nulled, i +1, i +2); //replace first Mana Character
                        string.addAttribute(TextAttribute.CHAR_REPLACEMENT,icon, i +2, i +3); //replace divider with icon
                        string.addAttribute(TextAttribute.CHAR_REPLACEMENT,nulled,endMana-1,endMana); //replace second Mana Character
                    } else {
                        string.addAttribute(TextAttribute.CHAR_REPLACEMENT,icon, i +1, i +2); //replace Mana Character
                    }
                    string.addAttribute(TextAttribute.CHAR_REPLACEMENT,nulled,endMana,endMana+1); //replace end bracket
                }
            }
        }
        return string;
    }

    public static boolean isWithinTolerance(int baseColor, int sourceColor, double tol) {
        int distance = colorDistance(baseColor, sourceColor);
        return distance <= tol * maxDistance;
    }

    public static int colorDistance(int baseColor, int sourceColor) {
        int distance = 0;
        for (int i = 0; i < 32; i += 8) {
            distance += StrictMath.pow((baseColor >> i & 0xff) - (sourceColor >> i & 0xff), 2);
        }
        return distance;
    }
}
