package magic.ui.dialog;

import magic.data.CardDefinitions;
import magic.data.GeneralConfig;
import magic.data.IconImages;
import magic.ui.MagicFrame;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.DirectoryChooser;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.SliderPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.NumberFormatter;

public class PreferencesDialog
    extends JDialog
    implements ActionListener, MouseListener, WindowListener {

    private static final long serialVersionUID = 1L;
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
    private JCheckBox confirmExitCheckBox;
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
    private final JLabel hintLabel = new JLabel();
    private boolean isProxyUpdated = false;

    public PreferencesDialog(final MagicFrame frame) {

        super(frame,true);
        this.setTitle("Preferences");
        this.setSize(400,500);
        this.setLocationRelativeTo(frame);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        ((JComponent)getContentPane()).setBorder(BorderFactory.createMatteBorder(8, 8, 8, 8, ThemeFactory.getInstance().getCurrentTheme().getColor(Theme.COLOR_TITLE_BACKGROUND)));

        this.frame=frame;

        hintLabel.setVerticalAlignment(SwingConstants.TOP);
        hintLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        hintLabel.setIcon(IconImages.MISSING_ICON);
        hintLabel.setVerticalTextPosition(SwingConstants.TOP);
        // hint label replaces tooltips.
        ToolTipManager.sharedInstance().setEnabled(false);

        getContentPane().setLayout(new MigLayout("flowy, insets 0"));
        getContentPane().add(getTabbedSettingsPane(), "w 10:100%, h 100%");
        getContentPane().add(hintLabel, "w 100%, h 86!, gapx 10 10");
        getContentPane().add(getActionButtonsPanel(), "w 100%");

        setEscapeKeyAsCancelAction();
        addWindowListener(this);

        setVisible(true);
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

        animateGameplayCheckBox = new JCheckBox("Play card animation (experimental)", config.isAnimateGameplay());
        animateGameplayCheckBox.setToolTipText("When a card is played by the AI from its hand it zooms out to the center of the screen where it is displayed for a short time before zooming in to the stack or battlefield. Left-click, Spacebar or Enter cancels preview.");
        animateGameplayCheckBox.setFocusable(false);
        animateGameplayCheckBox.addMouseListener(this);

        mulliganScreenCheckbox = new JCheckBox("Use Mulligan screen", config.showMulliganScreen());
        mulliganScreenCheckbox.setFocusable(false);
        mulliganScreenCheckbox.addMouseListener(this);

        gameLogCheckBox = new JCheckBox("Show game log messages.", config.isLogMessagesVisible());
        gameLogCheckBox.setToolTipText("Clear this option if you would prefer the game log messages to be hidden by default. You can still toggle visibility during a game by clicking on the log titlebar.");
        gameLogCheckBox.setFocusable(false);
        gameLogCheckBox.addMouseListener(this);

        soundCheckBox = new JCheckBox("Enable sound effects",config.isSound());
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

        // layout components
        final JPanel mainPanel = new JPanel(new MigLayout("flowy, insets 16, gapy 10"));
        mainPanel.add(animateGameplayCheckBox);
        mainPanel.add(mulliganScreenCheckbox);
        mainPanel.add(gameLogCheckBox);
        mainPanel.add(soundCheckBox);
        mainPanel.add(touchscreenCheckBox);
        mainPanel.add(skipSingleCheckBox);
        mainPanel.add(alwaysPassCheckBox);
        mainPanel.add(smartTargetCheckBox);
        return mainPanel;

    }

    private JPanel getGameplaySettingsPanel2() {

        highQualityCheckBox = new JCheckBox("Popup card images to original size", config.isHighQuality());
        highQualityCheckBox.setFocusable(false);
        highQualityCheckBox.addMouseListener(this);

        mouseWheelPopupCheckBox = new JCheckBox("Popup card image using mouse wheel (instead of delay)", config.isMouseWheelPopup());
        mouseWheelPopupCheckBox.setFocusable(false);
        mouseWheelPopupCheckBox.addMouseListener(this);

        popupDelaySlider=new SliderPanel("Popup", IconImages.DELAY, 0, 500, 50, config.getPopupDelay());

        messageDelaySlider = new SliderPanel("Message", IconImages.DELAY, 0, 3000, 500, config.getMessageDelay());
        messageDelaySlider.setToolTipText("The duration in milliseconds (1000 = 1 second) that the game pauses when an item is added to the stack. This has no effect unless the 'Automatically pass priority' option is enabled.");
        messageDelaySlider.addMouseListener(this);

        // layout components
        final JPanel panel = new JPanel(new MigLayout("flowy, insets 16, gapy 10"));
        panel.add(highQualityCheckBox);
        panel.add(mouseWheelPopupCheckBox);
        panel.add(popupDelaySlider);
        panel.add(messageDelaySlider);
        return panel;

    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final Object source=event.getSource();
        if (source==okButton) {
            if (validateSettings()) {
                config.setTheme(themeComboBox.getItemAt(themeComboBox.getSelectedIndex()));
                config.setHighlight(highlightComboBox.getItemAt(highlightComboBox.getSelectedIndex()));
                config.setConfirmExit(confirmExitCheckBox.isSelected());
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
                config.save();
                GeneralConfig.getInstance().setIsMissingFiles(false);
                CardDefinitions.checkForMissingFiles();
                ThemeFactory.getInstance().setCurrentTheme(config.getTheme());
                frame.refreshBackground();
                frame.repaint();
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
            JOptionPane.showMessageDialog(this, "The path for the images directory is invalid!");
            return false;
        }
        if (isProxyUpdated && !isProxyValid()) {
            JOptionPane.showMessageDialog(this, "Proxy settings are invalid!");
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
        cancelButton.setIcon(IconImages.CANCEL);
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton, "w 100!, alignx right, pushx");
        // Save button
        okButton=new JButton("Save");
        okButton.setFocusable(false);
        okButton.setIcon(IconImages.OK);
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

        // Theme setting
        final JLabel themeLabel=new JLabel("Theme");
        themeComboBox=new JComboBox<>(ThemeFactory.getInstance().getThemeNames());
        themeComboBox.setToolTipText("Additional themes can be downloaded from the Magarena forum. Alternatively, to set the background image you can simply drag & drop an image file onto the Magarena window.");
        themeComboBox.addMouseListener(this);
        themeComboBox.setFocusable(false);
        themeComboBox.setSelectedItem(config.getTheme());

        // Card highlight setting.
        final JLabel highlightLabel = new JLabel("Highlight");
        final String[] Highlightchoices = { "none", "overlay", "border", "theme" };
        highlightComboBox = new JComboBox<>(Highlightchoices);
        highlightComboBox.setFocusable(false);
        highlightComboBox.setSelectedItem(config.getHighlight());

        customBackgroundCheckBox = new JCheckBox("", config.isCustomBackground());
        customBackgroundCheckBox.setToolTipText("Overrides the default theme background with a custom image which is set by dragging an image file onto the Magarena window.");
        customBackgroundCheckBox.setFocusable(false);
        customBackgroundCheckBox.addMouseListener(this);

        // Layout UI components.
        final JPanel panel = new JPanel(new MigLayout("flowx, wrap 2, insets 16, gapy 8"));
        panel.add(themeLabel, "alignx right");
        panel.add(themeComboBox, "alignx left");
        panel.add(highlightLabel, "alignx right");
        panel.add(highlightComboBox, "alignx left");
        panel.add(new JLabel("Custom background"), "alignx right");
        panel.add(customBackgroundCheckBox);

        return panel;
    }


    private JPanel getGeneralTabPanel() {
        final JPanel panel = new JPanel(new MigLayout("flowy, gapy 14, insets 16"));
        panel.add(getCardExplorerEditorSettingsPanel(), "w 100%");
        panel.add(getMiscSettingsPanel(), "w 100%");
        panel.add(getDirectorySettingsPanel(), "w 100%");
        return panel;
    }

    private JPanel getCardExplorerEditorSettingsPanel() {

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
      panel.add(previewCardOnSelectCheckBox);
      panel.add(missingCardDataCheckbox);

      return panel;

  }

    private JPanel getMiscSettingsPanel() {

        confirmExitCheckBox = new JCheckBox("Show confirmation dialog on exit", config.isConfirmExit());
        confirmExitCheckBox.setFocusable(false);
        confirmExitCheckBox.addMouseListener(this);

        final JPanel panel = new JPanel(new MigLayout("flowy, insets 0"));
        panel.add(getCaptionLabel("Misc Settings"));
        panel.add(confirmExitCheckBox);

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
        tabbedPane.addTab("1", getGameplaySettingsPanel1());
        tabbedPane.addTab("2", getGameplaySettingsPanel2());
        return tabbedPane;
    }

}
