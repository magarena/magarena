package magic.ui.widget.card.filter;

import java.awt.Component;
import javax.swing.JCheckBox;
import magic.ui.theme.ThemeFactory;

@SuppressWarnings("serial")
class FilterCheckBox extends JCheckBox
    implements IFilterCheckBox {

    FilterCheckBox(String text) {
        super(text);
        setOpaque(false);
        setForeground(ThemeFactory.getTheme().getTextColor());
        setFocusPainted(true);
        setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    @Override
    public boolean isSelected() {
        return super.isSelected();
    }

    @Override
    public void setSelected(boolean b) {
        super.setSelected(b);
    }

    @Override
    public String getText() {
        return super.getText();
    }
}
