package magic.ui.dialog.prefs;

import java.awt.Cursor;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.data.settings.BooleanSetting;
import magic.data.settings.IntegerSetting;
import magic.data.settings.StringSetting;
import magic.translate.MText;
import magic.ui.ScreenController;
import magic.ui.helpers.LaFHelper;
import magic.ui.mwidgets.MCheckBox;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.ColorButton;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ThemesPanel extends JPanel
    implements ItemListener {

    // translatable strings.
    private static final String _S1 = "This color is read-only.";
    private static final String _S2 = "Please remove 'color_mouseover' from the 'theme.properties' file to enable color selection.";
    private static final String _S3 = "This is the default color as 'color_mouseover' is not defined in the 'theme.properties' file.";
    private static final String _S4 = "Overrides the default theme background with a custom image which is set by dragging an image file onto the Magarena window.";
    private static final String _S5 = "Custom background";
    private static final String _S6 = "Highlight color";
    private static final String _S7 = "Custom scrollbar";
    private static final String _S8 = "Minimalist style that fits better with the intended UI look and feel.";

    private static final String READONLY_COLOR_TIP = String.format("<b>%s</b><br>%s", MText.get(_S1), MText.get(_S2));

    private final MCheckBox cbCustomScrollbar;
    private final MCheckBox customBackgroundCheckBox;
    private final ColorButton rollOverColorButton;
    private final ThemesComboBox themeComboBox;

    private Theme selectedTheme = ThemeFactory.getInstance().getCurrentTheme();
    private boolean refreshLAF = false;

    ThemesPanel(MouseListener aListener) {

        rollOverColorButton = new ColorButton(MagicStyle.getRolloverColor());
        rollOverColorButton.setFocusable(false);
        rollOverColorButton.addMouseListener(aListener);

        themeComboBox = new ThemesComboBox();
        themeComboBox.setFocusable(false);
        themeComboBox.setSelectedItem(selectedTheme.getName());
        themeComboBox.addItemListener(this);

        customBackgroundCheckBox = new MCheckBox(MText.get(_S5), GeneralConfig.get(BooleanSetting.CUSTOM_BACKGROUND));
        customBackgroundCheckBox.setToolTipText(MText.get(_S4));
        customBackgroundCheckBox.setFocusable(false);
        customBackgroundCheckBox.addMouseListener(aListener);

        cbCustomScrollbar = new MCheckBox(MText.get(_S7), GeneralConfig.get(BooleanSetting.CUSTOM_SCROLLBAR));
        cbCustomScrollbar.setToolTipText(MText.get(_S8));
        cbCustomScrollbar.setFocusable(false);
        cbCustomScrollbar.addMouseListener(aListener);
        cbCustomScrollbar.addItemListener(this);


        setLayout(new MigLayout("flowy, insets 16, gapy 12", "[fill, grow]"));
        add(getThemeLayoutPanel(aListener));
        add(customBackgroundCheckBox.component());
        add(cbCustomScrollbar.component());
    }

    private void setTheme(Theme aTheme) {
        selectedTheme = aTheme;
        rollOverColorButton.setColor(MagicStyle.getRolloverColor(aTheme));
        rollOverColorButton.setLocked(false);
        rollOverColorButton.setToolTipText(MText.get(_S3));
        if (aTheme.hasValue(Theme.COLOR_MOUSEOVER)) {
            rollOverColorButton.setToolTipText(READONLY_COLOR_TIP);
            rollOverColorButton.setLocked(true);
        }
    }

    private JPanel getThemeLayoutPanel(MouseListener aListener) {
        JPanel panel = new JPanel(new MigLayout("wrap 3, insets 0 0 10 0",
            "[][grow][]"));
        panel.add(themeComboBox, "spanx 2, w 100%");
        panel.add(new ThemesActionPanel(this, aListener));
        panel.add(rollOverColorButton, "w 24!");
        panel.add(new JLabel(MText.get(_S6)), "spanx 2");
        panel.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, getBackground().darker()));
        return panel;
    }

    private void refreshLAF() {
        if (refreshLAF) {
            refreshLAF = false;
            LaFHelper.setDefaultLookAndFeel();
            SwingUtilities.updateComponentTreeUI(ScreenController.getFrame());
        }
    }

    void saveSettings() {
        GeneralConfig.set(BooleanSetting.CUSTOM_SCROLLBAR, cbCustomScrollbar.isSelected());
        GeneralConfig.set(StringSetting.THEME, getSelectedThemeName());
        GeneralConfig.set(BooleanSetting.CUSTOM_BACKGROUND, customBackgroundCheckBox.isSelected());
        if (!selectedTheme.hasValue(Theme.COLOR_MOUSEOVER)) {
            GeneralConfig.set(IntegerSetting.ROLLOVER_RGB, rollOverColorButton.getColor().getRGB());
        }
        refreshLAF();
    }

    String getSelectedThemeName() {
        return themeComboBox.getItemAt(themeComboBox.getSelectedIndex());
    }

    private void doThemeItemStateChanged(ItemEvent e) {
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
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        refreshLAF = e.getSource() == cbCustomScrollbar.component();
        if (e.getSource() == themeComboBox) {
            doThemeItemStateChanged(e);
        }
    }

}
