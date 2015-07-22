package magic.ui.player;

import magic.model.player.AiProfile;
import magic.ui.widget.FontsAndBorders;
import magic.ui.utility.MagicStyle;
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
import magic.ui.UiString;

@SuppressWarnings("serial")
public class AiPlayerJList
    extends JList<AiProfile> {

    // translatable string
    private static final String _S1 = "AI: %s";
    private static final String _S2 = "Level: %d / 8";
    private static final String _S3 = "Extra Life: %d";
    private static final String _S4 = "Last played";

    public AiPlayerJList() {
        setOpaque(false);
        setCellRenderer(new AiPlayerListRenderer());
    }

    private class AiPlayerListRenderer extends JLabel implements ListCellRenderer<AiProfile> {

        private Color foreColor;
        private AiProfile profile;

        public AiPlayerListRenderer() {
            setOpaque(false);
        }

        @Override
        public Component getListCellRendererComponent(
            JList<? extends AiProfile> list,
            AiProfile profile,
            int index,
            boolean isSelected,
            boolean cellHasFocus
        ) {

            this.profile = profile;
            foreColor = isSelected ? MagicStyle.getRolloverColor() : Color.WHITE;

            final MigLayout migLayout = new MigLayout("insets 0, gap 0");
            final JPanel panel = new JPanel(migLayout);
            migLayout.setColumnConstraints("[66][]8[center][align right]4");

            panel.setPreferredSize(new Dimension(getWidth(), 70));
            panel.setOpaque(false);
            panel.setBorder(isSelected ? BorderFactory.createLineBorder(MagicStyle.getRolloverColor(), 1) : null);

            panel.add(getAvatarPortrait(), "h 70!");
            panel.add(getNamePanel(), "w 150!");
            panel.add(getDefaultDuelSettingsPanel(), "w 150");
            panel.add(new PlayerMiniStatsPanel(profile.getStats(), foreColor), "pushx");

            panel.setToolTipText(profile.getAiType().toString());

            return panel;
        }

        private JPanel getDefaultDuelSettingsPanel() {
            final JPanel panel = new JPanel(new MigLayout("insets 0, flowy"));
            panel.setOpaque(false);
            panel.add(getLabel(UiString.get(_S1, profile.getAiType().name())), "w 100%");
            panel.add(getLabel(UiString.get(_S2, profile.getAiLevel())), "w 100%");
            panel.add(getLabel(UiString.get(_S3, profile.getExtraLife())), "w 100%");
            return panel;
        }

        private JLabel getLabel(final String caption) {
            final JLabel lbl = new JLabel(caption);
            lbl.setForeground(foreColor);
            lbl.setFont(FontsAndBorders.FONT0);
            return lbl;
        }

        private JLabel getAvatarPortrait() {
            return new JLabel(IconImages.getPlayerAvatar(profile).getIcon(2));
        }

        private JPanel getNamePanel() {
            final JPanel panel = new JPanel(new MigLayout("insets 0, gap 0, flowy"));
            panel.setOpaque(false);
            panel.setForeground(foreColor);
            panel.add(getPlayerNameLabel(), "gapbottom 2");
            panel.add(getTimestampLabel());
            return panel;
        }

        private JLabel getTimestampLabel() {
            final JLabel lbl = new JLabel(String.format("<html>%s:<br>%s</html>", UiString.get(_S4), profile.getStats().getLastPlayed()));
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
