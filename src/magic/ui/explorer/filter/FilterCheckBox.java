package magic.ui.explorer.filter;

import java.awt.Component;
import javax.swing.JCheckBox;
import magic.ui.theme.ThemeFactory;

@SuppressWarnings("serial")
public class FilterCheckBox extends JCheckBox {

    FilterCheckBox(String text) {
        super(text);
        setOpaque(false);
        setForeground(ThemeFactory.getTheme().getTextColor());
        setFocusPainted(true);
        setAlignmentX(Component.LEFT_ALIGNMENT);
    }
}
