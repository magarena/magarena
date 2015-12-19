package magic.ui.image;

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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;

import magic.data.MagicIcon;
import magic.model.MagicCardDefinition;
import magic.model.MagicManaCost;
import magic.ui.ImageFileIO;
import magic.ui.MagicImages;
import magic.ui.cardBuilder.IRenderableCard;
import magic.ui.cardBuilder.renderers.CardBuilder;
import magic.ui.cardBuilder.renderers.TypeLine;
import magic.ui.utility.GraphicsUtils;
import magic.ui.widget.FontsAndBorders;
import magic.utility.MagicResources;

public class CardProxyImageBuilder {

    private static final CardProxyImageBuilder INSTANCE = new CardProxyImageBuilder();

    private static final BufferedImage PROXY_CANVAS = GraphicsUtils.scale(
            ImageFileIO.toImg(MagicResources.getImageUrl("proxy-canvas.png"), null),
            312, 445,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR,
            true
    );

    private CardProxyImageBuilder() {
    }

    public static CardProxyImageBuilder getInstance() {
        return INSTANCE;
    }

    public BufferedImage getImage(IRenderableCard cardDef) {
        if (cardDef == MagicCardDefinition.UNKNOWN) {
            return MagicImages.getMissingCardImage();
        }
        return CardBuilder.getCardBuilderImage(cardDef);
    }

    private ImageIcon getCardTypeIcon(final IRenderableCard cardDef) {
        if (cardDef.isCreature()) {
            return MagicImages.getIcon(MagicIcon.FS_CREATURE);
        } else if (cardDef.isLand()) {
            return MagicImages.getIcon(MagicIcon.FS_LAND);
        } else if (cardDef.isInstant()) {
            return MagicImages.getIcon(MagicIcon.FS_INSTANT);
        } else if (cardDef.isArtifact()) {
            return MagicImages.getIcon(MagicIcon.FS_ARTIFACT);
        } else if (cardDef.isEnchantment()) {
            return MagicImages.getIcon(MagicIcon.FS_ENCHANTMENT);
        } else if (cardDef.isSorcery()) {
            return MagicImages.getIcon(MagicIcon.FS_SORCERY);
        } else if (cardDef.isPlaneswalker()) {
            return MagicImages.getIcon(MagicIcon.FS_PLANESWALKER);
        } else {
            return null;
        }
    }

    private void drawCardTypeWatermark(Graphics2D g2d, IRenderableCard cardDef) {
        final ImageIcon ico = getCardTypeIcon(cardDef);
        if (ico != null && ico.getIconWidth() > 0 && ico.getIconHeight() > 0) {
            final BufferedImage image = GraphicsUtils.getTranslucentImage(
                    GraphicsUtils.getConvertedIcon(ico),
                    0.4f
            );
            g2d.drawImage(image, 0, PROXY_CANVAS.getHeight() - ico.getIconHeight(), null);
        }
    }

    private BufferedImage getCardImage(final IRenderableCard cardDef) {

        final int W = PROXY_CANVAS.getWidth();
        final int H = PROXY_CANVAS.getHeight();

        final BufferedImage bi = GraphicsUtils.getCompatibleBufferedImage(W, H, Transparency.TRANSLUCENT);
        final Graphics2D g2d = bi.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // start with base proxy image
        int x = 0;
        int y = 0;
        g2d.drawImage(PROXY_CANVAS, x, y, null);

        drawCardTypeWatermark(g2d, cardDef);

        g2d.setFont(FontsAndBorders.FONT4);
        g2d.setColor(Color.BLACK);

        // mana const
        x = 17;
        y = 34;
        if (cardDef.isLand() == false) {
            for (MagicIcon icon : cardDef.getCost().getIcons()) {
                g2d.drawImage(MagicImages.getBigManaIcon(icon).getImage(), x, 16, null);
                x += 25;
            }
            x = 17;
        }

        // card name
        y = 64;
        g2d.drawString(cardDef.getName(), x, y);

        // type
        y = 84;
        g2d.setFont(FontsAndBorders.FONT2);
        g2d.drawString(TypeLine.getTypeLine(cardDef), x, y);

        // p/t
        if (cardDef.isCreature()) {
            y = 114;
            g2d.setFont(FontsAndBorders.FONT4);
            g2d.drawString(cardDef.getPowerToughnessText(), x, y);
        }

        // oracle
        if (!cardDef.getText().equals("NONE") && !cardDef.getText().isEmpty()) {
            y = 150;
            final Rectangle rect = new Rectangle(x, y, W - 40, H - y - 10);
            drawOracleText(g2d, cardDef.getText(), rect);
        }

        g2d.dispose();

        return bi;
    }

