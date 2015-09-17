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

    TextComponent(final String text, final JComponent component, final Font font, final boolean choice) {
        this.text = text;
        this.font = font;
        this.metrics = component.getFontMetrics(font);
        this.choice = choice;
        this.newLine = !(".".equals(text) || ",".equals(text));
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
