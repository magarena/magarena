package magic.ui.player;

import magic.model.MagicPlayerDefinition;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.TitleBar;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

import java.awt.BorderLayout;
import java.awt.Dimension;

public class PlayerAvatarPanel extends TexturedPanel {

    private static final long serialVersionUID = 1L;

    private static final Border NORMAL_BORDER=BorderFactory.createEmptyBorder(6,6,6,6);

    private final int index;
    private final JLabel faceLabel;
    private final TitleBar titleBar;
    private MagicPlayerDefinition playerDefinition;
    private boolean small;
    private final Border selectedBorder;

    public PlayerAvatarPanel(final int index) {

        this.index=index;
        this.setLayout(new BorderLayout());
        faceLabel=new JLabel();
        add(faceLabel,BorderLayout.CENTER);
        titleBar=new TitleBar("");
        titleBar.setHorizontalAlignment(JLabel.CENTER);
        add(titleBar,BorderLayout.SOUTH);
        setSelected(false);
        small=false;

        final Theme theme=ThemeFactory.getInstance().getCurrentTheme();
        selectedBorder=BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2,2,2,2),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(theme.getColor(Theme.COLOR_SELECTED_PLAYER),2),
                        BorderFactory.createEmptyBorder(2,2,2,2)
                    )
                );
    }

    public void setPlayerDefinition(final MagicPlayerDefinition playerDefinition) {

        this.playerDefinition=playerDefinition;
        update();
    }

    public MagicPlayerDefinition getPlayerDefinition() {

        return playerDefinition;
    }

    public int getIndex() {

        return index;
    }

    public void setSmall(final boolean small) {

        if (this.small!=small) {
            this.small=small;
            update();
        }
    }

    public void setSelected(final boolean selected) {

        this.setBorder(selected?selectedBorder:NORMAL_BORDER);
    }

    private void update() {
        if (playerDefinition != null) {

            faceLabel.setIcon(playerDefinition.getAvatar().getIcon(small?2:3));

            titleBar.setText(playerDefinition.getName());
            if (small) {
                titleBar.setVisible(false);
                setPreferredSize(new Dimension(72,80));
            } else {
                titleBar.setVisible(true);
                setPreferredSize(new Dimension(132,150));
            }
            revalidate();
        }
    }
}
