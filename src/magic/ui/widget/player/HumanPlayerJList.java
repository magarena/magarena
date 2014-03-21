package magic.ui.widget.player;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import magic.model.player.HumanPlayer;
import magic.ui.widget.FontsAndBorders;
import magic.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class HumanPlayerJList
    extends JList<HumanPlayer> {

    public HumanPlayerJList() {
        setOpaque(false);
        setCellRenderer(new HumanPlayerListRenderer());
    }

    private class HumanPlayerListRenderer extends JPanel implements ListCellRenderer<HumanPlayer> {

        private Color foreColor;
        private HumanPlayer profile;

        public HumanPlayerListRenderer() {
            setOpaque(false);
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends HumanPlayer> list,
                HumanPlayer profile,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

            this.profile = profile;
            foreColor = isSelected ? MagicStyle.HIGHLIGHT_COLOR: Color.WHITE;

            final JPanel panel = new JPanel(new MigLayout("insets 0 0 0 6, gap 0"));
            panel.setPreferredSize(new Dimension(0, 70));
            panel.setOpaque(false);
            panel.setForeground(foreColor);
            panel.setBorder(isSelected ? BorderFactory.createLineBorder(MagicStyle.HIGHLIGHT_COLOR, 1) : null);

            panel.add(getAvatarPortrait(), "w 70!, h 70!");
            panel.add(getNamePanel(), "w 100%");
            panel.add(getMiniStatsPanel());

            return panel;
        }

        private JLabel getAvatarPortrait() {
            return new JLabel(profile.getAvatar().getIcon(2));
        }

        private JPanel getMiniStatsPanel() {
            final JPanel panel = new JPanel(new MigLayout("insets 0, gap 0, wrap 4", "[][30!]"));
            panel.setOpaque(false);
            panel.setForeground(foreColor);
            panel.add(new JLabel());
            panel.add(getStatsLabel("P", foreColor), "w 100%");
            panel.add(getStatsLabel("W", foreColor), "w 100%");
            panel.add(getStatsLabel("L", foreColor), "w 100%");
            panel.add(getStatsLabel("Duels", foreColor), "w 60!");
            panel.add(getStatsLabel(Integer.toString(profile.getStats().duelsPlayed), foreColor), "w 100%");
            panel.add(getStatsLabel(Integer.toString(profile.getStats().duelsWon), foreColor), "w 100%");
            panel.add(getStatsLabel(Integer.toString(profile.getStats().duelsPlayed - profile.getStats().duelsWon), foreColor), "w 100%");
            panel.add(getStatsLabel("Games", foreColor), "w 60!");
            panel.add(getStatsLabel(Integer.toString(profile.getStats().gamesPlayed), foreColor), "w 100%");
            panel.add(getStatsLabel(Integer.toString(profile.getStats().gamesWon), foreColor), "w 100%");
            panel.add(getStatsLabel(Integer.toString(profile.getStats().gamesPlayed - profile.getStats().gamesWon), foreColor), "w 100%");
            return panel;
        }

        private JPanel getNamePanel() {
            final JPanel panel = new JPanel(new MigLayout("insets 0, gap 0, flowy"));
            panel.setOpaque(false);
            panel.setForeground(foreColor);
            panel.add(getPlayerNameLabel(), "w 100%, gapbottom 4");
            panel.add(getTimestampLabel());
            return panel;
        }

        private JLabel getTimestampLabel() {
            final JLabel lbl = new JLabel("Last played: " + profile.getStats().getLastPlayed());
            lbl.setForeground(foreColor);
            return lbl;
        }

        private JLabel getPlayerNameLabel() {
            final JLabel lbl = new JLabel(profile.getPlayerName());
            lbl.setFont(FontsAndBorders.FONT2);
            lbl.setForeground(foreColor);
            lbl.setVerticalAlignment(SwingConstants.TOP);
            return lbl;
        }

        private JLabel getStatsLabel(final String text, final Color foreColor) {
            final JLabel lbl = new JLabel(text);
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            lbl.setBorder(BorderFactory.createDashedBorder(null));
            lbl.setFont(FontsAndBorders.FONT0);
            lbl.setForeground(foreColor);
            return lbl;
        }

    }

}