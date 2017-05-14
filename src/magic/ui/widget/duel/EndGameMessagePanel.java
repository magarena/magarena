package magic.ui.widget.duel;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import magic.translate.MText;
import magic.ui.FontsAndBorders;
import magic.ui.duel.viewerinfo.GameViewerInfo;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class EndGameMessagePanel extends TexturedPanel {

    // translatable strings
    private static final String _S1 = "%s is the winner!";

    private final MigLayout miglayout = new MigLayout("flowy, center, center");

    public EndGameMessagePanel(GameViewerInfo game) {
        setPreferredSize(new Dimension(450, 350));
        setLayout(miglayout);
        //
        setOpaque(true);
        setBorder(BorderFactory.createMatteBorder(8, 8, 8, 8, MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND)));
        //
        final JLabel iconLabel = new JLabel(game.getWinningPlayer().getPlayerPanelAvatar());
        final Icon winningAvatar = iconLabel.getIcon();
        iconLabel.setPreferredSize(new Dimension(winningAvatar.getIconWidth(), winningAvatar.getIconHeight()));
        add(iconLabel, "alignx center");
        //
        final JLabel winnerLabel = new JLabel(MText.get(_S1, game.getWinningPlayer().getName()));
        winnerLabel.setFont(FontsAndBorders.FONT3);
        winnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        winnerLabel.setForeground(MagicStyle.getTheme().getColor(Theme.COLOR_TEXT_FOREGROUND));
        add(winnerLabel, "w 100%");
    }

}
