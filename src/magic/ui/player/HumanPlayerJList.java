package magic.ui.player;

import magic.model.player.HumanProfile;
import magic.ui.widget.FontsAndBorders;
import magic.ui.MagicStyle;
import net.miginfocom.swing.MigLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import magic.ui.IconImages;

@SuppressWarnings("serial")
public class HumanPlayerJList
    extends JList<HumanProfile> {

    public HumanPlayerJList() {
        setOpaque(false);
        setCellRenderer(new HumanPlayerListRenderer());
    }

    private class HumanPlayerListRenderer extends JPanel implements ListCellRenderer<HumanProfile> {

        private Color foreColor;
        private HumanProfile profile;

        public HumanPlayerListRenderer() {
            setOpaque(false);
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends HumanProfile> list,
                HumanProfile profile,
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
            panel.add(new PlayerMiniStatsPanel(profile.getStats(), foreColor));

            return panel;
        }

        private JLabel getAvatarPortrait() {
            return new JLabel(IconImages.getPlayerAvatar(profile).getIcon(2));
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

    }

}
