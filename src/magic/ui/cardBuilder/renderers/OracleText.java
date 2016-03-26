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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import magic.model.MagicColor;
import magic.model.MagicType;
import magic.ui.MagicImages;
import magic.ui.cardBuilder.IRenderableCard;
import magic.ui.cardBuilder.ResourceManager;
import magic.ui.utility.GraphicsUtils;

public class OracleText {

    public static final char NEWLINE = '\n';
    private static final int maxDistance = 260100;
    private static final double transparencyTolerance = 0.001;
    private static final int padding = 1;
    private static final Font cardTextFont = ResourceManager.getFont("MPlantin.ttf").deriveFont(Font.PLAIN, 18);//scale down when long string
    private static final int topPadding = 7;
    private static final int leftPadding = 3;

    static void drawOracleText(BufferedImage cardImage, IRenderableCard cardDef) {
        Rectangle textBoxBounds = getTextBoxSize(cardDef);
        if (cardDef.hasText()) {

            //29,327 = top left textbox position.
            int xPos = 30;
            int yPos = getYPosition(cardDef);
            drawTextToCard(
                cardImage,
                xPos,
                yPos,
                cardDef.getText(),
                textBoxBounds
            );

        } else if (cardDef.hasType(MagicType.Land)) {
            List<MagicColor> landColors = Frame.getLandColors(cardDef);
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
        int lines;
        int yPosOffset;
        if (cardDef.hasText() && getPlaneswalkerAbilityCount(cardDef) == 3) {
            lines = 3;
            yPosOffset = 330;
        } else {
            lines = 4;
            yPosOffset = 289;
        }

        String[] abilityActivation = getPlaneswalkerActivationText(cardDef);
        Rectangle textBoxBounds = new Rectangle(0, 0, 282, 49);
        for (int i = 0; i < lines; i++) {
            int xPos = 63;
            int yPos = (int) (yPosOffset + i * textBoxBounds.getHeight());
            drawTextToCard(
                cardImage,
                xPos,
                yPos,
                abilityActivation[i],
                textBoxBounds
            );
        }
    }

    static void drawLevellerOracleText(BufferedImage cardImage, IRenderableCard cardDef) {
        String[] abilities = getLevellerText(cardDef);
        Rectangle textBoxBounds = new Rectangle(0, 0, 185, 49);
        for (int i = 0; i < 3; i++) {
            String oracleText = abilities[i];
            if (!oracleText.isEmpty()) { //Not all levels have text
                int xPos = i == 0 ? 30 : 104; // xpos 104 for level arrows, normal for first line
                int yPos = (int) (330 + i * textBoxBounds.getHeight());
                drawTextToCard(
                    cardImage,
                    xPos,
                    yPos,
                    oracleText,
                    textBoxBounds
                );
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
        }
        if (cardDef.isPlaneswalker()) {
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
        }
        return null;
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
            String[] fulltext = abilities[i].split(": ", 2);
            text[i] = fulltext[fulltext.length - 1];
        }
        return text;
    }

    static String[] getLevellerText(IRenderableCard cardDef) {
        //Some levels contain /n as well
        String[] abilities = getOracleAsLines(cardDef);
        ArrayList<String> text = new ArrayList<>(3);
        for (int i = 0; i < abilities.length; i++) {
            if (i == 0) {
                text.add(abilities[0]);
            } else {
                if (abilities[i].contains("LEVEL") && abilities[i + 2].contains("LEVEL")) { //Catch empty level text
                    text.add("");
                } else {
                    if (!abilities[i].contains("LEVEL") && !abilities[i].matches("\\d+/\\d+")) {
                        if (i + 1 <= abilities.length - 1 && !abilities[i + 1].contains("LEVEL") && !abilities[i + 1].matches("\\d+/\\d+")) { //Catch multi-line level text
                            text.add(abilities[i] + NEWLINE + abilities[i + 1]);

                        } else {
                            text.add(abilities[i]);
                        }
                    }
                }
            }
        }
        return text.toArray(new String[3]);
    }

    public static void drawTextToCard(
        BufferedImage cardImage,
        int xPos,
        int yPos,
        String oracleText,
        Rectangle textBoxBounds) {

        AttributedString realOracle = textIconReplace(oracleText);

        BufferedImage textBoxText = drawTextToBox(
            realOracle,
            cardTextFont,
            textBoxBounds,
            leftPadding,
            topPadding
        );

        Graphics2D g2d = cardImage.createGraphics();
        BufferedImage trimmedTextBox = trimTransparency(textBoxText);
        int heightPadding = (int) ((textBoxBounds.getHeight() - trimmedTextBox.getHeight()) / 2);
        int widthPadding = (int) Math.min((textBoxBounds.getWidth() - trimmedTextBox.getWidth()) / 2, 3);
        g2d.drawImage(trimmedTextBox, xPos + widthPadding, yPos + heightPadding, null);
        g2d.dispose();
    }


    private static SortedMap<Float, TextLayout> tryTextLayout(
        AttributedString attrString,
        FontRenderContext frc,
        Rectangle box,
        int leftPadding,
        int topPadding
    ) {
        final SortedMap<Float, TextLayout> lines = new TreeMap<>();
        AttributedCharacterIterator text = attrString.getIterator();
        int paragraphStart = text.getBeginIndex();
        int paragraphEnd = text.getEndIndex();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(text, frc);
        float boxWidth = (float)box.getWidth();
        float boxHeight = (float)box.getHeight();
        float posY = topPadding;
        lineMeasurer.setPosition(paragraphStart);

        //Measure length of string to fit in box
        final AttributedCharacterIterator iter = attrString.getIterator();
        while (lineMeasurer.getPosition() < paragraphEnd) {
            //Check for ptPanel overlap
            int next = lineMeasurer.nextOffset(posY >= 123 ? boxWidth - (leftPadding << 1) - 100 : boxWidth - (leftPadding << 1));
            int limit = next;
            //Check for newlines
            for (int i = lineMeasurer.getPosition(); i < next; ++i) {
                char c = iter.setIndex(i);
                if (c == NEWLINE && i > lineMeasurer.getPosition()) {
                    limit = i;
                    break;
                }
            }

            //get+draw measured length
            TextLayout layout = lineMeasurer.nextLayout(boxWidth, limit, false);
            posY += layout.getAscent();
            lines.put(posY, layout);

            //add extra space between paragraphs
            if (limit < next) {
                posY += layout.getLeading() + layout.getDescent();
            }

            //move to next line
            posY += layout.getDescent();

            //check if out of room
            if (posY > boxHeight) {
                lines.clear();
                break;
            }
        }
        return lines;
    }

    private static SortedMap<Float, TextLayout> fitTextLayout(
        AttributedString attrString,
        Font font,
        FontRenderContext frc,
        Rectangle box,
        int leftPadding,
        int topPadding
    ) {
        // decrease font by 0.5 points each time until lines can fit
        SortedMap<Float, TextLayout> lines = new TreeMap<>();
        Font f = font;
        while (lines.isEmpty()) {
            attrString.addAttribute(TextAttribute.FONT, f);
            lines = tryTextLayout(attrString, frc, box, leftPadding, topPadding);
            f = f.deriveFont(f.getSize2D() - 0.5f);
        }
        return lines;
    }

    private static BufferedImage drawTextToBox(
        AttributedString attrString,
        Font font,
        Rectangle box,
        int leftPadding,
        int topPadding
    ) {

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

        // find largest font that can fit text to box
        final SortedMap<Float, TextLayout> lines = fitTextLayout(attrString, font, frc, box, leftPadding, topPadding);

        // draw the text
        for (final Entry<Float, TextLayout> entry : lines.entrySet()) {
            final TextLayout layout = entry.getValue();
            final float posY = entry.getKey();
            layout.draw(g2d, leftPadding, posY);
        }

        // cleanup + return
        g2d.dispose();

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

    public static AttributedString textIconReplace(final String text) {
        final String compacted = text.replaceAll("\\{[^\\}]+\\}", "M");
        final AttributedString attrString = new AttributedString(compacted);
        for (int i = 0, j = 0; i < text.length(); i++, j++) {
            char c = text.charAt(i);
            if (c == '{') {
                final int endSymbol = text.indexOf('}', i);
                // get mana-string, substring returns at -1 value
                String iconString = text.substring(i, endSymbol + 1);
                // get related Icon as Image
                Image iconImage = MagicImages.getIcon(iconString).getImage();
                // define replacement icon
                ImageGraphicAttribute icon = new ImageGraphicAttribute(iconImage, GraphicAttribute.BOTTOM_ALIGNMENT);
                // replace M with icon
                attrString.addAttribute(
                    TextAttribute.CHAR_REPLACEMENT,
                    icon,
                    j,
                    j + 1
                );
                // advance i to the end of symbol
                i = endSymbol;
            }
        }
        return attrString;
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
