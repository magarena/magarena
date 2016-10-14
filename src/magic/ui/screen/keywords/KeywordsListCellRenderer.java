package magic.ui.screen.keywords;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import magic.ui.utility.MagicStyle;

@SuppressWarnings("serial")
class KeywordsListCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        /* The DefaultListCellRenderer class will take care of
         * the JLabels text property, it's foreground and background
         * colors, and so on.
         */
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        setForeground(isSelected
                ? MagicStyle.getRolloverColor()
                : Color.LIGHT_GRAY);

        setOpaque(false);

        return this;
    }

}
