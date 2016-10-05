package magic.ui.widget.message;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import magic.data.GeneralConfig;
import magic.model.MagicMessage;

public class TextComponent extends TComponent {

    public static MessageStyle messageStyle = GeneralConfig.getInstance().getLogMessageStyle();
    private static final String CARD_ID_DELIM = String.valueOf(MagicMessage.CARD_ID_DELIMITER);

    private final String text;
    private final Font font;
    private final Color fontColor;
    private final FontMetrics metrics;
    private final boolean newLine;
    private final String cardInfo;

    TextComponent(
        final String text,
        final JComponent component,
        final Font aFont,
        final boolean isChoice,
        final String aCardInfo,
        final Color choiceColor,
        final Color interactiveColor) {

        this.text = text;
        this.cardInfo = aCardInfo;

        this.fontColor = getTextColor(isChoice, choiceColor, interactiveColor);
        this.font = getTextFont(aFont);
        this.metrics = component.getFontMetrics(this.font);

        this.newLine = !(".".equals(text) || ",".equals(text));

    }

    private Color getTextColor(boolean isChoice, Color choiceColor, Color interactiveColor) {
        if (isCardId() && isChoice == false) {
            return Color.DARK_GRAY;
        }
        if (isInteractive() && messageStyle != MessageStyle.PLAINBOLDMONO) {
            return interactiveColor;
        }
        if (text.equals("(") || text.equals(")")) {
            return messageStyle != MessageStyle.PLAINBOLDMONO
                ? interactiveColor
                : choiceColor;
        }
        if (isChoice) {
            return choiceColor;
        }
        return Color.BLACK;
    }

    private Font getTextFont(final Font aFont) {
        final boolean isBoldFont =
            messageStyle != MessageStyle.PLAIN
            && (isInteractive() || messageStyle == MessageStyle.BOLD)
            && !isCardId();
        return messageStyle == MessageStyle.PLAIN
            ? aFont
            : isBoldFont
                ? aFont.deriveFont(Font.BOLD)
                : aFont;
    }

    private boolean isCardId() {
        return text.startsWith(CARD_ID_DELIM);
    }

    @Override
    boolean requiresNewLine() {
        return newLine;
    }

    @Override
    Dimension getPreferredSize() {
        return new Dimension(metrics.stringWidth(text) + 1, metrics.getHeight());
    }

    @Override
    void paint(final JComponent com, final Graphics g, final int x, final int y) {
        g.setColor(fontColor);
        g.setFont(font);
        g.drawString(text, lx + x, ly + y + metrics.getAscent());
    }

    @Override
    Rectangle getBounds() {
        return new Rectangle(
                lx,
                ly,
                metrics.stringWidth(text) + 1,
                metrics.getHeight());
    }

    @Override
    final boolean isInteractive() {
        return cardInfo.isEmpty() == false && getCardId() > 0;
    }

    long getCardId() {
        final String[] info = cardInfo.split(CARD_ID_DELIM);
        if (info.length > 1) {
            return Long.parseLong(info[1]);
        } else {
            return 0L;
        }
    }

}
