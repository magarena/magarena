package magic.ui.dialog;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import magic.ai.MagicAIImpl;
import magic.model.player.AiProfile;
import magic.model.player.PlayerProfile;
import magic.translate.MText;
import magic.ui.dialog.button.CancelButton;
import magic.ui.dialog.button.SaveButton;
import magic.ui.widget.SliderPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class AiPropertiesDialog extends MagicDialog {

    // translatable strings
    private static final String _S1 = "AI Profile";
    private static final String _S2 = "Extra Life";
    private static final String _S3 = "AI Level";
    private static final String _S6 = "AI Type:";
    private static final String _S7 = "AI Name:";

    private AiProfile playerProfile;
    private final JTextField playerNameTextField;
    private final SliderPanel aiLevelSliderPanel;
    private final SliderPanel lifeSliderPanel;
    private final JComboBox<MagicAIImpl> aiComboBox;

    public AiPropertiesDialog(final AiProfile profile) {

        super(MText.get(_S1), new Dimension(400, 260));

        this.playerProfile = profile == null ? new AiProfile() : profile;
        playerNameTextField = new JTextField(playerProfile.getPlayerName());

        lifeSliderPanel = new SliderPanel(MText.get(_S2), 0, 10, 1, playerProfile.getExtraLife());
        aiLevelSliderPanel = new SliderPanel(MText.get(_S3), 1, 8, 1, playerProfile.getAiLevel());

        aiComboBox = new JComboBox<>();
        aiComboBox.setModel(new DefaultComboBoxModel<>(MagicAIImpl.SUPPORTED_AIS));
        aiComboBox.setLightWeightPopupEnabled(false);
        aiComboBox.setFocusable(false);
        aiComboBox.setSelectedItem(playerProfile.getAiType());

        refreshLayout();
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        setVisible(true);
    }

    public AiPropertiesDialog() {
        this(null);
    }

    private void refreshLayout() {
        final JPanel panel = getDialogContentPanel();
        panel.setLayout(new MigLayout("flowy, gap 0 10"));
        panel.add(getPlayerNamePanel(), "w 100%");
        panel.add(getAiTypePanel(), "w 100%");
        panel.add(lifeSliderPanel, "w 100%");
        panel.add(aiLevelSliderPanel, "w 100%");
        panel.add(getButtonPanel(), "w 100%, h 30!, pushy, aligny bottom");
    }

    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    private JPanel getButtonPanel() {
        final JPanel buttonPanel = new JPanel(new MigLayout("insets 0, alignx right"));
        buttonPanel.add(getCancelButton());
        buttonPanel.add(getSaveButton());
        return buttonPanel;
    }

    private JButton getCancelButton() {
        final JButton btn = new CancelButton();
        btn.addActionListener(getCancelAction());
        return btn;
    }

    private JButton getSaveButton() {
        final JButton btn = new SaveButton();
        btn.addActionListener(getSaveAction());
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

    private JPanel getAiTypePanel() {
        final JPanel panel = new JPanel(new MigLayout("insets 0"));
        panel.add(new JLabel(MText.get(_S6)));
        panel.add(aiComboBox, "w 100%, left");
        return panel;
    }

    private JPanel getPlayerNamePanel() {
        final JPanel panel = new JPanel(new MigLayout("insets 0"));
        panel.add(new JLabel(MText.get(_S7)));
        panel.add(playerNameTextField, "w 100%, left");
        return panel;
    }

    @Override
    protected AbstractAction getCancelAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerProfile = null;
                dispose();
            }
        };
    }

    private AbstractAction getSaveAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPlayerNameValid()) {
                    savePlayerProfile();
                    dispose();
                }
            }
        };
    }

}
