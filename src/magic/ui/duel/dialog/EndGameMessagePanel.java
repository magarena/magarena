package magic.ui.duel.dialog;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.ui.screen.duel.game.SwingGameController;
import magic.ui.MagicImages;
import magic.translate.UiString;
import magic.ui.theme.Theme;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class EndGameMessagePanel extends TexturedPanel {

    // translatable strings
    private static final String _S1 = "%s is the winner!";

    private final MigLayout miglayout = new MigLayout("flowy, center, center");

    public EndGameMessagePanel(final SwingGameController controller) {
        setPreferredSize(new Dimension(450, 350));
        setLayout(miglayout);
        //
        setOpaque(true);
        setBorder(BorderFactory.createMatteBorder(8, 8, 8, 8, MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND)));
        //
        final MagicGame game = controller.getGame();
        final MagicPlayer humanPlayer = game.getPlayer(0);
        final MagicPlayer aiPlayer = game.getPlayer(1);
        final MagicPlayer losingPlayer = game.getLosingPlayer();
        final MagicPlayer winningPlayer = losingPlayer == humanPlayer ? aiPlayer : humanPlayer;
        //
        final JLabel iconLabel = new JLabel(MagicImages.getIconSize3(winningPlayer.getPlayerDefinition()));
        final Icon winningAvatar = iconLabel.getIcon();
        iconLabel.setPreferredSize(new Dimension(winningAvatar.getIconWidth(), winningAvatar.getIconHeight()));
        add(iconLabel, "alignx center");
        //
        final JLabel winnerLabel = new JLabel(UiString.get(_S1, winningPlayer.getName()));
        winnerLabel.setFont(FontsAndBorders.FONT3);
        winnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        winnerLabel.setForeground(MagicStyle.getTheme().getColor(Theme.COLOR_TEXT_FOREGROUND));
        add(winnerLabel, "w 100%");
    }

}
