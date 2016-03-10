package magic.ui.prefs;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.text.NumberFormatter;
import magic.data.GeneralConfig;
import magic.translate.UiString;
import magic.ui.MagicFrame;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.ui.theme.Theme;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.SliderPanel;
import magic.ui.utility.MagicStyle;
import magic.ui.dialog.button.CancelButton;
import magic.ui.dialog.button.SaveButton;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class PreferencesDialog
        extends JDialog
        implements ActionListener, MouseListener, WindowListener {

    // translatable strings.
    private static final String _S3 = "General";
    private static final String _S4 = "Gameplay";
    private static final String _S5 = "Theme";
    private static final String _S6 = "Audio";
    private static final String _S7 = "Network";
    private static final String _S12 = "Proxy:";
    private static final String _S13 = "URL:";
    private static final String _S14 = "Port:";
    private static final String _S15 = "[Experimental] Suppress AI action prompts";
    private static final String _S16 = "If enabled, hides AI prompts in the user action panel. Only prompts that require you to make a decision will be shown.";
    private static final String _S17 = "Use Mulligan screen";
    private static final String _S20 = "Double-click to cast or activate ability (for touchscreen)";
    private static final String _S21 = "Automatically pass priority";
    private static final String _S22 = "When the only option is to pass don't prompt player, just pass immediately.";
    private static final String _S23 = "Always pass during draw and begin of combat step";
    private static final String _S24 = "Limit options for human player to those available to the AI";
    private static final String _S25 = "Positive effects, such as pump and untap, can only be applied to your own permanents. Negative effects, such as destroy and exile, can only be applied to opponent's permanents.";
    private static final String _S26 = "Message:";
    private static final String _S27 = "The duration in milliseconds (1000 = 1 second) that the game pauses when an item is added to the stack. This has no effect unless the 'Automatically pass priority' option is enabled.";
    private static final String _S37 = "Proxy settings are invalid!";
    private static final String _S68 = "Images";
    private static final String _S79 = "Preferences";
    private static final String _S80 = "There is a problem reading the translation file.";
    private static final String _S81 = "Please ensure the file is encoded as 'UTF-8 without BOM'.";
    private static final String _S82 = "Animations";

    private final static GeneralConfig config = GeneralConfig.getInstance();

    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent actionEvent) {
            dispose();
        }
    };

    private final MagicFrame frame;
    private final JComboBox<Proxy.Type> proxyComboBox = new JComboBox<>();
    private final JTextField proxyAddressTextField = new JTextField();
    private final JSpinner proxyPortSpinner = new JSpinner(new SpinnerNumberModel());
    private JCheckBox touchscreenCheckBox;
    private JCheckBox skipSingleCheckBox;
    private JCheckBox alwaysPassCheckBox;
    private JCheckBox smartTargetCheckBox;
    private SliderPanel messageDelaySlider;
    private JButton saveButton;
    private JButton cancelButton;
    private JCheckBox mulliganScreenCheckbox;
    private JCheckBox hideAIPromptCheckBox;

    private final GeneralPanel generalPanel;
    private final AnimationsPanel animationsPanel;
    private final GameplayImagesPanel gameImagesPanel;
    private final AudioPanel audioPanel;
    private final ThemesPanel themesPanel;

    private final JLabel hintLabel = new JLabel();
    private boolean isProxyUpdated = false;
    private boolean isRestartRequired = false;
    private final boolean isGamePlayMode;

    public PreferencesDialog(final MagicFrame frame, final boolean isGamePlayMode) {

        super(frame, true);
        this.isGamePlayMode = isGamePlayMode;

        this.setTitle(UiString.get(_S79));
        this.setSize(460, 530);
        this.setLocationRelativeTo(ScreenController.getMainFrame());
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        ((JComponent) getContentPane()).setBorder(BorderFactory.createMatteBorder(0, 8, 8, 8, MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND)));

        this.frame = frame;

        generalPanel = new GeneralPanel(this);
        animationsPanel = new AnimationsPanel(this);
        gameImagesPanel = new GameplayImagesPanel(this);
        audioPanel = new AudioPanel(this);
        themesPanel = new ThemesPanel(this);

        hintLabel.setVerticalAlignment(SwingConstants.TOP);
        hintLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        hintLabel.setVerticalTextPosition(SwingConstants.TOP);
        // hint label replaces tooltips.
        ToolTipManager.sharedInstance().setEnabled(false);

        getContentPane().setLayout(new MigLayout("flowy, insets 0, gapy 0"));
        getContentPane().add(getDialogCaptionLabel(), "w 100%, h 26!");
        getContentPane().add(getTabbedSettingsPane(), "w 10:100%, h 100%");
        getContentPane().add(hintLabel, "w 100%, h 76!, gapx 10 10");
        getContentPane().add(getActionButtonsPanel(), "w 100%, aligny bottom, pushy");

        setEscapeKeyAsCancelAction();
        addWindowListener(this);

        setVisible(true);
    }

    private JLabel getDialogCaptionLabel() {
        final JLabel lbl = new JLabel(getTitle());
        lbl.setOpaque(true);
        lbl.setBackground(MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND));
        lbl.setForeground(MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_FOREGROUND));
        lbl.setFont(FontsAndBorders.FONT1.deriveFont(14f));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        return lbl;
    }

    private void setEscapeKeyAsCancelAction() {
        final KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private JTabbedPane getTabbedSettingsPane() {
        final JTabbedPane tabbedPane = new JTabbedPane();
        if (isGamePlayMode) {
            tabbedPane.addTab(UiString.get(_S4), getGameplaySettingsTabbedPane());
            tabbedPane.addTab(UiString.get(_S5), themesPanel);
            tabbedPane.addTab(UiString.get(_S6), audioPanel);
        } else {
            tabbedPane.addTab(UiString.get(_S3), generalPanel);
            tabbedPane.addTab(UiString.get(_S4), getGameplaySettingsTabbedPane());
            tabbedPane.addTab(UiString.get(_S5), themesPanel);
            tabbedPane.addTab(UiString.get(_S6), audioPanel);
            tabbedPane.addTab(UiString.get(_S7), getNetworkSettingsPanel());
        }
        return tabbedPane;
    }

    private JPanel getNetworkSettingsPanel() {
        final Proxy proxy = config.getProxy();
        //
        proxyComboBox.setModel(new DefaultComboBoxModel<>(Proxy.Type.values()));
        proxyComboBox.setFocusable(false);
        proxyComboBox.addActionListener(this);
        proxyComboBox.setSelectedItem(proxy.type());
        //
        // allow only numeric characters to be recognised.
        proxyPortSpinner.setEditor(new JSpinner.NumberEditor(proxyPortSpinner, "#"));
        final JFormattedTextField txt1 = ((JSpinner.NumberEditor) proxyPortSpinner.getEditor()).getTextField();
        ((NumberFormatter) txt1.getFormatter()).setAllowsInvalid(false);
        //
        if (proxy != Proxy.NO_PROXY) {
            proxyAddressTextField.setText(proxy.address().toString());
            proxyPortSpinner.setValue(((InetSocketAddress) proxy.address()).getPort());
        }
        // layout components
        final JPanel panel = new JPanel(new MigLayout("flowx, wrap 2, insets 16, gapy 4"));
        panel.add(getCaptionLabel(UiString.get(_S12)));
        panel.add(proxyComboBox, "w 140!");
        panel.add(getCaptionLabel(UiString.get(_S13)));
        panel.add(proxyAddressTextField, "w 100%");
        panel.add(getCaptionLabel(UiString.get(_S14)));
        panel.add(proxyPortSpinner, "w 60!");
        return panel;
    }

    private Proxy getNewProxy() {
        if (proxyComboBox.getSelectedItem() == Proxy.Type.DIRECT) {
            return Proxy.NO_PROXY;
        } else {
            final Proxy.Type proxyType = (Proxy.Type) proxyComboBox.getSelectedItem();
            final String urlAddress = proxyAddressTextField.getText().trim();
            final String portString = proxyPortSpinner.getValue().toString();
            final int portNumber = portString.isEmpty() ? 0 : Integer.parseInt(portString);
            return new Proxy(proxyType, new InetSocketAddress(urlAddress, portNumber));
        }
    }

    private void updateProxy() {
        final boolean use = proxyComboBox.getSelectedItem() != Proxy.Type.DIRECT;
        proxyAddressTextField.setEnabled(use);
        proxyPortSpinner.setEnabled(use);
        if (use) {
            proxyAddressTextField.requestFocus();
        }
        isProxyUpdated = true;
    }

    private String getAsHtml(String text) {
        return String.format("<html>%s</html>", text);
    }

    private void setButtonPropertyDefaults(final AbstractButton btn) {
        btn.setFocusable(false);
        btn.setVerticalTextPosition(SwingConstants.TOP);
        btn.addMouseListener(this);
    }

    private JScrollPane getGameplaySettingsPanel1() {

        hideAIPromptCheckBox = new JCheckBox(getAsHtml(UiString.get(_S15)), config.getHideAiActionPrompt());
        hideAIPromptCheckBox.setToolTipText(UiString.get(_S16));
        setButtonPropertyDefaults(hideAIPromptCheckBox);

        mulliganScreenCheckbox = new JCheckBox(getAsHtml(UiString.get(_S17)), config.getMulliganScreenActive());
        setButtonPropertyDefaults(mulliganScreenCheckbox);

        touchscreenCheckBox = new JCheckBox(getAsHtml(UiString.get(_S20)), config.isTouchscreen());
        setButtonPropertyDefaults(touchscreenCheckBox);

        skipSingleCheckBox = new JCheckBox(getAsHtml(UiString.get(_S21)), config.getSkipSingle());
        skipSingleCheckBox.setToolTipText(UiString.get(_S22));
        setButtonPropertyDefaults(skipSingleCheckBox);

        alwaysPassCheckBox = new JCheckBox(getAsHtml(UiString.get(_S23)), config.getAlwaysPass());
        setButtonPropertyDefaults(alwaysPassCheckBox);

        smartTargetCheckBox = new JCheckBox(getAsHtml(UiString.get(_S24)), config.getSmartTarget());
        smartTargetCheckBox.setToolTipText(UiString.get(_S25));
        setButtonPropertyDefaults(smartTargetCheckBox);

        messageDelaySlider = new SliderPanel(getAsHtml(UiString.get(_S26)), 0, 3000, 500, config.getMessageDelay());
        messageDelaySlider.setToolTipText(UiString.get(_S27));
        messageDelaySlider.addMouseListener(this);

        final ScrollablePanel panel = new ScrollablePanel(new MigLayout("flowy, insets 16, gapy 10"));
        panel.setScrollableWidth(ScrollablePanel.ScrollableSizeHint.FIT);

        panel.add(hideAIPromptCheckBox);
        panel.add(mulliganScreenCheckbox);
        panel.add(touchscreenCheckBox);
        panel.add(skipSingleCheckBox);
        panel.add(alwaysPassCheckBox);
        panel.add(smartTargetCheckBox);
        panel.add(messageDelaySlider, "w 100%");

        final JScrollPane scroller = new JScrollPane(panel);
        scroller.getVerticalScrollBar().setUnitIncrement(18);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        return scroller;

    }

    private void saveSettings() {
        animationsPanel.saveSettings();
        gameImagesPanel.saveSettings();
        audioPanel.saveSettings();
        themesPanel.saveSettings();
        config.setTouchscreen(touchscreenCheckBox.isSelected());
        config.setSkipSingle(skipSingleCheckBox.isSelected());
        config.setAlwaysPass(alwaysPassCheckBox.isSelected());
        config.setSmartTarget(smartTargetCheckBox.isSelected());
        config.setMessageDelay(messageDelaySlider.getValue());
        config.setMulliganScreenActive(mulliganScreenCheckbox.isSelected());
        config.setHideAiActionPrompt(hideAIPromptCheckBox.isSelected());

        if (isGamePlayMode == false) {
            generalPanel.saveSettings();
            // Network
            config.setProxy(getNewProxy());
        }

        config.save();
    }

    private void doSaveButtonAction() {

        final boolean isNewTranslation = !config.getTranslation().equals(generalPanel.getLanguage());

        saveSettings();

        if (isNewTranslation) {
            if (!setNewTranslation()) {
                return;
            }
        }

        MagicImages.clearCache();
        frame.refreshUI();

        dispose();

    }

    private boolean setNewTranslation() {
        try {
            UiString.loadTranslationFile();
            isRestartRequired = true;
            return true;

        } catch (NumberFormatException ex) {
            UiString.disableTranslations();
            System.err.println(ex);
            ScreenController.showWarningMessage(String.format("%s\n%s\n\n%s",
                    UiString.get(_S80), UiString.get(_S81), ex)
            );
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    config.setTranslation(GeneralConfig.DEFAULT_TRANSLATION);
                    config.save();
                    generalPanel.refreshLanguageCombo();
                }
            });
            return false;

        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        final Object source = event.getSource();
        if (source == saveButton) {
            if (validateSettings()) {
                doSaveButtonAction();
            }
        } else if (source == cancelButton) {
            dispose();
        } else if (source == proxyComboBox) {
            updateProxy();
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private boolean validateSettings() {
        if (isGamePlayMode == false) {
            if (isProxyUpdated && !isProxyValid()) {
                ScreenController.showWarningMessage(UiString.get(_S37));
                return false;
            }
        }
        return true;
    }

    private boolean isProxyValid() {
        try {
            getNewProxy();
            return true;
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    private JButton getCancelButton() {
        cancelButton = new CancelButton();
        cancelButton.setFocusable(false);
        cancelButton.addActionListener(this);
        return cancelButton;
    }

    private JButton getSaveButton() {
        saveButton = new SaveButton();
        saveButton.setFocusable(false);
        saveButton.addActionListener(this);
        return saveButton;
    }

    private JPanel getActionButtonsPanel() {
        final JPanel buttonPanel = new JPanel(new MigLayout("insets 5, gapx 5, flowx"));
        buttonPanel.add(getCancelButton(), "alignx right, pushx");
        buttonPanel.add(getSaveButton());
        return buttonPanel;
    }

    /*
     * MouseListener overrides
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() instanceof JComponent) {
            final JComponent c = (JComponent) e.getSource();
            String caption = "";
            if (c instanceof AbstractButton) {
                caption = ((AbstractButton) c).getText();
            }
            hintLabel.setText(String.format("<html>%s</html>",
                    c.getToolTipText() == null ? caption : c.getToolTipText()));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() instanceof JComponent) {
            hintLabel.setText("");
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /*
     * WindowListener overrides
     */
    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
        ToolTipManager.sharedInstance().setEnabled(true);
    }

    @Override
    public void windowClosing(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    private JLabel getCaptionLabel(final String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FontsAndBorders.FONT1);
        return lbl;
    }

    private JTabbedPane getGameplaySettingsTabbedPane() {
        final JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab(UiString.get(_S3), getGameplaySettingsPanel1());
        tabbedPane.addTab(UiString.get(_S68), gameImagesPanel);
        tabbedPane.addTab(UiString.get(_S82), animationsPanel);
        return tabbedPane;
    }

    public boolean isRestartRequired() {
        return isRestartRequired;
    }

}
