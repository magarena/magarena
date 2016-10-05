package magic.ui.dialog.prefs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import magic.ui.theme.ThemeFactory;

@SuppressWarnings("serial")
class ThemesComboBox extends JComboBox<String> implements ListCellRenderer<String> {

    ThemesComboBox() {
        super(ThemeFactory.getThemeNames());
        setMinimumSize(new Dimension(getWidth(), 30));
        setRenderer(this);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
        final JLabel lbl = new JLabel(value, JLabel.CENTER);
        lbl.setOpaque(isSelected);
        lbl.setFont(lbl.getFont().deriveFont(14f));
        lbl.setFont(value.equals(ThemeFactory.getInstance().getCurrentTheme().getName()) ? lbl.getFont().deriveFont(Font.BOLD) : lbl.getFont());
        lbl.setBackground(isSelected ? Color.LIGHT_GRAY : lbl.getBackground());
        lbl.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        return lbl;
    }

}
