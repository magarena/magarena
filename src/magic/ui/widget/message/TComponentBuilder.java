package magic.ui.widget.message;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import javax.swing.JComponent;
import magic.data.TextImages;
import magic.model.MagicMessage;
import magic.ui.MagicImages;
import magic.ui.FontsAndBorders;
import magic.utility.MagicSystem;

class TComponentBuilder {
    private TComponentBuilder() {}

    static final TComponent SPACE_COMPONENT = new EmptyComponent();
    static final TComponent BREAK_COMPONENT = new EmptyComponent();

    private static JComponent container;
    private static Font defaultFont;
    private static Color choiceColor;
    private static Color interactiveColor;
    private static List<TComponent> components;

    static void buildTComponents(
        final List<TComponent> components,
        final String source,
        final JComponent aContainer,
        final Font aFont,
        final Color aColor,
        final Color interactiveColor) {

        TComponentBuilder.components = components;
        TComponentBuilder.container = aContainer;
        TComponentBuilder.defaultFont = aFont;
        TComponentBuilder.choiceColor = aColor;
        TComponentBuilder.interactiveColor = interactiveColor;

        final String msg = replaceWhitespace(source, " ") + ' ';

        boolean isCardId = false;
        boolean isTinyFont = false;
        boolean isChoice = false;
        String cardInfo = "";

        int startIndex = 0;

        for (int index = 0; index < msg.length(); index++) {

            final char ch = msg.charAt(index);

            //
            // space
            //
            if (ch == ' ') {
                final String textPart = msg.substring(startIndex, index);
                addTComponent(textPart, isTinyFont, isChoice, cardInfo);
                addTComponent(SPACE_COMPONENT);
                startIndex = index + 1;

            //
            // new line
            //
            } else if (ch == '|') {
                final String textPart = msg.substring(startIndex, index);
                addTComponent(textPart, isTinyFont, isChoice, cardInfo);
                addTComponent(BREAK_COMPONENT);
                startIndex = index + 1;

            //
            // "{icon_image}"
            //
            } else if (ch == '{') {
                final String textPart = msg.substring(startIndex, index);
                addTComponent(textPart, isTinyFont, isChoice, cardInfo);
                startIndex = index;

            } else if (ch == '}') {
                final String textPart = msg.substring(startIndex, index + 1);
                addTComponent(textPart, isTinyFont, isChoice, cardInfo);
                startIndex = index + 1;

            //
            // "(player_choice)"
            //
            } else if (ch == '(') {
                final String textPart = msg.substring(startIndex, index);
                addTComponent(textPart, isTinyFont, isChoice, cardInfo);
                startIndex = index;
                isTinyFont = true;
                isChoice = true;

            } else if (ch == ')') {
                final String textPart = msg.substring(startIndex, index + 1);
                addTComponent(textPart, isTinyFont, isChoice, cardInfo);
                startIndex = index + 1;
                isTinyFont = false;
                isChoice = false;

            //
            // "[use_tiny_font]"
            //
            } else if (ch == '[') {
                final String textPart = msg.substring(startIndex, index);
                addTComponent(textPart, isTinyFont, isChoice, cardInfo);
                startIndex = index + 1;
                isTinyFont = true;

            } else if (ch == ']') {
                final String textPart = msg.substring(startIndex, index);
                addTComponent(textPart, isTinyFont, isChoice, cardInfo);
                startIndex = index + 1;
                isTinyFont = false;

            //
            // "<card_name~MagicCard_id>"
            //
            } else if (ch == '<') {
                final String textPart = msg.substring(startIndex, index);
                addTComponent(textPart, isTinyFont, isChoice, cardInfo);
                cardInfo = msg.substring(index + 1, msg.indexOf(">", index + 1));
                startIndex = index + 1;

            } else if (ch == '>') {
                final String textPart = String.format(isCardId ? "#%s" : "%s", msg.substring(startIndex, index));
                addTComponent(textPart, isTinyFont, isChoice, cardInfo);
                isCardId = false;
                isTinyFont = isChoice;
                cardInfo = "";
                startIndex = index + 1;

            } else if (ch == MagicMessage.CARD_ID_DELIMITER && !cardInfo.isEmpty()) {
                final String textPart = msg.substring(startIndex, index);
                addTComponent(textPart, isTinyFont, isChoice, cardInfo);
                startIndex = index + 1;
                // only display card id if running in dev mode.
                if (MagicSystem.isDevMode()) {
                    isTinyFont = true;
                    isCardId = true;
                } else {
                    startIndex = msg.indexOf(">", index + 1);
                }
            }
        }

    }

    // whitespace = multiple spaces, tabs, line breaks, form feeds, etc.
    private static String replaceWhitespace(final String text, final String replace) {
        return text.replaceAll("\\s+", replace).trim();
    }

    private static void addTComponent(final TComponent component) {
        if (component != null) {
            components.add(component);
        }
    }

    private static void addTComponent(String text, boolean isTinyFont, boolean isChoice, String aCardInfo) {
        addTComponent(getTComponent(text, isTinyFont, isChoice ,aCardInfo));
    }

    private static TComponent getTComponent(final String text, boolean isTinyFont, boolean isChoice, String aCardInfo) {

        if (text.isEmpty()) {
            return null;
        }

        if (text.charAt(0) == '{' && TextImages.contains(text)) {
            return new IconComponent(MagicImages.getIcon(TextImages.getIcon(text)));
        }

        return new TextComponent(
            text,
            container,
            isTinyFont ? FontsAndBorders.FONT0 : defaultFont,
            isChoice,
            aCardInfo,
            choiceColor,
            TComponentBuilder.interactiveColor
        );
    }

}
