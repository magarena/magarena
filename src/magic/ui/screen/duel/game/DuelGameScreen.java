package magic.ui.screen.duel.game;

import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.ui.screen.MScreen;
import magic.utility.MagicSystem;

@SuppressWarnings("serial")
public class DuelGameScreen extends MScreen {

    private static final GeneralConfig config = GeneralConfig.getInstance();

    private DuelLayeredPane duelPane;
    private SwingGameController controller;

    public DuelGameScreen(final MagicDuel duel) {
        showScreen(duel.nextGame());
    }

    // CTR - called when using -DtestGame=X or -DsaveGame=X
    public DuelGameScreen(final MagicGame game) {
        showScreen(game);
    }

    public void showScreen(MagicGame aGame) {
        setMainContent(getScreenContent(aGame));
        if (!config.showMulliganScreen() || MagicSystem.isAiVersusAi() || MagicSystem.isTestGame()) {
            duelPane.setVisible(true);
            quickFixSpaceKeyShortcut();
        }
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

    public void showOptionsMenuOverlay() {
        controller.showGameOptionsOverlay();
    }

    public void updateView() {
        duelPane.updateView();
        duelPane.setVisible(true);
        quickFixSpaceKeyShortcut();
    }

    @Override
    public void requestFocus() {
        if (duelPane != null) {
            duelPane.requestFocus();
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

}
