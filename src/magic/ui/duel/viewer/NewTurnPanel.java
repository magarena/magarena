package magic.ui.duel.viewer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import magic.model.MagicGame;
import magic.ui.IconImages;
import magic.ui.MagicStyle;
import magic.ui.theme.Theme;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class NewTurnPanel extends TexturedPanel {

    private final MigLayout migLayout = new MigLayout("flowy, gapy 0");
    private final JLabel iconLabel = new JLabel();
    private final JLabel turnLabel = new JLabel();
    private final JLabel playerLabel = new JLabel();

    public NewTurnPanel() {
        setLookAndFeel();
        refreshLayout();
    }

    private void setLookAndFeel() {
        setOpaque(true);
        setBorder(BorderFactory.createLineBorder(MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND), 1));
        //
        turnLabel.setFont(turnLabel.getFont().deriveFont(16f));
        turnLabel.setForeground(MagicStyle.getTheme().getColor(Theme.COLOR_TEXT_FOREGROUND));
        turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
        //
        playerLabel.setFont(turnLabel.getFont().deriveFont(20f));
        playerLabel.setForeground(MagicStyle.getTheme().getColor(Theme.COLOR_TEXT_FOREGROUND));
        playerLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void refreshLayout() {
        setLayout(migLayout);
        add(iconLabel, "alignx center");
        add(playerLabel, "w 100%, h 100%");
        add(turnLabel, "w 100%, h 100%");
    }

    public void refreshData(final MagicGame game) {
        iconLabel.setIcon(IconImages.getIconSize3(game.getTurnPlayer().getPlayerDefinition()));
        turnLabel.setText("Turn " + game.getTurn());
        playerLabel.setText(game.getTurnPlayer().getName());
    }

}
