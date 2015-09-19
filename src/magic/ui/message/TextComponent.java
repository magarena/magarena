package magic.ui.message;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import magic.model.MagicMessage;

class TextComponent extends TComponent {

    private final Color choiceColor;
    private final String text;
    private final Font font;
    private final FontMetrics metrics;
    private final boolean isChoice;
    private final boolean newLine;
    private final String cardInfo;

    TextComponent(
        final String text,
        final JComponent component,
        final Font aFont,
        final boolean isChoice,
        final String aCardInfo,
        final Color aColor) {

        this.text = text;
        this.isChoice = isChoice;
        this.choiceColor = aColor;
        this.cardInfo = aCardInfo;

        final boolean isBoldFont = aCardInfo.isEmpty() == false && isChoice == false && !text.startsWith("#");
        this.font = isBoldFont ? aFont.deriveFont(Font.BOLD) : aFont;
        this.metrics = component.getFontMetrics(this.font);

        this.newLine = !(".".equals(text) || ",".equals(text));

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
        g.setColor(text.startsWith("#") ? Color.DARK_GRAY : isChoice ? choiceColor : Color.BLACK);
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
    boolean isInteractive() {
        return cardInfo.isEmpty() == false;
    }

    long getCardId() {
        final String[] info = cardInfo.split(String.valueOf(MagicMessage.CARD_ID_DELIMITER));
        if (info.length > 1) {
            return Long.parseLong(info[1]);
        } else {
            return 0L;
        }
    }

}
