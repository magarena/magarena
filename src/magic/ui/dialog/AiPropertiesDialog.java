package magic.ui.dialog;

import magic.ai.MagicAIImpl;
import magic.model.player.AiProfile;
import magic.model.player.PlayerProfile;
import magic.ui.MagicFrame;
import magic.ui.widget.SliderPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class AiPropertiesDialog extends JDialog {

    private AiProfile playerProfile;
    private final JTextField playerNameTextField;
    private final SliderPanel aiLevelSliderPanel;
    private final SliderPanel lifeSliderPanel;
    private final JComboBox<MagicAIImpl> aiComboBox;

 // CTR : edit an existing profile.
    public AiPropertiesDialog(final MagicFrame frame, final AiProfile profile) {

        super(frame, true);
        this.setTitle("AI Profile");
        this.setSize(300, 260);
        this.setLocationRelativeTo(frame);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.playerProfile = profile == null ? new AiProfile() : profile;
        playerNameTextField = new JTextField(playerProfile.getPlayerName());
        lifeSliderPanel = new SliderPanel("Extra Life", null, 0, 10, 1, playerProfile.getExtraLife());
        aiLevelSliderPanel = new SliderPanel("AI Level", null, 1, 8, 1, playerProfile.getAiLevel());

        aiComboBox = new JComboBox<MagicAIImpl>();
        aiComboBox.setModel(new DefaultComboBoxModel<MagicAIImpl>(MagicAIImpl.SUPPORTED_AIS));
        aiComboBox.setLightWeightPopupEnabled(false);
        aiComboBox.setFocusable(false);
        aiComboBox.setSelectedItem(playerProfile.getAiType());

        getContentPane().setLayout(new MigLayout("insets 0, flowy", "", "[][45!]"));
        getContentPane().add(getDataPanel(), "w 100%, h 100%");
        getContentPane().add(getButtonPanel(), "w 100%, h 40!");

        setEscapeKeyAction();

        setVisible(true);
    }
    public AiPropertiesDialog(final MagicFrame frame) {
        this(frame, null);
    }

    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    private void setEscapeKeyAction() {
        JRootPane root = getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "closeDialog");
        root.getActionMap().put("closeDialog", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        });
    }

    private JPanel getButtonPanel() {
        final JPanel buttonPanel = new JPanel(new MigLayout("alignx right"));
        buttonPanel.add(getSaveButton(), "w 80!");
        buttonPanel.add(getCancelButton(), "w 80!");
        return buttonPanel;
    }

    private JButton getCancelButton() {
        final JButton btn = new JButton("Cancel");
        btn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerProfile = null;
                dispose();
            }
        });
        return btn;
    }

    private JButton getSaveButton() {
        final JButton btn = new JButton("Save");
        btn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPlayerNameValid()) {
                    savePlayerProfile();
                    dispose();
                }
            }
        });
        return btn;
    }

    private boolean isPlayerNameValid() {
        final String newName = playerNameTextField.getText().trim();
        return !newName.isEmpty();
    }

    private void savePlayerProfile() {
        playerProfile.setPlayerName(getVerifiedPlayerName(this.playerNameTextField.getText(), playerProfile.getPlayerName()));
        playerProfile.setExtraLife(lifeSliderPanel.getValue());
        playerProfile.setAiLevel(aiLevelSliderPanel.getValue());
        playerProfile.setAiType((MagicAIImpl)aiComboBox.getSelectedItem());
        playerProfile.save();
    }

    private String getVerifiedPlayerName(String newName, String oldName) {
        if (newName == null) {
            newName = oldName;
        } else {
            newName = newName.trim();
            if (newName.isEmpty() || newName.equalsIgnoreCase(oldName) ) {
                newName = oldName;
            }
        }
        return newName;
    }

    private JPanel getDataPanel() {
        final JPanel panel = new JPanel(new MigLayout("flowy"));
        panel.add(getPlayerNamePanel(), "w 100%");
        panel.add(getAiTypePanel(), "w 100%");
        panel.add(lifeSliderPanel, "w 100%");
        panel.add(aiLevelSliderPanel, "w 100%");
        return panel;
    }

    private JPanel getAiTypePanel() {
        final JPanel panel = new JPanel(new MigLayout());
        panel.add(new JLabel("AI Type:"));
        panel.add(aiComboBox, "w 100%, left");
        return panel;
    }

    private JPanel getPlayerNamePanel() {
        final JPanel panel = new JPanel(new MigLayout());
        panel.add(new JLabel("AI Name:"));
        panel.add(playerNameTextField, "w 100%, left");
        return panel;
    }

}
