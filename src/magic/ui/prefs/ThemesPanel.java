package magic.ui.prefs;

import java.awt.Cursor;
import java.awt.event.ItemEvent;
import java.awt.event.MouseListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.translate.UiString;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.ColorButton;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ThemesPanel extends JPanel {

    // translatable strings.
    private static final String _S1 = "This color is read-only.";
    private static final String _S2 = "Please remove 'color_mouseover' from the 'theme.properties' file to enable color selection.";
    private static final String _S3 = "This is the default color as 'color_mouseover' is not defined in the 'theme.properties' file.";
    private static final String _S47 = "Overrides the default theme background with a custom image which is set by dragging an image file onto the Magarena window.";
    private static final String _S49 = "custom background";
    private static final String _S51 = "highlight color";

    private static final String READONLY_COLOR_TIP = String.format("<b>%s</b><br>%s", UiString.get(_S1), UiString.get(_S2));
    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();

    private final JCheckBox customBackgroundCheckBox;
    private final ColorButton rollOverColorButton;
    private final ThemesComboBox themeComboBox;
    private Theme selectedTheme = ThemeFactory.getInstance().getCurrentTheme();

    ThemesPanel(MouseListener aListener) {

        rollOverColorButton = new ColorButton(MagicStyle.getRolloverColor());
        rollOverColorButton.setFocusable(false);
        rollOverColorButton.addMouseListener(aListener);

        themeComboBox = new ThemesComboBox();
        themeComboBox.setFocusable(false);
        themeComboBox.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                SwingUtilities.invokeLater(() -> {
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    if (getSelectedThemeName().equals(selectedTheme.getName())) {
                        setTheme(selectedTheme);
                    } else {
                        setTheme(ThemeFactory.getInstance().loadTheme(getSelectedThemeName()));
                    }
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                });
            }
        });
        themeComboBox.setSelectedItem(selectedTheme.getName());

        customBackgroundCheckBox = new JCheckBox("", CONFIG.isCustomBackground());
        customBackgroundCheckBox.setToolTipText(UiString.get(_S47));
        customBackgroundCheckBox.setFocusable(false);
        customBackgroundCheckBox.addMouseListener(aListener);

        setLayout(new MigLayout("flowx, wrap 2, insets 16, gapy 0"));
        add(themeComboBox, "w 100%");
        add(new ThemesActionPanel(this, aListener));
        add(getSettingsPanel(), "w 100%, h 100%, spanx 2");
    }

    private void setTheme(Theme aTheme) {
        selectedTheme = aTheme;
        rollOverColorButton.setColor(MagicStyle.getRolloverColor(aTheme));
        rollOverColorButton.setLocked(false);
        rollOverColorButton.setToolTipText(UiString.get(_S3));
        if (aTheme.hasValue(Theme.COLOR_MOUSEOVER)) {
            rollOverColorButton.setToolTipText(READONLY_COLOR_TIP);
            rollOverColorButton.setLocked(true);
        }
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
        if (!selectedTheme.hasValue(Theme.COLOR_MOUSEOVER)) {
            CONFIG.setRolloverColor(rollOverColorButton.getColor());
        }
    }

    String getSelectedThemeName() {
        return themeComboBox.getItemAt(themeComboBox.getSelectedIndex());
    }

}
