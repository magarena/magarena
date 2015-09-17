package magic.ui.message;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import magic.data.TextImages;
import magic.ui.IconImages;
import magic.ui.widget.FontsAndBorders;

@SuppressWarnings("serial")
public class TextLabel extends JPanel {

    private static final TComponent SPACE_LABEL = new EmptyComponent();
    private static final TComponent BREAK_LABEL = new EmptyComponent();

    private static final int SPACE_WIDTH = 4;
    private static final int LINE_HEIGHT = 16;

    private static final Map<?, ?> desktopHintsMap;

    private final List<TComponent> components;
    private final int maxWidth;
    private final boolean center;

    static {
        final Toolkit tk = Toolkit.getDefaultToolkit();
        desktopHintsMap = (Map<?, ?>) (tk.getDesktopProperty("awt.font.desktophints"));
    }

    public TextLabel(final String text, final int maxWidth, final boolean center) {
        components = new ArrayList<>();
        this.maxWidth = maxWidth;
        this.center = center;
        setLayout(null);
        setOpaque(false);
        buildComponents(text);
        layoutComponents();
    }

    public void setColors(final Color aTextColor, final Color aChoiceColor) {
        TextComponent.setColors(aTextColor, aChoiceColor);
    }

    private void addComponent(final TComponent component) {
        if (component != null) {
            components.add(component);
        }
    }

    private TComponent buildComponent(final String textPart, final boolean info, final boolean isBlueInfo) {
        if (textPart.isEmpty()) {
            return null;
        }
        final TComponent component;
        if (textPart.charAt(0) == '{' && TextImages.contains(textPart)) {
            component = new IconComponent(IconImages.getIcon(TextImages.getIcon(textPart)));
        } else if (info) {
            component = new TextComponent(textPart, this, FontsAndBorders.FONT0, isBlueInfo);
        } else {
            component = new TextComponent(textPart, this, FontsAndBorders.FONT1, false);
        }
        return component;
    }

    private void buildComponents(String text) {
        text = text.replaceAll("\\s+", " ").trim() + ' ';
        int startIndex = 0;
        boolean info = false;
        boolean isBlueInfo = false;
        for (int index = 0; index < text.length(); index++) {
            final char ch = text.charAt(index);
            if (ch == ' ') {
                addComponent(buildComponent(text.substring(startIndex, index), info, isBlueInfo));
                addComponent(SPACE_LABEL);
                startIndex = index + 1;
            } else if (ch == '|') {
                addComponent(buildComponent(text.substring(startIndex, index), info, isBlueInfo));
                addComponent(BREAK_LABEL);
                startIndex = index + 1;
            } else if (ch == '{') {
                addComponent(buildComponent(text.substring(startIndex, index), info, isBlueInfo));
                startIndex = index;
            } else if (ch == '}') {
                addComponent(buildComponent(text.substring(startIndex, index + 1), info, isBlueInfo));
                startIndex = index + 1;
            } else if (ch == '(') {
                addComponent(buildComponent(text.substring(startIndex, index), info, isBlueInfo));
                startIndex = index;
                info = true;
                isBlueInfo = true;
            } else if (ch == ')') {
                addComponent(buildComponent(text.substring(startIndex, index + 1), info, isBlueInfo));
                startIndex = index + 1;
                info = false;
                isBlueInfo = false;
            } else if (ch == '[') {
                addComponent(buildComponent(text.substring(startIndex, index), info, isBlueInfo));
                startIndex = index + 1;
                info = true;
            } else if (ch == ']') {
                addComponent(buildComponent(text.substring(startIndex, index), info, isBlueInfo));
                startIndex = index + 1;
                info = false;
            }
        }
    }

    private void layoutComponents() {
        int x = 0;
        int y = 0;
        for (final TComponent component : components) {

            if (component == SPACE_LABEL) {
                x += SPACE_WIDTH;
            } else if (component == BREAK_LABEL) {
                x = 0;
                y += LINE_HEIGHT;
            } else {
                final Dimension csize = component.getPreferredSize();
                if (component.requiresNewLine() && x + csize.width >= maxWidth) {
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
