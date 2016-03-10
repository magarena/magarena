package magic.ui.prefs;

import java.awt.event.MouseListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.data.GeneralConfig;
import magic.translate.UiString;
import magic.ui.URLUtils;
import magic.ui.theme.ThemeFactory;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.ColorButton;
import magic.ui.widget.LinkLabel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ThemesPanel extends JPanel {
    
    // translatable strings.
    private static final String _S47 = "Overrides the default theme background with a custom image which is set by dragging an image file onto the Magarena window.";
    private static final String _S49 = "Custom background";
    private static final String _S51 = "Roll-over color";
    private static final String _S52 = "Theme";
    private static final String _S53 = "Additional themes can be downloaded from the Magarena forum using the link below.";
    private static final String _S54 = "more themes online...";

    private final static GeneralConfig CONFIG = GeneralConfig.getInstance();

    private final JCheckBox customBackgroundCheckBox;
    private final ColorButton rollOverColorButton;
    private JComboBox<String> themeComboBox;

    ThemesPanel(MouseListener aListener) {

        customBackgroundCheckBox = new JCheckBox("", CONFIG.isCustomBackground());
        customBackgroundCheckBox.setToolTipText(UiString.get(_S47));
        customBackgroundCheckBox.setFocusable(false);
        customBackgroundCheckBox.addMouseListener(aListener);

        rollOverColorButton = new ColorButton(MagicStyle.getRolloverColor());
        rollOverColorButton.setFocusable(false);

        setLayout(new MigLayout("flowx, wrap 2, insets 16, gapy 8", "[46%][54%]"));
        add(getThemeSettingPanel(aListener), "spanx 2, w 100%");
        add(new JLabel(UiString.get(_S49)), "alignx right");
        add(customBackgroundCheckBox);
        add(new JLabel(UiString.get(_S51)), "alignx right");
        add(rollOverColorButton);

    }

    private JPanel getThemeSettingPanel(MouseListener aListener) {
        // Theme setting
        final JLabel themeLabel = new JLabel(UiString.get(_S52));
        themeComboBox = new JComboBox<>(ThemeFactory.getThemeNames());
        themeComboBox.setToolTipText(UiString.get(_S53));
        themeComboBox.addMouseListener(aListener);
        themeComboBox.setFocusable(false);
        themeComboBox.setSelectedItem(CONFIG.getTheme());
        // link to more themes online
        final JLabel linkLabel = new LinkLabel(UiString.get(_S54), URLUtils.URL_THEMES);
        // layout
        final JPanel panel = new JPanel(new MigLayout("flowx, wrap 2, insets 0, gapy 0", "[46%][54%]"));
        panel.add(themeLabel, "alignx right");
        panel.add(themeComboBox, "alignx left");
        panel.add(new JLabel(), "alignx right");
        panel.add(linkLabel, "alignx left");
        return panel;
    }

    void saveSettings() {
        CONFIG.setTheme(themeComboBox.getItemAt(themeComboBox.getSelectedIndex()));
        CONFIG.setCustomBackground(customBackgroundCheckBox.isSelected());
        CONFIG.setRolloverColor(rollOverColorButton.getColor());
    }

}
