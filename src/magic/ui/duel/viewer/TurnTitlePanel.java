package magic.ui.duel.viewer;

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
import magic.model.MagicGame;
import magic.ui.IconImages;
import magic.ui.MagicStyle;
import magic.ui.SwingGameController;
import magic.ui.screen.interfaces.IOptionsMenu;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.theme.Theme;
import magic.utility.MagicSystem;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class TurnTitlePanel extends JPanel {

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
        miglayout.setLayoutConstraints("insets 0 3 0 4, gap 0 2, flowy, wrap 2");
        miglayout.setColumnConstraints("[fill]push[]");
        removeAll();
        add(scoreLabel);
        add(gameLabel);
        add(getOptionsIconButton(), "spany 2, aligny bottom");
    }

    private JButton getOptionsIconButton() {

        final JButton btn = new ActionBarButton(
                IconImages.getIcon(MagicIcon.OPTIONS_ICON),
                "Options Menu [ESC]",
                "Displays menu of common and screen specific options.",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        showOptionsMenu();
                    }
                }
        );
        btn.setMaximumSize(new Dimension(30, 30));

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

    public void refresh(final MagicGame game) {
        scoreLabel.setText(getScoreString());
        scoreLabel.setToolTipText(String.format("First player to %d wins the duel.",
                game.getDuel().getConfiguration().getGamesRequiredToWinDuel())
        );
        gameLabel.setText(String.format("Game %d / %d  •  Turn %d  •  %s",
                game.getDuel().getGameNr(),
                game.getDuel().getGamesTotal(),
                game.getTurn(),
                game.getTurnPlayer().getName())
        );
    }
    
    private String getScoreString() {
        final MagicGame game = controller.getGame();
        final ViewerInfo boardInfo = controller.getViewerInfo();
        return String.format("%s %d - %d %s",
                boardInfo.getPlayerInfo(false).name,
                game.getDuel().getGamesWon(),
                game.getDuel().getGamesPlayed() - game.getDuel().getGamesWon(),
                boardInfo.getPlayerInfo(true).name
        );
    }

    private void setButtonTransparent(final JButton btn) {
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setBorder(null);
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
