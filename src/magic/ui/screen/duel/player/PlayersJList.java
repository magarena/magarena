package magic.ui.screen.duel.player;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import magic.model.player.PlayerProfile;
import magic.ui.FontsAndBorders;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
abstract class PlayersJList extends JList<PlayerProfile> {

    protected abstract String getPlayerSettingsLabelText(PlayerProfile aProfile);

    PlayersJList() {
        setOpaque(false);
        setCellRenderer(new PlayersJListCellRenderer());
    }

    private class PlayersJListCellRenderer extends JLabel implements ListCellRenderer<PlayerProfile> {

        private Color foreColor;
        private PlayerProfile profile;

        public PlayersJListCellRenderer() {
            setOpaque(false);
        }

        @Override
        public Component getListCellRendererComponent(
            JList<? extends PlayerProfile> list,
            PlayerProfile profile,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

            this.profile = profile;
            foreColor = isSelected ? MagicStyle.getRolloverColor() : Color.WHITE;

            final MigLayout migLayout = new MigLayout("insets 0, gap 0");
            final JPanel panel = new JPanel(migLayout);
            migLayout.setColumnConstraints("4[66][][align right]4");

            panel.setPreferredSize(new Dimension(getWidth(), 70));
            panel.setOpaque(false);
            panel.setBorder(isSelected ? BorderFactory.createLineBorder(MagicStyle.getRolloverColor(), 1) : null);

            panel.add(getAvatarPortrait(), "h 70!");
            panel.add(getNamePanel(), "w 200!");
            panel.add(new PlayerMiniStatsPanel(profile.getStats(), foreColor), "pushx");

            return panel;
        }

        private JLabel getAvatarPortrait() {
            return new JLabel(profile.getAvatar().getIcon(2));
        }

        private JPanel getNamePanel() {
            final JPanel panel = new JPanel(new MigLayout("insets 0, gap 0, flowy"));
            panel.setOpaque(false);
            panel.setForeground(foreColor);
            panel.add(getPlayerNameLabel());
            panel.add(getPlayerSettingsLabel());
            return panel;
        }

        private JLabel getPlayerSettingsLabel() {
            final JLabel lbl = new JLabel(getPlayerSettingsLabelText(profile));
            lbl.setForeground(foreColor);
            lbl.setFont(FontsAndBorders.FONT0);
            return lbl;
        }

        private JLabel getPlayerNameLabel() {
            final JLabel lbl = new JLabel(profile.getPlayerName());
            lbl.setFont(FontsAndBorders.FONT2);
            lbl.setForeground(foreColor);
            lbl.setVerticalAlignment(SwingConstants.TOP);
            return lbl;
        }

    }

}
