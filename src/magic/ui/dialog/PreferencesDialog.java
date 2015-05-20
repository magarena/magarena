package magic.ui.dialog;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.text.ParseException;
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
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;
import magic.data.GeneralConfig;
import magic.data.MagicIcon;
import magic.ui.CachedImagesProvider;
import magic.ui.IconImages;
import magic.ui.URLUtils;
import magic.ui.CardImagesProvider;
import magic.ui.MagicFrame;
import magic.ui.ScreenController;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.DirectoryChooser;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.LinkLabel;
import magic.ui.widget.SliderPanel;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class PreferencesDialog
    extends JDialog
    implements ActionListener, MouseListener, WindowListener {

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
    private JComboBox<String> themeComboBox;
    private JComboBox<String> highlightComboBox;
    private JCheckBox soundCheckBox;
    private JCheckBox touchscreenCheckBox;
    private JCheckBox highQualityCheckBox;
    private JCheckBox skipSingleCheckBox;
    private JCheckBox alwaysPassCheckBox;
    private JCheckBox smartTargetCheckBox;
    private JCheckBox mouseWheelPopupCheckBox;
    private SliderPanel popupDelaySlider;
    private SliderPanel messageDelaySlider;
    private JButton okButton;
    private JButton cancelButton;
    private JCheckBox previewCardOnSelectCheckBox;
    private JCheckBox gameLogCheckBox;
    private JCheckBox mulliganScreenCheckbox;
    private JCheckBox missingCardDataCheckbox;
    private DirectoryChooser imagesFolderChooser;
    private JCheckBox animateGameplayCheckBox;
    private JCheckBox customBackgroundCheckBox;
    private JSpinner newTurnAlertSpinner;
    private JSpinner landAnimationSpinner;
    private JSpinner nonLandAnimationSpinner;
    private JCheckBox splitViewDeckEditorCheckBox;
    private JCheckBox popupScaleContextCheckbox;
    private SliderPanel popupScaleSlider;
    private JCheckBox uiSoundCheckBox;
    private JCheckBox pauseGamePopupCheckBox;
    private JCheckBox hideAIPromptCheckBox;

    private final JLabel hintLabel = new JLabel();
    private boolean isProxyUpdated = false;

    public PreferencesDialog(final MagicFrame frame) {

        super(frame,true);
        this.setTitle("Preferences");
        this.setSize(440,500);
        this.setLocationRelativeTo(frame);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        ((JComponent)getContentPane()).setBorder(BorderFactory.createMatteBorder(0, 8, 8, 8, MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND)));

        this.frame=frame;

        hintLabel.setVerticalAlignment(SwingConstants.TOP);
        hintLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        hintLabel.setIcon(IconImages.getIcon(MagicIcon.MISSING_ICON));
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
        tabbedPane.addTab("General", getGeneralTabPanel());
        tabbedPane.addTab("Gameplay", getGameplaySettingsTabbedPane());
        tabbedPane.addTab("Look & Feel", getLookAndFeelSettingsPanel());
        tabbedPane.addTab("Network", getNetworkSettingsPanel());
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
        ((NumberFormatter)txt1.getFormatter()).setAllowsInvalid(false);
        //
        if (proxy != Proxy.NO_PROXY) {
            proxyAddressTextField.setText(proxy.address().toString());
            proxyPortSpinner.setValue(((InetSocketAddress)proxy.address()).getPort());
        }
        // layout components
        final JPanel panel = new JPanel(new MigLayout("flowx, wrap 2, insets 16, gapy 4"));
        panel.add(getCaptionLabel("Proxy:"));
        panel.add(proxyComboBox, "w 140!");
        panel.add(getCaptionLabel("URL:"));
        panel.add(proxyAddressTextField, "w 100%");
        panel.add(getCaptionLabel("Port:"));
        panel.add(proxyPortSpinner, "w 60!");
        return panel;
     }

    private Proxy getNewProxy() {
        if (proxyComboBox.getSelectedItem() == Proxy.Type.DIRECT) {
            return Proxy.NO_PROXY;
        } else {
            final Proxy.Type proxyType = (Proxy.Type)proxyComboBox.getSelectedItem();
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

    private JPanel getGameplaySettingsPanel1() {

        hideAIPromptCheckBox = new JCheckBox("[Experimental] Suppress AI action prompts", config.getHideAiActionPrompt());
        hideAIPromptCheckBox.setToolTipText("If enabled, hides AI prompts in the user action panel. Only prompts that require you to make a decision will be shown.");
        hideAIPromptCheckBox.setFocusable(false);
        hideAIPromptCheckBox.addMouseListener(this);

        mulliganScreenCheckbox = new JCheckBox("Use Mulligan screen", config.getMulliganScreenActive());
        mulliganScreenCheckbox.setFocusable(false);
        mulliganScreenCheckbox.addMouseListener(this);

        gameLogCheckBox = new JCheckBox("Show game log messages.", config.isLogMessagesVisible());
        gameLogCheckBox.setToolTipText("Clear this option if you would prefer the game log messages to be hidden by default. You can still toggle visibility during a game by clicking on the log titlebar.");
        gameLogCheckBox.setFocusable(false);
        gameLogCheckBox.addMouseListener(this);

        soundCheckBox = new JCheckBox("Enable sound effects",config.isSound());
        soundCheckBox.setToolTipText("Enables or disables sound effects during a game, such as end of turn, stack resolution, combat damage and win/lose sounds.");
        soundCheckBox.setFocusable(false);
        soundCheckBox.addMouseListener(this);

        touchscreenCheckBox = new JCheckBox("Double-click to cast or activate ability (for touchscreen)",config.isTouchscreen());
        touchscreenCheckBox.setFocusable(false);
        touchscreenCheckBox.addMouseListener(this);

        skipSingleCheckBox = new JCheckBox("Automatically pass priority", config.getSkipSingle());
        skipSingleCheckBox.setToolTipText("When the only option is to pass don't prompt player, just pass immediately.");
        skipSingleCheckBox.setFocusable(false);
        skipSingleCheckBox.addMouseListener(this);

        alwaysPassCheckBox = new JCheckBox("Always pass during draw and begin of combat step", config.getAlwaysPass());
        alwaysPassCheckBox.setFocusable(false);
        alwaysPassCheckBox.addMouseListener(this);

        smartTargetCheckBox=new JCheckBox("Allow only sensible choices", config.getSmartTarget());
        smartTargetCheckBox.setToolTipText("Prevents you from choosing your own permanents for negative effects (eg. \"Destroy target creature\") or alternatively choosing an opponent's permanent for positive effects (eg. Giant Growth).");
        smartTargetCheckBox.setFocusable(false);
        smartTargetCheckBox.addMouseListener(this);

        messageDelaySlider = new SliderPanel("Message", IconImages.getIcon(MagicIcon.DELAY), 0, 3000, 500, config.getMessageDelay());
        messageDelaySlider.setToolTipText("The duration in milliseconds (1000 = 1 second) that the game pauses when an item is added to the stack. This has no effect unless the 'Automatically pass priority' option is enabled.");
        messageDelaySlider.addMouseListener(this);

        // layout components
        final JPanel mainPanel = new JPanel(new MigLayout("flowy, insets 16, gapy 10"));
        mainPanel.add(hideAIPromptCheckBox);
        mainPanel.add(mulliganScreenCheckbox);
        mainPanel.add(gameLogCheckBox);
        mainPanel.add(soundCheckBox);
        mainPanel.add(touchscreenCheckBox);
        mainPanel.add(skipSingleCheckBox);
        mainPanel.add(alwaysPassCheckBox);
        mainPanel.add(smartTargetCheckBox);
        mainPanel.add(messageDelaySlider);
        return mainPanel;

    }

    private JPanel getGameplaySettingsPanel2() {


        mouseWheelPopupCheckBox = new JCheckBox("Popup card image using mouse wheel.", config.isMouseWheelPopup());
        mouseWheelPopupCheckBox.setFocusable(false);
        mouseWheelPopupCheckBox.setToolTipText("Manually display the card image popup by moving the mouse wheel forwards. Overrides the Auto-Popup delay.");
        mouseWheelPopupCheckBox.addMouseListener(this);

        popupDelaySlider=new SliderPanel("Popup Delay", null, 0, 2000, 50, config.getPopupDelay());
        popupDelaySlider.setToolTipText("Automatically displays the card popup image after the specified number of milliseconds that the mouse cursor hovers over a card.");
        popupDelaySlider.addMouseListener(this);

        final Dimension maxCardSize = CardImagesProvider.MAXIMUM_CARD_SIZE;

        popupScaleContextCheckbox = new JCheckBox("Scale popup to screen size", config.isCardPopupScaledToScreen());
        popupScaleContextCheckbox.setToolTipText("If enabled then the popup size is scaled against the current screen size otherwise it is scaled against the maximum card size. The popup size will not exceed the screen or maximum card size, whichever is smaller. The maximum card size is (W=" + maxCardSize.width + ", H=" + maxCardSize.height + ").");
        popupScaleContextCheckbox.addMouseListener(this);

        final int popupScale = (int)(config.getCardPopupScale() * 100);
        popupScaleSlider = new SliderPanel("Popup Scale", null, 60, 100, 1, popupScale);
        popupScaleSlider.setToolTipText("Sets the size of the card popup image as a percentage of the screen size or maximum card size based on the \"Scale popup to screen size\" setting.");
        popupScaleSlider.addMouseListener(this);

        pauseGamePopupCheckBox = new JCheckBox("Pause game on popup.", config.isGamePausedOnPopup());
        pauseGamePopupCheckBox.setFocusable(false);
        pauseGamePopupCheckBox.setToolTipText("Pauses the game while the popup is open.");
        pauseGamePopupCheckBox.addMouseListener(this);

        // layout components
        final JPanel panel = new JPanel(new MigLayout("flowy, insets 16, gapy 10"));
        panel.add(mouseWheelPopupCheckBox);
        panel.add(popupDelaySlider);
        panel.add(popupScaleContextCheckbox);
        panel.add(popupScaleSlider);
        panel.add(pauseGamePopupCheckBox);
        return panel;

    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final Object source=event.getSource();
        if (source==okButton) {
            if (validateSettings()) {
                config.setTheme(themeComboBox.getItemAt(themeComboBox.getSelectedIndex()));
                config.setHighlight(highlightComboBox.getItemAt(highlightComboBox.getSelectedIndex()));
                config.setSound(soundCheckBox.isSelected());
                config.setTouchscreen(touchscreenCheckBox.isSelected());
                config.setHighQuality(highQualityCheckBox.isSelected());
                config.setSkipSingle(skipSingleCheckBox.isSelected());
                config.setAlwaysPass(alwaysPassCheckBox.isSelected());
                config.setSmartTarget(smartTargetCheckBox.isSelected());
                config.setMouseWheelPopup(mouseWheelPopupCheckBox.isSelected());
                config.setPopupDelay(popupDelaySlider.getValue());
                config.setMessageDelay(messageDelaySlider.getValue());
                config.setPreviewCardOnSelect(previewCardOnSelectCheckBox.isSelected());
                config.setLogMessagesVisible(gameLogCheckBox.isSelected());
                config.setMulliganScreenActive(mulliganScreenCheckbox.isSelected());
                config.setCustomBackground(customBackgroundCheckBox.isSelected());
                config.setShowMissingCardData(missingCardDataCheckbox.isSelected());
                config.setCardImagesPath(imagesFolderChooser.getPath());
                config.setAnimateGameplay(animateGameplayCheckBox.isSelected());
                config.setProxy(getNewProxy());
                config.setNewTurnAlertDuration((int)newTurnAlertSpinner.getValue());
                config.setLandPreviewDuration((int)landAnimationSpinner.getValue());
                config.setNonLandPreviewDuration((int)nonLandAnimationSpinner.getValue());
                config.setIsSplitViewDeckEditor(splitViewDeckEditorCheckBox.isSelected());
                config.setIsCardPopupScaledToScreen(popupScaleContextCheckbox.isSelected());
                config.setCardPopupScale(popupScaleSlider.getValue() / 100d);
                config.setIsUiSound(uiSoundCheckBox.isSelected());
                config.setIsGamePausedOnPopup(pauseGamePopupCheckBox.isSelected());
                config.setHideAiActionPrompt(hideAIPromptCheckBox.isSelected());
                config.save();
                CachedImagesProvider.getInstance().clearCache();
                frame.refreshUI();
                dispose();
            }
        } else if (source==cancelButton) {
            dispose();
        } else if (source==proxyComboBox) {
            updateProxy();
        }
    }

    private boolean validateSettings() {
        if (!imagesFolderChooser.isValidDirectory()) {
            ScreenController.showWarningMessage("The path for the images directory is invalid!");
            return false;
        }
        if (isProxyUpdated && !isProxyValid()) {
            ScreenController.showWarningMessage("Proxy settings are invalid!");
            return false;
        }
        try {
            newTurnAlertSpinner.commitEdit();
            landAnimationSpinner.commitEdit();
            nonLandAnimationSpinner.commitEdit();
        } catch (ParseException ex) {
            ScreenController.showWarningMessage("One of more spinner values are invalid - " + ex.getMessage());
            return false;
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

    private JPanel getActionButtonsPanel() {
        final JPanel buttonPanel = new JPanel(new MigLayout("insets 5, gapx 5, flowx"));
        // Cancel button
        cancelButton=new JButton("Cancel");
        cancelButton.setFocusable(false);
        cancelButton.setIcon(IconImages.getIcon(MagicIcon.CANCEL));
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton, "w 100!, alignx right, pushx");
        // Save button
        okButton=new JButton("Save");
        okButton.setFocusable(false);
        okButton.setIcon(IconImages.getIcon(MagicIcon.OK));
        okButton.addActionListener(this);
        buttonPanel.add(okButton, "w 100!");
        return buttonPanel;
    }

    /*
    * MouseListener overrides
    */
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() instanceof JComponent) {
            final JComponent c = (JComponent)e.getSource();
            String caption = "";
            if (c instanceof AbstractButton) {
                caption = ((AbstractButton)c).getText();
            }
            hintLabel.setText("<html>" + (c.getToolTipText() == null ? caption : c.getToolTipText()) + "</html>");
        }
    }
    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() instanceof JComponent) {
            hintLabel.setText("");
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}

    /*
     * WindowListener overrides
     */
    @Override
    public void windowActivated(WindowEvent e) {}
    @Override
    public void windowClosed(WindowEvent e) {
        ToolTipManager.sharedInstance().setEnabled(true);
    }
    @Override
    public void windowClosing(WindowEvent e) {}
    @Override
    public void windowDeactivated(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowOpened(WindowEvent e) {}

    private JPanel getLookAndFeelSettingsPanel() {

        // Card highlight setting.
        final JLabel highlightLabel = new JLabel("Highlight");
        final String[] Highlightchoices = { "none", "overlay", "border", "theme" };
        highlightComboBox = new JComboBox<>(Highlightchoices);
        highlightComboBox.setSelectedItem(config.getHighlight());
        highlightComboBox.setToolTipText("Determines the style in which cards are highlighted during a game.");
        highlightComboBox.setFocusable(false);
        highlightComboBox.addMouseListener(this);

        customBackgroundCheckBox = new JCheckBox("", config.isCustomBackground());
        customBackgroundCheckBox.setToolTipText("Overrides the default theme background with a custom image which is set by dragging an image file onto the Magarena window.");
        customBackgroundCheckBox.setFocusable(false);
        customBackgroundCheckBox.addMouseListener(this);

        highQualityCheckBox = new JCheckBox("", config.isHighQuality());
        highQualityCheckBox.setToolTipText("Enable this option if you want to view card images at the optimum size for larger screens.");
        highQualityCheckBox.setFocusable(false);
        highQualityCheckBox.addMouseListener(this);

        // Layout UI components.
        final JPanel panel = new JPanel(new MigLayout("flowx, wrap 2, insets 16, gapy 8", "[140!][]"));
        panel.add(getThemeSettingPanel(), "spanx 2, w 100%");
        panel.add(highlightLabel, "alignx right");
        panel.add(highlightComboBox, "alignx left");
        panel.add(new JLabel("Custom background"), "alignx right");
        panel.add(customBackgroundCheckBox);
        panel.add(new JLabel("Large card images"), "alignx right");
        panel.add(highQualityCheckBox);

        return panel;
    }

    private JPanel getThemeSettingPanel() {
        // Theme setting
        final JLabel themeLabel=new JLabel("Theme");
        themeComboBox=new JComboBox<>(ThemeFactory.getInstance().getThemeNames());
        themeComboBox.setToolTipText("Additional themes can be downloaded from the Magarena forum using the link below.");
        themeComboBox.addMouseListener(this);
        themeComboBox.setFocusable(false);
        themeComboBox.setSelectedItem(config.getTheme());
        // link to more themes online
        final JLabel linkLabel = new LinkLabel("more themes online...", URLUtils.URL_THEMES);
        // layout
        final JPanel panel = new JPanel(new MigLayout("flowx, wrap 2, insets 0, gapy 0", "[140!][]"));
        panel.add(themeLabel, "alignx right");
        panel.add(themeComboBox, "alignx left");
        panel.add(new JLabel(),"alignx right");
        panel.add(linkLabel, "alignx left");
        return panel;
    }


    private JPanel getGeneralTabPanel() {

        uiSoundCheckBox = new JCheckBox("Enable UI sound", config.isUiSound());
        uiSoundCheckBox.setToolTipText("Enables or disables sound effects used by the user interface in general. For example, this affects whether a sound is played when the missing images alert is displayed. See also 'Enable sound effects' in the Gameplay -> General tab.");
        uiSoundCheckBox.setFocusable(false);
        uiSoundCheckBox.addMouseListener(this);

        final JPanel panel = new JPanel(new MigLayout("flowy, gapy 14, insets 16"));
        panel.add(uiSoundCheckBox, "w 100%");
        panel.add(getCardExplorerEditorSettingsPanel(), "w 100%");
        panel.add(getDirectorySettingsPanel(), "w 100%");
        return panel;
    }

    private JPanel getCardExplorerEditorSettingsPanel() {

      splitViewDeckEditorCheckBox = new JCheckBox("Deck Editor split view.", config.isSplitViewDeckEditor());
      splitViewDeckEditorCheckBox.setToolTipText("Use the old style split view in the Deck Editor instead of the new tabbed view. This option is provided for convenience, any new features will only be added to the tabbed view.");
      splitViewDeckEditorCheckBox.setFocusable(false);
      splitViewDeckEditorCheckBox.addMouseListener(this);

      previewCardOnSelectCheckBox = new JCheckBox("Preview card on select only.", config.isPreviewCardOnSelect());
      previewCardOnSelectCheckBox.setToolTipText("By default, as you move the mouse cursor over a card entry it will display the image. If you find this a bit too sensitive then this setting will only change the image when the card entry is selected.");
      previewCardOnSelectCheckBox.setFocusable(false);
      previewCardOnSelectCheckBox.addMouseListener(this);

      missingCardDataCheckbox = new JCheckBox("Show missing card data.", config.showMissingCardData());
      missingCardDataCheckbox.setToolTipText("If set then the Card Explorer will display extra data for each missing card otherwise it will only show the card name. This setting can affect the time it takes the Card Explorer screen to open the first time it is accessed.");
      missingCardDataCheckbox.addMouseListener(this);
      previewCardOnSelectCheckBox.setFocusable(false);

      // Layout UI components.
      final JPanel panel = new JPanel(new MigLayout("flowy, insets 0"));
      panel.add(getCaptionLabel("Card Explorer & Deck Editor"));
      panel.add(splitViewDeckEditorCheckBox);
      panel.add(previewCardOnSelectCheckBox);
      panel.add(missingCardDataCheckbox);

      return panel;

  }

    private JPanel getDirectorySettingsPanel() {

        imagesFolderChooser = new DirectoryChooser(config.getCardImagesPath());
        imagesFolderChooser.setToolTipText("Location of the \"cards\" and \"tokens\" directories which contain downloaded card and token images respectively. Right click to open in file explorer.");
        imagesFolderChooser.addMouseListener(this);

        final JPanel panel = new JPanel(new MigLayout("flowy, insets 0"));
        panel.add(getCaptionLabel("Card Images Directory"));
        panel.add(imagesFolderChooser, "w 100%");

        return panel;

    }

    private JLabel getCaptionLabel(final String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FontsAndBorders.FONT1);
        return lbl;
    }

    private JTabbedPane getGameplaySettingsTabbedPane() {
        final JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("General", getGameplaySettingsPanel1());
        tabbedPane.addTab("Images", getGameplaySettingsPanel2());
        tabbedPane.addTab("Visual Cues", getVisualCueSettings());
        return tabbedPane;
    }

    private JPanel getVisualCueSettings() {

        animateGameplayCheckBox = new JCheckBox("Play animations", config.getAnimateGameplay());
        animateGameplayCheckBox.setToolTipText("Turn off animations to speed up gameplay but it will make it harder to follow the action. Left-click, Spacebar or Enter cancels the card preview animation.");
        animateGameplayCheckBox.setFocusable(false);
        animateGameplayCheckBox.addMouseListener(this);
        animateGameplayCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                landAnimationSpinner.setEnabled(animateGameplayCheckBox.isSelected());
                nonLandAnimationSpinner.setEnabled(animateGameplayCheckBox.isSelected());
            }
        });

        // layout components
        final JPanel mainPanel = new JPanel(new MigLayout("flowy, insets 16, gapy 10"));
        mainPanel.add(animateGameplayCheckBox);
        mainPanel.add(getLandAnimationPanel(), "w 100%");
        mainPanel.add(getNonLandAnimationPanel(), "w 100%");
        mainPanel.add(getnewTurnAlertPanel(), "w 100%");
        return mainPanel;

    }

    private JPanel getnewTurnAlertPanel() {
        newTurnAlertSpinner = new JSpinner(new SpinnerNumberModel(config.getNewTurnAlertDuration(), 0, 10000, 100));
        // allow only numeric characters to be recognised.
        newTurnAlertSpinner.setEditor(new JSpinner.NumberEditor(newTurnAlertSpinner,"#"));
        final JFormattedTextField txt1 = ((JSpinner.NumberEditor) newTurnAlertSpinner.getEditor()).getTextField();
        ((NumberFormatter)txt1.getFormatter()).setAllowsInvalid(false);
        //
        final JPanel panel = new JPanel(new MigLayout("insets 0"));
        panel.add(new JLabel("Display new Turn announcement for"));
        panel.add(newTurnAlertSpinner, "w 70!");
        panel.add(new JLabel("msecs"));
        panel.setToolTipText("Pauses the game for the specified duration at the start of each turn. Set to zero to disable (1000 millisecs = 1 second).");
        panel.addMouseListener(this);
        return panel;
    }

    private JPanel getLandAnimationPanel() {
        landAnimationSpinner = new JSpinner(new SpinnerNumberModel(config.getLandPreviewDuration(), 500, 20000, 100));
        // allow only numeric characters to be recognised.
        landAnimationSpinner.setEditor(new JSpinner.NumberEditor(landAnimationSpinner,"#"));
        final JFormattedTextField txt1 = ((JSpinner.NumberEditor) landAnimationSpinner.getEditor()).getTextField();
        ((NumberFormatter)txt1.getFormatter()).setAllowsInvalid(false);
        //
        final JPanel panel = new JPanel(new MigLayout("insets 0"));
        panel.add(new JLabel("Preview land card for"));
        panel.add(landAnimationSpinner, "w 70!");
        panel.add(new JLabel("msecs"));
        panel.setToolTipText("When the AI plays a land card, this setting determines how long it should be displayed at full size (1000 millisecs = 1 second).");
        panel.addMouseListener(this);
        return panel;
    }

    private JPanel getNonLandAnimationPanel() {
        nonLandAnimationSpinner = new JSpinner(new SpinnerNumberModel(config.getNonLandPreviewDuration(), 1000, 20000, 100));
        // allow only numeric characters to be recognised.
        nonLandAnimationSpinner.setEditor(new JSpinner.NumberEditor(nonLandAnimationSpinner,"#"));
        final JFormattedTextField txt1 = ((JSpinner.NumberEditor) nonLandAnimationSpinner.getEditor()).getTextField();
        ((NumberFormatter)txt1.getFormatter()).setAllowsInvalid(false);
        //
        final JPanel panel = new JPanel(new MigLayout("insets 0"));
        panel.add(new JLabel("Preview non-land card for"));
        panel.add(nonLandAnimationSpinner, "w 70!");
        panel.add(new JLabel("msecs"));
        panel.setToolTipText("When the AI plays a non-land card, this setting determines how long it should be displayed at full size (1000 millisecs = 1 second).");
        panel.addMouseListener(this);
        return panel;
    }



}
