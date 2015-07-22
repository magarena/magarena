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
import magic.ui.StringContext;
import magic.ui.UiString;

@SuppressWarnings("serial")
public class AiPlayerJList
    extends JList<AiProfile> {

    // translatable strings
    @StringContext(eg="this is the AI level.")
    private static final String _S2 = "Level";
    private static final String _S3 = "Extra Life";

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
            migLayout.setColumnConstraints("4[66][][align right]4");

            panel.setPreferredSize(new Dimension(getWidth(), 70));
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
            panel.add(getPlayerSettingsLabel());
            return panel;
        }

        private JLabel getPlayerSettingsLabel() {
            final JLabel lbl = new JLabel(
                    String.format("<html>%s<br>%s: %d, %s: %d</html>",
                            profile.getAiType(),
                            UiString.get(_S2), profile.getAiLevel(),
                            UiString.get(_S3), profile.getExtraLife())
            );
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
