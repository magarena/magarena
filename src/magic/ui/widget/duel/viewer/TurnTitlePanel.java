package magic.ui.widget.duel.viewer;

import magic.ui.duel.viewerinfo.GameViewerInfo;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import magic.data.MagicIcon;
import magic.ui.MagicImages;
import magic.translate.StringContext;
import magic.ui.utility.MagicStyle;
import magic.ui.screen.duel.game.SwingGameController;
import magic.translate.UiString;
import magic.ui.screen.interfaces.IOptionsMenu;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.theme.Theme;
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

        final JButton btn = new ActionBarButton(
                MagicImages.getIcon(MagicIcon.MENU),
                UiString.get(_S1),
                UiString.get(_S2),
                new AbstractAction() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        showOptionsMenu();
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

    private void showOptionsMenu() {
        Container parent = getParent();
        while (parent != null) {
            if (parent instanceof IOptionsMenu) {
                break;
            }
            parent = parent.getParent();
        }
        if (parent != null) {
            ((IOptionsMenu) parent).showOptionsMenuOverlay();
        }
    }

    public void refresh(final GameViewerInfo gameInfo) {
        scoreLabel.setText(getScoreString(gameInfo));
        scoreLabel.setToolTipText(UiString.get(_S3, gameInfo.getGamesRequiredToWinDuel()));
        gameLabel.setText(String.format("%s  •  %s  •  %s",
                UiString.get(_S4, gameInfo.getGameNumber(), gameInfo.getMaxGames()),
                UiString.get(_S5, gameInfo.getTurn()),
                gameInfo.getTurnPlayer().getName())
        );
    }

    private String getScoreString(final GameViewerInfo gameInfo) {
        return String.format("%s %d - %d %s",
                gameInfo.getPlayerInfo(false).getName(),
                gameInfo.getPlayerInfo(false).getGamesWon(),
                gameInfo.getPlayerInfo(true).getGamesWon(),
                gameInfo.getPlayerInfo(true).getName()
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
