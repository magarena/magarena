package magic.ui.screen;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutionException;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
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
    private static final String _S5 = "Text mode";
    private static final String _S7 = "Sidebar Layout";
    private static final String _S8 = "Resume game";
    private static final String _S9 = "Gameplay Report";

    private static final GeneralConfig config = GeneralConfig.getInstance();

    private DuelLayeredPane duelPane;
    private SwingGameController controller;
    private final GameLoadingMessage loadingMessage = new GameLoadingMessage();

    public DuelGameScreen(final MagicDuel duel) {
        new StartupWorker(duel).execute();
    }

    // CTR - called when using -DtestGame=X argument.
    public DuelGameScreen(final MagicGame game) {
        setContent(getScreenContent(game));
        duelPane.setVisible(true);
        startGameThread();
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
        if (duelPane.getDialogPanel().isVisible()) {
            duelPane.getDialogPanel().setVisible(false);
        } else {
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
                    setVisible(false);
                }
            });
            menu.addMenuItem(UiString.get(_S3), new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    screen.resetGame();
                    setVisible(false);
                }
            });
            final boolean isTextMode = config.getTextView();
            menu.addMenuItem(isTextMode ? UiString.get(_S4) : UiString.get(_S5), new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    config.setTextView(!isTextMode);
                    screen.updateView();
                    setVisible(false);
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
                    setVisible(false);
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

    private final class StartupWorker extends SwingWorker<MagicGame, Void> {

        private final static int MINWAIT = 3000; // millisecs
        private final MagicDuel duel;

        public StartupWorker(final MagicDuel aDuel) {
            this.duel = aDuel;
        }

        @Override
        protected MagicGame doInBackground() throws Exception {
            final long start_time = System.currentTimeMillis();
            config.setTextView(false);
            final MagicGame game = duel.nextGame();
            final long duration = System.currentTimeMillis() - start_time;
            Thread.sleep(duration < MINWAIT ? MINWAIT - duration : 1);
            return game;
        }

        @Override
        protected void done() {
            try {
                setContent(getScreenContent(get()));
                if (!config.showMulliganScreen() || MagicSystem.isAiVersusAi()) {
                    duelPane.setVisible(true);
                    quickFixSpaceKeyShortcut();
                }
            } catch (InterruptedException | ExecutionException e1) {
                throw new RuntimeException(e1);
            }
            startGameThread();
            loadingMessage.setEnabled(false);
        }

    }

}
