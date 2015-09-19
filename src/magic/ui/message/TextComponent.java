package magic.ui.message;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JComponent;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;

class TextComponent extends TComponent {

    private static Color textColor = MagicStyle.getTheme().getTextColor();
    private static Color choiceColor = MagicStyle.getTheme().getColor(Theme.COLOR_CHOICE_FOREGROUND);

    private final String text;
    private final Font font;
    private final FontMetrics metrics;
    private final boolean choice;
    private final boolean newLine;
    private final String cardInfo;

    TextComponent(
        final String text,
        final JComponent component,
        final Font aFont,
        final boolean choice,
        final String aCardInfo) {
        
        this.text = text;
        this.font = aFont;
        this.metrics = component.getFontMetrics(aFont);
        this.choice = choice;
        this.newLine = !(".".equals(text) || ",".equals(text));
        this.cardInfo = aCardInfo;
    }

    static void setColors(final Color aTextColor, final Color aChoiceColor) {
        textColor = aTextColor;
        choiceColor = aChoiceColor;
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
        g.setColor(choice ? choiceColor : textColor);
        g.setFont(font);
        g.drawString(text, lx + x, ly + y + metrics.getAscent());
    }
    
}
