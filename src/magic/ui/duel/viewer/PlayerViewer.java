package magic.ui.duel.viewer;

import magic.model.MagicPlayer;
import magic.ui.GameController;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.PanelButton;
import magic.ui.widget.TexturedPanel;
import magic.ui.player.PlayerAvatarPanel;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Set;
import magic.ui.MagicStyle;

public class PlayerViewer extends JPanel implements ChoiceViewer {

    private static final long serialVersionUID = 1L;

    private static final String[] ICON_NAMES={
        Theme.ICON_LIFE,Theme.ICON_PREVENT,Theme.ICON_LAND,
        Theme.ICON_HAND,Theme.ICON_LIBRARY,Theme.ICON_GRAVEYARD
    };

    private final ViewerInfo viewerInfo;
    private final GameController controller;
    private final boolean opponent;
    private final PanelButton avatarButton;
    private final PlayerAvatarPanel avatarPanel;
    private final JLabel[] labels;
    private final JPanel labelsPanel;
    private final ImageIcon preventIcon;
    private final ImageIcon poisonIcon;

    public PlayerViewer(final GameController controller, final boolean opponent) {

        this.viewerInfo=controller.getViewerInfo();
        this.controller=controller;
        this.opponent=opponent;

        controller.registerChoiceViewer(this);

        setLayout(new BorderLayout());

        avatarButton=new PanelButton() {

            private static final long serialVersionUID = 1L;

            @Override
            public void mouseClicked() {

                final MagicPlayer player=viewerInfo.getPlayerInfo(opponent).player;
                PlayerViewer.this.controller.processClick(player);
            }

            @Override
            public Color getValidColor() {

                return ThemeFactory.getInstance().getCurrentTheme().getChoiceColor();
            }
        };

        avatarPanel=new PlayerAvatarPanel(0);
        avatarButton.setComponent(avatarPanel);
        add(avatarButton,BorderLayout.WEST);

        labelsPanel=new TexturedPanel();
        labelsPanel.setBorder(FontsAndBorders.BLACK_BORDER);
        add(labelsPanel,BorderLayout.CENTER);

        labels=new JLabel[6];
        preventIcon=MagicStyle.getTheme().getIcon(Theme.ICON_PREVENT);
        poisonIcon=MagicStyle.getTheme().getIcon(Theme.ICON_POISON);
        final Color foreground=MagicStyle.getTheme().getTextColor();
        for (int index=0;index<labels.length;index++) {

            labels[index]=new JLabel("0");
            labels[index].setFont(FontsAndBorders.FONT2);
            labels[index].setForeground(foreground);
            labels[index].setIconTextGap(4);
            labels[index].setHorizontalAlignment(JLabel.CENTER);
            labels[index].setIcon(MagicStyle.getTheme().getIcon(ICON_NAMES[index]));
        }

        setSmall(true);
        update();
    }

    public void setSmall(final boolean small) {

        labelsPanel.removeAll();
        avatarPanel.setSmall(small);

        if (small) {
            labelsPanel.setLayout(new GridLayout(2,3));
        } else {
            labelsPanel.setLayout(new GridLayout(3,2));
        }

        for (int index=0;index<labels.length;index++) {

            labelsPanel.add(labels[index]);
        }

        showValidChoices(controller.getValidChoices());
        revalidate();
    }

    public void update() {
        final PlayerViewerInfo playerInfo=viewerInfo.getPlayerInfo(opponent);
        avatarPanel.setPlayerDefinition(playerInfo.player.getPlayerDefinition());

        labels[0].setText(Integer.toString(playerInfo.life));
        labels[1].setIcon(playerInfo.poison>0?poisonIcon:preventIcon);
        labels[1].setText(playerInfo.poison>0?Integer.toString(playerInfo.poison):Integer.toString(playerInfo.preventDamage));
        labels[2].setText(Integer.toString(playerInfo.lands));
        labels[3].setText(Integer.toString(playerInfo.hand.size()));
        labels[4].setText(Integer.toString(playerInfo.library.size()));
        labels[5].setText(Integer.toString(playerInfo.graveyard.size()));

        avatarPanel.setSelected(playerInfo.turn);
    }

    @Override
    public void showValidChoices(final Set<?> validChoices) {
        if (!validChoices.isEmpty()) {
            final MagicPlayer player=viewerInfo.getPlayerInfo(opponent).player;
            avatarButton.setValid(validChoices.contains(player));
        } else {
            avatarButton.setValid(false);
        }
    }

    public PlayerAvatarPanel getAvatarPanel() {
        return avatarPanel;
    }
}
