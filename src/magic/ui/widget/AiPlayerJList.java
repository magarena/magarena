package magic.ui.widget;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import magic.model.player.AiPlayer;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class AiPlayerJList
    extends JList<AiPlayer> {

    public AiPlayerJList() {
        setOpaque(false);
        setCellRenderer(new AiPlayerListRenderer());
    }

    private class AiPlayerListRenderer extends JLabel implements ListCellRenderer<AiPlayer> {

        private Color foreColor;
        private AiPlayer profile;

        public AiPlayerListRenderer() {
            setOpaque(false);
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends AiPlayer> list,
                AiPlayer profile,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

            this.profile = profile;
            foreColor = isSelected ? Color.YELLOW : Color.WHITE;

            final JPanel panel = new JPanel(new MigLayout("insets 0 0 0 6, gap 0"));
            panel.setPreferredSize(new Dimension(0, 70));
            panel.setOpaque(false);
            panel.setForeground(foreColor);
            panel.setBorder(isSelected ? BorderFactory.createLineBorder(Color.YELLOW, 1) : null);

            panel.add(getAvatarPortrait(), "w 70!, h 70!");
            panel.add(getNamePanel(), "w 100%");
            panel.add(getDefaultDuelSettingsPanel(), "w 100%");
            panel.add(getMiniStatsPanel());

            panel.setToolTipText(profile.getAiType().toString());

            return panel;
        }

        private JPanel getDefaultDuelSettingsPanel() {
            final JPanel panel = new JPanel(new MigLayout("debug, insets 0 20 0 0, flowy"));
            panel.setOpaque(false);
            panel.add(getLabel("AI: " + profile.getAiType().name()), "w 100%");
            panel.add(getLabel("Level: " + profile.getAiLevel() + " / 8"), "w 100%");
            panel.add(getLabel("Extra Life: " + profile.getExtraLife()), "w 100%");
            return panel;
        }

        private JLabel getLabel(final String caption) {
            final JLabel lbl = new JLabel(caption);
            lbl.setForeground(foreColor);
            return lbl;
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
            panel.add(getPlayerNameLabel(), "gapbottom 4");
            //            panel.add(getAiTypeLabel());
            panel.add(getTimestampLabel());
            return panel;
        }

        private JLabel getTimestampLabel() {
            final JLabel lbl = new JLabel("Last played: Never");
            lbl.setForeground(foreColor);
            //            lbl.setBorder(BorderFactory.createDashedBorder(null));
            return lbl;
        }

        private JLabel getPlayerNameLabel() {
            final JLabel lbl = new JLabel(profile.getPlayerName());
            lbl.setFont(FontsAndBorders.FONT2);
            lbl.setForeground(foreColor);
            lbl.setVerticalAlignment(SwingConstants.TOP);
            //            lbl.setBorder(BorderFactory.createDashedBorder(null));
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