    private void drawOracleText(final Graphics2D g2d, final String text, final Rectangle rect) {

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setColor(Color.BLACK);

        final Font font = FontsAndBorders.FONT2.deriveFont(Font.PLAIN);

        final float PARAGRAPH_SPACING = 4f;
        float y = (float) rect.y;
        for (String paragraph : text.split("\n")) {
            final AttributedString ats = getAttrStringWithIcons(paragraph, font);
            y = drawAttrStringParagraph(ats, g2d, rect, y);
            y += PARAGRAPH_SPACING;
        }

    }

    private static ImageIcon getIcon(final String string){
        switch (string) {
            case "{T}":
                return MagicImages.getSmallManaIcon(MagicIcon.MANA_TAP);
            case "{Q}":
                return MagicImages.getSmallManaIcon(MagicIcon.MANA_UNTAP);
            case "{S}":
                return MagicImages.getSmallManaIcon(MagicIcon.MANA_SNOW);
            default:
                final MagicManaCost mana = MagicManaCost.create(string);
                final List<MagicIcon> icons = mana.getIcons();
                return MagicImages.getIcon(icons.get(0));
        }
    }

    // identifies "{?}" tokens in oracle text.
    private static final Pattern TOKEN_PATTERN = Pattern.compile("\\{(.*?)\\}");

    // use an obscure character that is never likely to appear in oracle text.
    private static final String ICON_PLACEHOLDER = "\u25D9"; // ◙
    private static final Pattern ICON_PATTERN = Pattern.compile(ICON_PLACEHOLDER);

    private static AttributedString getAttrStringWithIcons(final String text, Font aFont) {

        // first pass - identify {?} tokens and replace with obscure single
        // unicode character to act as a placeholder for icon image.
        final Matcher m = TOKEN_PATTERN.matcher(text);
        final List<String> tokens = new ArrayList<>();
        while (m.find()) {
            tokens.add(m.group());
        }
        final String newText = m.replaceAll(ICON_PLACEHOLDER);

        final AttributedString ats = new AttributedString(newText);
        ats.addAttribute(TextAttribute.FONT, aFont);

        if (tokens.size() > 0) {
            // second pass - replace single character placeholder with
            // appropriate icon image associated with token.
            final Matcher m2 = ICON_PATTERN.matcher(newText);
            int tokenIdx = 0;
            while (m2.find()) {
                final Image tokenImage = getIcon(tokens.get(tokenIdx)).getImage();
                final ImageGraphicAttribute iga = new ImageGraphicAttribute(tokenImage, GraphicAttribute.BOTTOM_ALIGNMENT);
                ats.addAttribute(TextAttribute.CHAR_REPLACEMENT, iga, m2.start(), m2.start() + 1);
                tokenIdx++;
            }
        }

        return ats;
    }

    private static float drawAttrStringParagraph(
            final AttributedString ats,
            final Graphics2D g2d,
            final Rectangle rect,
            final float startY) {

        float posY = startY;

        final FontRenderContext frc = g2d.getFontRenderContext();
        final AttributedCharacterIterator itr = ats.getIterator();
        final LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(itr, frc);
        lineMeasurer.setPosition(itr.getBeginIndex());

        while (lineMeasurer.getPosition() < itr.getEndIndex()) {
            final TextLayout layout = lineMeasurer.nextLayout(rect.width);
            posY += layout.getAscent();
            layout.draw(g2d, rect.x, posY);
            posY += layout.getDescent() + layout.getLeading();
        }

        return posY;

    }

}
