package magic.ui.prefs;

import java.awt.Font;
import java.awt.event.MouseListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.data.GeneralConfig;
import magic.translate.UiString;
import magic.ui.theme.ThemeFactory;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.ColorButton;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ThemesPanel extends JPanel {
    
    // translatable strings.
    private static final String _S47 = "Overrides the default theme background with a custom image which is set by dragging an image file onto the Magarena window.";
    private static final String _S49 = "custom background";
    private static final String _S51 = "general highlight color";

    private final static GeneralConfig CONFIG = GeneralConfig.getInstance();

    private final JCheckBox customBackgroundCheckBox;
    private final ColorButton rollOverColorButton;
    private final JComboBox<String> themeComboBox;

    ThemesPanel(MouseListener aListener) {

        themeComboBox = new JComboBox<>(ThemeFactory.getThemeNames());
        themeComboBox.setFont(themeComboBox.getFont().deriveFont(Font.BOLD).deriveFont(14f));
        ((JLabel)themeComboBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        themeComboBox.addMouseListener(aListener);
        themeComboBox.setFocusable(false);
        themeComboBox.setSelectedItem(CONFIG.getTheme());

        customBackgroundCheckBox = new JCheckBox("", CONFIG.isCustomBackground());
        customBackgroundCheckBox.setToolTipText(UiString.get(_S47));
        customBackgroundCheckBox.setFocusable(false);
        customBackgroundCheckBox.addMouseListener(aListener);

        rollOverColorButton = new ColorButton(MagicStyle.getRolloverColor());
        rollOverColorButton.setFocusable(false);

        setLayout(new MigLayout("flowx, wrap 2, insets 16, gapy 0"));
        add(themeComboBox, "w 100%");
        add(new ThemesActionPanel(this, aListener));
        add(getSettingsPanel(), "w 100%, h 100%, spanx 2");
    }

    private JPanel getSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("flowx, wrap 2"));
        panel.add(rollOverColorButton, "w 24!");
        panel.add(new JLabel(UiString.get(_S51)));
        panel.add(customBackgroundCheckBox, "w 24!");
        panel.add(new JLabel(UiString.get(_S49)));
        return panel;
    }

    void saveSettings() {
        CONFIG.setTheme(getSelectedThemeName());
        CONFIG.setCustomBackground(customBackgroundCheckBox.isSelected());
        CONFIG.setRolloverColor(rollOverColorButton.getColor());
    }

    String getSelectedThemeName() {
        return themeComboBox.getItemAt(themeComboBox.getSelectedIndex());
    }

}
