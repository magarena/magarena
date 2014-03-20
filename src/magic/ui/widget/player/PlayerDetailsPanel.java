package magic.ui.widget.player;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import magic.model.player.AiPlayer;
import magic.model.player.PlayerProfile;
import magic.ui.widget.FontsAndBorders;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class PlayerDetailsPanel extends JPanel {

    private final Color foreColor;

    private final PlayerProfile profile;

    // CTR
    public PlayerDetailsPanel(final PlayerProfile profile, final Color foreColor) {
        this.profile = profile;
        this.foreColor = foreColor;
        setOpaque(false);
        setForeground(foreColor);
        setLayout(new MigLayout("insets 0, gap 0, flowy"));
        layoutComponents();
    }
    public PlayerDetailsPanel(final PlayerProfile profile) {
        this(profile, Color.WHITE);
    }

    private void layoutComponents() {
        add(getPlayerTypeLabel(), "w 100%");
        add(getPlayerNameLabel(), "w 100%");
        add(getPlayerPropertiesLabel(), "w 100%");
    }

    private JLabel getPlayerTypeLabel() {
        final JLabel lbl = new JLabel();
        lbl.setFont(FontsAndBorders.FONT0);
        lbl.setForeground(foreColor);
        if (profile instanceof AiPlayer) {
            final AiPlayer player = (AiPlayer)profile;
            lbl.setText("AI : " + player.getAiType());
        }
        return lbl;
    }

    private JLabel getPlayerNameLabel() {
        final JLabel lbl = new JLabel(profile.getPlayerName());
        lbl.setFont(FontsAndBorders.FONT3);
        lbl.setForeground(foreColor);
        lbl.setVerticalAlignment(SwingConstants.TOP);
        return lbl;
    }

    private JLabel getPlayerPropertiesLabel() {
        JLabel lbl = new JLabel();
        if (profile instanceof AiPlayer) {
            lbl = getAiPlayerProperties();
        }
        return lbl;
    }

    private JLabel getAiPlayerProperties() {
        final AiPlayer player = (AiPlayer)profile;
        final JLabel lbl = new JLabel("Level: " + player.getAiLevel() + "  Extra Life: " + player.getExtraLife());
        lbl.setFont(FontsAndBorders.FONT0);
        lbl.setForeground(foreColor);
        return lbl;
    }

}
