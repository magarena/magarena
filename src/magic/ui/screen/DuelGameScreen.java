package magic.ui.screen;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.ui.MagicFrame;
import magic.ui.ScreenController;
import magic.ui.ScreenOptionsOverlay;
import magic.translate.UiString;
import magic.ui.duel.SwingGameController;
import magic.ui.duel.DuelLayeredPane;
import magic.ui.screen.interfaces.IOptionsMenu;
import magic.ui.screen.widget.MenuPanel;
import magic.ui.widget.GameLoadingMessage;
import magic.utility.MagicSystem;

@SuppressWarnings("serial")
public class DuelGameScreen extends AbstractScreen implements IOptionsMenu {

    // translatable strings
    private static final String _S1 = "Game Options";
    private static final String _S2 = "Concede game";
    private static final String _S3 = "Restart game";
    private static final String _S4 = "Image mode";
    private static final String _S7 = "Sidebar Layout";
    private static final String _S8 = "Resume game";
    private static final String _S9 = "Gameplay Report";

    private static final GeneralConfig config = GeneralConfig.getInstance();

    private DuelLayeredPane duelPane;
    private SwingGameController controller;
    private final GameLoadingMessage loadingMessage = new GameLoadingMessage();

    public DuelGameScreen(final MagicDuel duel) {
        showScreen(duel.nextGame());
    }

    // CTR - called when using -DtestGame=X argument.
    public DuelGameScreen(final MagicGame game) {
        showScreen(game);
    }

    public void showScreen(MagicGame aGame) {
        setContent(getScreenContent(aGame));
        if (!config.showMulliganScreen() || MagicSystem.isAiVersusAi() || MagicSystem.isTestGame()) {
            duelPane.setVisible(true);
            quickFixSpaceKeyShortcut();
        }
        startGameThread();
        loadingMessage.setEnabled(false);
    }

    private DuelLayeredPane getScreenContent(final MagicGame aGame) {
        duelPane = new DuelLayeredPane(aGame);
        controller = new SwingGameController(duelPane, aGame);
        return duelPane;
    }

    /**
     * Runs game loop on non-EDT.
     */
    private void startGameThread() {
        assert SwingUtilities.isEventDispatchThread();
        final Thread t = new Thread() {
            @Override
            public void run() {
                Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
                controller.runGame();
            }
        };
        t.setDaemon(true);
        t.start();
    }

    @Override
    public void showOptionsMenuOverlay() {
        if (duelPane == null) {
            //do nothing
        } else if (duelPane.getDialogPanel().isVisible()) {
            duelPane.getDialogPanel().setVisible(false);
        } else {
            pauseGame();
            new ScreenOptions(getFrame(), this);
        }
    }

    public void updateView() {
        duelPane.updateView();
        duelPane.setVisible(true);
        quickFixSpaceKeyShortcut();
    }

    public void concedeGame() {
        controller.concede();
    }

    public void resetGame() {
        controller.resetGame();
    }

    public void pauseGame() {
        controller.setGamePaused(true);
    }

    public void resumeGame() {
        controller.setGamePaused(false);
    }

    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        return true;
    }

    @Override
    public void requestFocus() {
        if (duelPane != null) {
            duelPane.requestFocus();
        }
    }

    private class ScreenOptions extends ScreenOptionsOverlay {

        private final DuelGameScreen screen;

        public ScreenOptions(final MagicFrame frame, final DuelGameScreen screen0) {
            super(frame);
            this.screen = screen0;
        }

        @Override
        protected MenuPanel getScreenMenu() {

            final MenuPanel menu = new MenuPanel(UiString.get(_S1));

            menu.addMenuItem(UiString.get(_S2), new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    screen.concedeGame();
                    hideOverlay();
                }
            });
            menu.addMenuItem(UiString.get(_S3), new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    screen.resetGame();
                    hideOverlay();
                }
            });
            menu.addMenuItem(UiString.get(_S7), new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    hideOverlay();
                    ScreenController.showDuelSidebarDialog(controller);
                }
            });
            menu.addBlankItem();
            menu.addMenuItem(UiString.get(_S9), new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    controller.createGameplayReport();
                    hideOverlay();
                }
            });
            menu.addBlankItem();
            menu.addMenuItem(UiString.get(_S8), new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    hideOverlay();
                }
            });

            menu.refreshLayout();
            return menu;

        }

        @Override
        protected boolean showPreferencesOption() {
            return true;
        }

        @Override
        public void hideOverlay() {
            setVisible(false);
            screen.resumeGame();
        }
    }

    /**
     * This is a bit of a hack which ensures that the SPACE key works
     * as expected in the duel/game screen. Addresses issue 481.
     * TODO: look at implementing a more consistent focusing system.
     */
    private void quickFixSpaceKeyShortcut() {
        duelPane.requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        loadingMessage.render(g, getSize());
        super.paintComponent(g);
    }

}
