package magic.ui.duel.viewer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.model.MagicGame;
import magic.ui.IconImages;
import magic.ui.MagicStyle;
import magic.ui.theme.Theme;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class NewTurnPanel extends JPanel {

    private final MigLayout migLayout = new MigLayout("insets 0 2 0 0, gapx 10");
    private final JLabel iconLabel = new JLabel();
    private final JLabel turnLabel = new JLabel();

    public NewTurnPanel() {
        setLookAndFeel();
        refreshLayout();
    }

    private void setLookAndFeel() {
        setOpaque(true);
        setBackground(MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND));
        turnLabel.setFont(turnLabel.getFont().deriveFont(26f));
        turnLabel.setForeground(MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_FOREGROUND));
    }

    private void refreshLayout() {
        setLayout(migLayout);
        add(iconLabel);
        add(turnLabel, "w 100%, h 100%");
    }

    public void refreshData(final MagicGame game) {
        iconLabel.setIcon(IconImages.getIconSize4(game.getTurnPlayer().getPlayerDefinition()));
        turnLabel.setText("Turn " + game.getTurn());
    }

}
