package magic.ui.duel.dialog;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.ui.GameController;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class EndGameMessagePanel extends TexturedPanel {

    private final Theme THEME = ThemeFactory.getInstance().getCurrentTheme();
    private final MigLayout miglayout = new MigLayout("flowy, center, center");

    public EndGameMessagePanel(final GameController controller) {
        setPreferredSize(new Dimension(450, 350));
        setLayout(miglayout);
        //
        setOpaque(true);
        setBorder(BorderFactory.createMatteBorder(8, 8, 8, 8, THEME.getColor(Theme.COLOR_TITLE_BACKGROUND)));
        //
        final MagicGame game = controller.getGame();
        final MagicPlayer humanPlayer = game.getPlayer(0);
        final MagicPlayer aiPlayer = game.getPlayer(1);
        final MagicPlayer losingPlayer = game.getLosingPlayer();
        final MagicPlayer winningPlayer = losingPlayer == humanPlayer ? aiPlayer : humanPlayer;
        //
        final JLabel iconLabel = new JLabel(winningPlayer.getPlayerDefinition().getAvatar().getIcon(3));
        final Icon winningAvatar = iconLabel.getIcon();
        iconLabel.setPreferredSize(new Dimension(winningAvatar.getIconWidth(), winningAvatar.getIconHeight()));
        add(iconLabel, "alignx center");
        //
        final JLabel winnerLabel = new JLabel(winningPlayer.getName() + " is the winner!");
        winnerLabel.setFont(FontsAndBorders.FONT3);
        winnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        winnerLabel.setForeground(THEME.getColor(Theme.COLOR_TEXT_FOREGROUND));
        add(winnerLabel, "w 100%");
    }

}
