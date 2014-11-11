package magic.ui.player;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import magic.model.MagicPlayerDefinition;
import magic.ui.widget.TexturedPanel;

@SuppressWarnings("serial")
public class PlayerAvatarPanel extends TexturedPanel {

    private final int index;
    private final JLabel faceLabel;
    private MagicPlayerDefinition playerDefinition;
    private boolean small = false;
    private boolean isSelected = false;

    public PlayerAvatarPanel(final int index) {
        this.index = index;
        this.setLayout(new BorderLayout());
        faceLabel = new JLabel();
        faceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(faceLabel, BorderLayout.CENTER);
    }

    public void setPlayerDefinition(final MagicPlayerDefinition playerDefinition) {
        this.playerDefinition = playerDefinition;
        update();
    }

    public MagicPlayerDefinition getPlayerDefinition() {
        return playerDefinition;
    }

    public int getIndex() {
        return index;
    }

    public void setSmall(final boolean small) {
        this.small=small;
        update();
    }

    public void setSelected(final boolean selected) {
        this.isSelected = selected;
        this.small = !selected;
        update();
    }

    private void update() {
        if (playerDefinition != null) {
            faceLabel.setIcon(playerDefinition.getAvatar().getIcon(small ? 2 : 3));
            if (small) {
                setPreferredSize(new Dimension(72,80));
            } else {
                setPreferredSize(new Dimension(120,150));
            }
            revalidate();
        }
    }

}
