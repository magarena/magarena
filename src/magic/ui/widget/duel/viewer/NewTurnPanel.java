package magic.ui.widget.duel.viewer;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.translate.UiString;
import magic.ui.duel.viewerinfo.GameViewerInfo;
import magic.ui.utility.MagicStyle;
import magic.ui.theme.Theme;
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
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        turnLabel.setFont(turnLabel.getFont().deriveFont(26f));
        turnLabel.setForeground(MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_FOREGROUND));
    }

    private void refreshLayout() {
        setLayout(migLayout);
        add(iconLabel);
        add(turnLabel, "w 100%, h 100%");
    }

    public void refreshData(final GameViewerInfo gameInfo) {
        iconLabel.setIcon(gameInfo.getTurnPlayer().getAvatar());
        turnLabel.setText(UiString.get(_S1, gameInfo.getTurn()));
    }

}
