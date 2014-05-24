package magic.ui.dialog;

import magic.data.GeneralConfig;
import magic.data.IconImages;
import magic.ui.MagicFrame;
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class PreferencesDialog
    extends JDialog
    implements ActionListener, MouseListener, WindowListener {

    private static final long serialVersionUID = 1L;
    private final static GeneralConfig config = GeneralConfig.getInstance();

    private final ActionListener actionListener = new ActionListener() {
        public void actionPerformed(final ActionEvent actionEvent) {
            dispose();
        }
    };

    private final MagicFrame frame;
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
    private final JLabel hintLabel = new JLabel();
    private boolean isCustomBackground;

    public PreferencesDialog(final MagicFrame frame) {

        super(frame,true);
        this.setTitle("Preferences");
        this.setSize(400,500);
        this.setLocationRelativeTo(frame);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.frame=frame;

        hintLabel.setVerticalAlignment(SwingConstants.TOP);
        hintLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        hintLabel.setIcon(IconImages.MISSING_ICON);
        hintLabel.setVerticalTextPosition(SwingConstants.TOP);
        // hint label replaces tooltips.
        ToolTipManager.sharedInstance().setEnabled(false);

        getContentPane().setLayout(new MigLayout("flowy, insets 0"));
        getContentPane().add(getTabbedSettingsPane(), "w 100%, h 100%");
        getContentPane().add(hintLabel, "w 100%, h 66!, gapx 10 10");
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
        tabbedPane.addTab("Gameplay", getDuelSettingsPanel());
        tabbedPane.addTab("Advanced", getAdvancedTabPanel());
        return tabbedPane;
    }

    private JPanel getDuelSettingsPanel() {

        final JPanel mainPanel=new JPanel();
        mainPanel.setLayout(null);

        int Y=10;
        final int X3=25;
        final int H3=20;
        final int W3=350;

        gameLogCheckBox = new JCheckBox("Show game log messages.", config.isLogMessagesVisible());
        gameLogCheckBox.setToolTipText("Clear this option if you would prefer the game log messages to be hidden by default. You can still toggle visibility during a game by clicking on the log titlebar.");
        gameLogCheckBox.setBounds(X3,Y,W3,H3);
        gameLogCheckBox.setFocusable(false);
        gameLogCheckBox.addMouseListener(this);
        mainPanel.add(gameLogCheckBox);

        Y += 30;
        soundCheckBox = new JCheckBox("Enable sound effects",config.isSound());
        soundCheckBox.setBounds(X3,Y,W3,H3);
        soundCheckBox.setFocusable(false);
        soundCheckBox.addMouseListener(this);
        mainPanel.add(soundCheckBox);

        Y += 30;
        touchscreenCheckBox = new JCheckBox("Double-click to cast or activate ability (for touchscreen)",config.isTouchscreen());
        touchscreenCheckBox.setBounds(X3,Y,W3,H3);
        touchscreenCheckBox.setFocusable(false);
        touchscreenCheckBox.addMouseListener(this);
        mainPanel.add(touchscreenCheckBox);

        Y += 30;
        skipSingleCheckBox = new JCheckBox("Automatically pass priority", config.getSkipSingle());
        skipSingleCheckBox.setToolTipText("When the only option is to pass don't prompt player, just pass immediately.");
        skipSingleCheckBox.setBounds(X3,Y,W3,H3);
        skipSingleCheckBox.setFocusable(false);
        skipSingleCheckBox.addMouseListener(this);
        mainPanel.add(skipSingleCheckBox);

        Y += 30;
        alwaysPassCheckBox = new JCheckBox("Always pass during draw and begin of combat step",
                config.getAlwaysPass());
        alwaysPassCheckBox.setBounds(X3,Y,W3,H3);
        alwaysPassCheckBox.setFocusable(false);
        alwaysPassCheckBox.addMouseListener(this);
        mainPanel.add(alwaysPassCheckBox);

        Y += 30;
        smartTargetCheckBox=new JCheckBox("Allow only sensible choices", config.getSmartTarget());
        smartTargetCheckBox.setToolTipText("Prevents you from choosing your own permanents for negative effects (eg. \"Destroy target creature\") or alternatively choosing an opponent's permanent for positive effects (eg. Giant Growth).");
        smartTargetCheckBox.setBounds(X3,Y,W3,H3);
        smartTargetCheckBox.setFocusable(false);
        smartTargetCheckBox.addMouseListener(this);
        mainPanel.add(smartTargetCheckBox);

        Y += 30;
        mouseWheelPopupCheckBox = new JCheckBox("Popup card image using mouse wheel (instead of delay)",
                config.isMouseWheelPopup());
        mouseWheelPopupCheckBox.setBounds(X3,Y,W3,H3);
        mouseWheelPopupCheckBox.setFocusable(false);
        mouseWheelPopupCheckBox.addMouseListener(this);
        mainPanel.add(mouseWheelPopupCheckBox);

        Y += 30;
        popupDelaySlider=new SliderPanel("Popup",
                IconImages.DELAY,
                0,
                500,
                50,
                config.getPopupDelay());
        popupDelaySlider.setBounds(50,Y,280,50);
        mainPanel.add(popupDelaySlider);

        Y += 40;
        messageDelaySlider = new SliderPanel("Message",
                IconImages.DELAY,
                0,
                3000,
                500,
                config.getMessageDelay());
        messageDelaySlider.setBounds(50,Y,280,50);
        mainPanel.add(messageDelaySlider);

        return mainPanel;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final Object source=event.getSource();
        if (source==okButton) {
            if (validateSettings()) {
                final GeneralConfig config=GeneralConfig.getInstance();
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
                config.setCustomBackground(isCustomBackground);
                config.setShowMissingCardData(missingCardDataCheckbox.isSelected());
                config.setCardImagesPath(imagesFolderChooser.getPath());
                config.save();
                ThemeFactory.getInstance().setCurrentTheme(config.getTheme());
                frame.repaint();
                dispose();
            }
        } else if (source==cancelButton) {
            dispose();
        }
    }

    private boolean validateSettings() {
        if (imagesFolderChooser.isValidDirectory()) {
            return true;
        } else {
            JOptionPane.showMessageDialog(this, "The path for the images directory is invalid!");
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

    private JPanel getGeneralTabPanel() {
        final JPanel panel = new JPanel(new MigLayout("flowy, gapy 10"));
        panel.add(getStyleSettingsPanel(), "w 100%");
        panel.add(getCardExplorerEditorSettingsPanel(), "w 100%");
        panel.add(getMiscSettingsPanel(), "w 100%");
        return panel;
    }

    private JPanel getStyleSettingsPanel() {

        // Theme setting
        final JLabel themeLabel=new JLabel("Theme");
        themeComboBox=new JComboBox<String>(ThemeFactory.getInstance().getThemeNames());
        themeComboBox.setToolTipText("Additional themes can be downloaded from the Magarena forum. Alternatively, to set the background image you can simply drag & drop an image file onto the Magarena window.");
        themeComboBox.addMouseListener(this);
        themeComboBox.setFocusable(false);
        themeComboBox.setSelectedItem(config.getTheme());
        themeComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent arg0) {
                isCustomBackground = false;
            }
        });

        // Card highlight setting.
        final JLabel highlightLabel = new JLabel("Highlight");
        final String[] Highlightchoices = { "none", "overlay", "border", "theme" };
        highlightComboBox = new JComboBox<String>(Highlightchoices);
        highlightComboBox.setFocusable(false);
        highlightComboBox.setSelectedItem(config.getHighlight());

        isCustomBackground = config.isCustomBackground();

        // Layout UI components.
        final JPanel panel = new JPanel(new MigLayout("flowx, wrap 2, insets 0"));
        panel.setBorder(BorderFactory.createTitledBorder("Look & Feel"));
        panel.add(themeLabel, "alignx right");
        panel.add(themeComboBox);
        panel.add(highlightLabel, "alignx right");
        panel.add(highlightComboBox);

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
      panel.setBorder(BorderFactory.createTitledBorder("Card Explorer / Deck Editor"));
      panel.add(previewCardOnSelectCheckBox);
      panel.add(missingCardDataCheckbox);

      return panel;

  }

    private JPanel getMiscSettingsPanel() {

        confirmExitCheckBox = new JCheckBox("Show confirmation dialog on exit", config.isConfirmExit());
        confirmExitCheckBox.setFocusable(false);
        confirmExitCheckBox.addMouseListener(this);

        highQualityCheckBox = new JCheckBox("Show card images in original size", config.isHighQuality());
        highQualityCheckBox.setFocusable(false);
        highQualityCheckBox.addMouseListener(this);

        mulliganScreenCheckbox = new JCheckBox("Use Mulligan screen", config.showMulliganScreen());
        mulliganScreenCheckbox.setFocusable(false);
        mulliganScreenCheckbox.addMouseListener(this);

        final JPanel panel = new JPanel(new MigLayout("flowy, insets 0"));
        panel.setBorder(BorderFactory.createTitledBorder("Misc Settings"));
        panel.add(confirmExitCheckBox);
        panel.add(highQualityCheckBox);
        panel.add(mulliganScreenCheckbox);

        return panel;

    }

    private JPanel getAdvancedTabPanel() {
        final JPanel panel = new JPanel(new MigLayout("flowy, gapy 10"));
        panel.add(getDirectorySettingsPanel(), "w 100%");
        return panel;
    }

    private JPanel getDirectorySettingsPanel() {

        imagesFolderChooser = new DirectoryChooser(config.getCardImagesPath());
        imagesFolderChooser.setToolTipText("Location of the \"cards\" and \"tokens\" directories which contain downloaded card and token images respectively. Right click to open in file explorer.");
        imagesFolderChooser.addMouseListener(this);

        final JPanel panel = new JPanel(new MigLayout("flowy"));
        panel.add(getCaptionLabel("Card Images Directory"));
        panel.add(imagesFolderChooser, "w 100%");

        return panel;

    }

    private JLabel getCaptionLabel(final String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FontsAndBorders.FONT1);
        return lbl;
    }

}
