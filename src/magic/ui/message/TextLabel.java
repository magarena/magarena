package magic.ui.message;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.FontsAndBorders;

@SuppressWarnings("serial")
public class TextLabel extends JPanel {

    private static final int SPACE_WIDTH = 3;
    private static final int LINE_HEIGHT = 16;

    private static final Map<?, ?> desktopHintsMap;

    private final List<TComponent> components = new ArrayList<>();
    private final int maxWidth;
    private final boolean center;

    static {
        final Toolkit tk = Toolkit.getDefaultToolkit();
        desktopHintsMap = (Map<?, ?>) (tk.getDesktopProperty("awt.font.desktophints"));
    }

    // CTR
    public TextLabel(String text, Font aFont, int maxWidth, boolean center, Color aColor) {

        this.maxWidth = maxWidth;
        this.center = center;

        setOpaque(false);
        setLayout(null);

        TComponentBuilder.buildTComponents(components, text, this, aFont, aColor);
        layoutTComponents();
    }

    // CTR
    public TextLabel(String text, Font aFont, int maxWidth, boolean center) {
        this(text, aFont, maxWidth, center, MagicStyle.getTheme().getColor(Theme.COLOR_CHOICE_FOREGROUND));
    }

    // CTR
    public TextLabel(final String text, final int maxWidth, final boolean center) {
        this(text, FontsAndBorders.FONT1, maxWidth, center, MagicStyle.getTheme().getColor(Theme.COLOR_CHOICE_FOREGROUND));
    }

    private void layoutTComponents() {

        int x = 0;
        int y = 0;

        for (final TComponent component : components) {

            if (component == TComponentBuilder.SPACE_COMPONENT) {
                x += SPACE_WIDTH;

            } else if (component == TComponentBuilder.BREAK_COMPONENT) {
                x = 0;
                y += LINE_HEIGHT;

            } else {
                final Dimension csize = component.getPreferredSize();
                if (component.requiresNewLine() && x + csize.width > maxWidth) {
                    x = 0;
                    y += LINE_HEIGHT;
                }
                component.setLocation(x, y + (LINE_HEIGHT - csize.height));
                x += csize.width;
            }
        }

        final Insets insets = getInsets();
        setPreferredSize(new Dimension(
            maxWidth - insets.left + insets.right,
            y + LINE_HEIGHT + insets.top + insets.bottom));
    }

    @Override
    public void paintComponent(final Graphics g) {

        if (desktopHintsMap != null) {
            ((Graphics2D) g).addRenderingHints(desktopHintsMap);
        }

        final Dimension size = getPreferredSize();
        final int cx = center ? (getWidth() - maxWidth) / 2 : 0;
        final int cy = center ? (getHeight() - size.height) / 2 : 0;

        for (final TComponent component : components) {
            component.paint(this, g, cx, cy);
        }
    }

}
