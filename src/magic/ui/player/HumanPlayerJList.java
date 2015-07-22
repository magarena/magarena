package magic.ui.player;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import magic.model.player.HumanProfile;
import magic.ui.IconImages;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.FontsAndBorders;
import net.miginfocom.swing.MigLayout;

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
            boolean cellHasFocus
        ) {

            this.profile = profile;
            foreColor = isSelected ? MagicStyle.getRolloverColor(): Color.WHITE;

            final MigLayout migLayout = new MigLayout("insets 0, gap 0");
            final JPanel panel = new JPanel(migLayout);
            migLayout.setColumnConstraints("4[66][][align right]4");

            panel.setPreferredSize(new Dimension(0, 70));
            panel.setOpaque(false);
            panel.setBorder(isSelected ? BorderFactory.createLineBorder(MagicStyle.getRolloverColor(), 1) : null);

            panel.add(getAvatarPortrait(), "h 70!");
            panel.add(getNamePanel(), "w 160!");
            panel.add(new PlayerMiniStatsPanel(profile.getStats(), foreColor), "pushx");

            return panel;
        }

        private JLabel getAvatarPortrait() {
            return new JLabel(IconImages.getPlayerAvatar(profile).getIcon(2));
        }

        private JPanel getNamePanel() {
            final JPanel panel = new JPanel(new MigLayout("insets 0, gap 0, flowy"));
            panel.setOpaque(false);
            panel.setForeground(foreColor);
            panel.add(getPlayerNameLabel());
            return panel;
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
