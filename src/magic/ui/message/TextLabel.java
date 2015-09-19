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
import magic.data.TextImages;
import magic.model.MagicMessage;
import magic.ui.IconImages;
import magic.ui.widget.FontsAndBorders;
import magic.utility.MagicSystem;

@SuppressWarnings("serial")
public class TextLabel extends JPanel {

    private static final TComponent SPACE_LABEL = new EmptyComponent();
    private static final TComponent BREAK_LABEL = new EmptyComponent();

    private static final int SPACE_WIDTH = 3;
    private static final int LINE_HEIGHT = 16;

    private static final Map<?, ?> desktopHintsMap;

    private final List<TComponent> components;
    private final int maxWidth;
    private final boolean center;
    private final Font defaultFont;

    static {
        final Toolkit tk = Toolkit.getDefaultToolkit();
        desktopHintsMap = (Map<?, ?>) (tk.getDesktopProperty("awt.font.desktophints"));
    }

    public TextLabel(String text, Font aFont, int maxWidth, boolean center) {

        this.defaultFont = aFont;
        this.maxWidth = maxWidth;
        this.center = center;

        components = new ArrayList<>();

        setOpaque(false);
        setLayout(null);

        buildComponents(text);
        layoutComponents();
    }

    public TextLabel(final String text, final int maxWidth, final boolean center) {
        this(text, FontsAndBorders.FONT1, maxWidth, center);
    }

    public void setColors(final Color aTextColor, final Color aChoiceColor) {
        TextComponent.setColors(aTextColor, aChoiceColor);
    }

    private void addComponent(final TComponent component) {
        if (component != null) {
            components.add(component);
        }
    }

    private TComponent buildComponent(
            final String textPart,
            final boolean info,
            final boolean isBlueInfo,
            final String aCardInfo) {

        if (textPart.isEmpty()) {
            return null;
        }

        final TComponent component;
        if (textPart.charAt(0) == '{' && TextImages.contains(textPart)) {
            component = new IconComponent(IconImages.getIcon(TextImages.getIcon(textPart)));
        } else if (info) {
            component = new TextComponent(textPart, this, FontsAndBorders.FONT0, isBlueInfo, aCardInfo);
        } else {
            component = new TextComponent(textPart, this, defaultFont, false, aCardInfo);
        }
        return component;
    }

    private TComponent buildComponent(final String textPart, final boolean info, final boolean isBlueInfo) {
        return buildComponent(textPart, info, isBlueInfo, "");
    }

    private String replaceWhitespace(final String text, final String replace) {
        // whitespace = mutliple spaces, tabs, line breaks, form feeds, etc.
        return text.replaceAll("\\s+", replace).trim();
    }

    private void buildComponents(String text) {

        text = replaceWhitespace(text, " ") + ' ';

        boolean info = false;
        boolean isBlueInfo = false;
        String cardInfo = "";
        int startIndex = 0;

        for (int index = 0; index < text.length(); index++) {
            final char ch = text.charAt(index);
            if (ch == ' ') {
                addComponent(buildComponent(text.substring(startIndex, index), info, isBlueInfo, cardInfo));
                addComponent(SPACE_LABEL);
                startIndex = index + 1;
            } else if (ch == '|') {
                addComponent(buildComponent(text.substring(startIndex, index), info, isBlueInfo, cardInfo));
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
            } else if (ch == '<') {
                cardInfo = text.substring(index + 1, text.indexOf(">", index + 1));
                addComponent(buildComponent(text.substring(startIndex, index), info, isBlueInfo));
                startIndex = index + 1;
            } else if (ch == '>') {
                final String s = String.format(info ? " (%s)" : "%s", text.substring(startIndex, index));
                addComponent(buildComponent(s, info, isBlueInfo, cardInfo));
                info = false;
                isBlueInfo = false;
                cardInfo = "";
                startIndex = index + 1;
            } else if (ch == MagicMessage.CARD_ID_DELIMITER) {
                addComponent(buildComponent(text.substring(startIndex, index), info, isBlueInfo, cardInfo));
                startIndex = index + 1;
                if (MagicSystem.isDevMode()) {
                    info = true;
                    isBlueInfo = true;
                } else {
                    startIndex = text.indexOf(">", index + 1);
                }
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
