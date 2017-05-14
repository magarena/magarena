package magic.ui.widget.duel.viewer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.translate.MText;
import magic.ui.duel.viewerinfo.GameViewerInfo;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class NewTurnPanel extends JPanel {

    // translatable strings
    private static final String _S1 = "Turn %d";

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

    public void refreshData(final GameViewerInfo gameInfo) {
        iconLabel.setIcon(gameInfo.getTurnPlayer().getNewTurnAvatar());
        turnLabel.setText(MText.get(_S1, gameInfo.getTurn()));
    }

}
