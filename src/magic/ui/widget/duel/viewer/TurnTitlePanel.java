package magic.ui.widget.duel.viewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import magic.data.MagicIcon;
import magic.translate.MText;
import magic.translate.StringContext;
import magic.ui.duel.viewerinfo.GameViewerInfo;
import magic.ui.helpers.ImageHelper;
import magic.ui.screen.duel.game.SwingGameController;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import magic.utility.MagicSystem;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class TurnTitlePanel extends JPanel {

    // translatable strings
    private static final String _S1 = "Options Menu [ESC]";
    private static final String _S2 = "Displays menu of common and screen specific options.";
    private static final String _S3 = "First player to %d wins the duel.";
    @StringContext(eg = "as in 'Game 2 of 3'")
    private static final String _S4 = "Game %d / %d";
    private static final String _S5 = "Turn %d";

    private static final ImageIcon MENU_ICON = ImageHelper.getRecoloredIcon(
        MagicIcon.OPTION_MENU_TINY, Color.BLACK, Color.WHITE
    );

    private final MigLayout miglayout = new MigLayout();
    private final JLabel scoreLabel = new JLabel();
    private final JLabel gameLabel = new JLabel();
    private final SwingGameController controller;

    public TurnTitlePanel(final SwingGameController controller) {
        this.controller = controller;
        setLookAndFeel();
        setLayout(miglayout);
        refreshLayout();
    }

    private void setLookAndFeel() {
        setOpaque(false);
        //
        final Color textColor = MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_FOREGROUND);
        scoreLabel.setForeground(textColor);
        gameLabel.setForeground(textColor);
    }

    private void refreshLayout() {
        miglayout.setLayoutConstraints("insets 0 3 0 2, gap 0, flowy, wrap 2");
        miglayout.setColumnConstraints("[fill]push[]");
        removeAll();
        add(scoreLabel);
        add(gameLabel);
        add(getOptionsIconButton(), "spany 2, aligny top");
    }

    private JButton getOptionsIconButton() {

        final JButton btn = new ActionBarButton(MENU_ICON,
                MText.get(_S1), MText.get(_S2),
                new AbstractAction() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        controller.showGameOptionsOverlay();
                    }
                }
        );
        btn.setMaximumSize(new Dimension(18, 18));

        if (MagicSystem.isDevMode()) {
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.getClickCount() == 1 && SwingUtilities.isRightMouseButton(e)) {
                        showDevPopupMenu(e);
                    } else {
                        super.mouseReleased(e);
                    }
                }
            });
        }
        return btn;
    }

    public void refresh(final GameViewerInfo gameInfo) {
        scoreLabel.setText(getScoreString(gameInfo));
        scoreLabel.setToolTipText(MText.get(_S3, gameInfo.getGamesRequiredToWinDuel()));
        gameLabel.setText(String.format("%s  •  %s  •  %s",
                MText.get(_S4, gameInfo.getGameNumber(), gameInfo.getMaxGames()),
                MText.get(_S5, gameInfo.getTurn()),
                gameInfo.getTurnPlayer().getName())
        );
    }

    private String getScoreString(final GameViewerInfo gameInfo) {
        return String.format("%s %d - %d %s",
                gameInfo.getMainPlayer().getName(),
                gameInfo.getMainPlayer().getGamesWon(),
                gameInfo.getOpponent().getGamesWon(),
                gameInfo.getOpponent().getName()
        );
    }

    private void showDevPopupMenu(final MouseEvent e) {
        final JPopupMenu menu = new JPopupMenu();
        final JMenuItem item1 = new JMenuItem(new AbstractAction("Save Game") {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.doSaveGame();
            }
        });
        menu.add(item1);
        menu.show(e.getComponent(), e.getX(), e.getY());
    }

}
