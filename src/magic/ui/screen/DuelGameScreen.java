package magic.ui.screen;

import magic.data.GeneralConfig;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.ui.duel.GameLayeredPane;
import magic.ui.duel.DuelPanel;
import magic.ui.MagicFrame;
import magic.ui.ScreenOptionsOverlay;
import magic.ui.screen.interfaces.IOptionsMenu;
import magic.ui.screen.widget.MenuPanel;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.ZoneBackgroundLabel;
import net.miginfocom.swing.MigLayout;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutionException;


@SuppressWarnings("serial")
public class DuelGameScreen extends AbstractScreen implements IOptionsMenu {

    private final static GeneralConfig config = GeneralConfig.getInstance();

    private DuelPanel gamePanel;
    private GameLayeredPane gamePane;
    private JPanel container;

    public DuelGameScreen(final MagicDuel duel) {

        final SwingWorker<MagicGame, Void> worker = new SwingWorker<MagicGame, Void> () {
            @Override
            protected MagicGame doInBackground() throws Exception {
                duel.updateDifficulty();
                return duel.nextGame(true);
            }
            @Override
            protected void done() {
                try {
                    setContent(getScreenContent(get()));
                    if (!config.showMulliganScreen() || Boolean.getBoolean("selfMode")) {
                        container.setVisible(true);
                        quickFixSpaceKeyShortcut();
                    }
                } catch (InterruptedException | ExecutionException e1) {
                    throw new RuntimeException(e1);
                }
                gamePanel.startGameThread();
            }
        };
        worker.execute();

    }

    // CTR - called when using -DtestGame=X argument.
    public DuelGameScreen(final MagicGame game) {
        setContent(getScreenContent(game));
        container.setVisible(true);
        gamePanel.startGameThread();
    }

    private JPanel getScreenContent(final MagicGame game) {
        final ZoneBackgroundLabel backgroundLabel = new ZoneBackgroundLabel();
        backgroundLabel.setGame(true);
        gamePanel = new DuelPanel(getFrame(), game, backgroundLabel);
        gamePane = new GameLayeredPane(gamePanel, backgroundLabel);
        container = new JPanel(new MigLayout("insets 0, gap 0"));
        container.setVisible(false);
        container.setOpaque(false);
        container.add(gamePane, "w 100%, h 100%");
        return container;
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagScreenOptionsMenu#showOptionsMenuOverlay()
     */
    @Override
    public void showOptionsMenuOverlay() {
        new ScreenOptions(getFrame(), this);
    }

    public void updateView() {
        gamePanel.updateView();
        gamePane.updateView();
        container.setVisible(true);
        quickFixSpaceKeyShortcut();
    }

    public void concedeGame() {
        gamePanel.getController().concede();
    }

    public void resetGame() {
        gamePanel.getController().resetGame();
    }

    /* (non-Javadoc)
     * @see magic.ui.MagScreen#isScreenReadyToClose(magic.ui.MagScreen)
     */
    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        return true;
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#requestFocus()
     */
    @Override
    public void requestFocus() {
        if (gamePanel != null) {
            gamePanel.requestFocus();
        }
    }

    private class ScreenOptions extends ScreenOptionsOverlay {

        private final DuelGameScreen screen;

        public ScreenOptions(final MagicFrame frame, final DuelGameScreen screen0) {
            super(frame);
            this.screen = screen0;
        }

        /* (non-Javadoc)
         * @see magic.ui.ScreenOptionsOverlay#getScreenMenu()
         */
        @Override
        protected MenuPanel getScreenMenu() {

            final MenuPanel menu = new MenuPanel("Game Options");

            menu.addMenuItem("Concede game", new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    screen.concedeGame();
                    setVisible(false);
                }
            });
            menu.addMenuItem("Restart game", new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    screen.resetGame();
                    setVisible(false);
                }
            });
            final boolean isTextMode = config.getTextView();
            menu.addMenuItem(isTextMode ? "Image mode" : "Text mode", new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    config.setTextView(!isTextMode);
                    screen.updateView();
                    setVisible(false);
                }
            });
            menu.addMenuItem("Game Log", new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    getFrame().showGameLogScreen();
                }
            });
            menu.addBlankItem();
            menu.addMenuItem("Close menu", new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    hideOverlay();
                }
            });

            menu.refreshLayout();
            return menu;

        }

    }

    /**
     * This is a bit of a hack which ensures that the SPACE key works
     * as expected in the duel/game screen. Addresses issue 481.
     * TODO: look at implementing a more consistent focusing system.
     */
    private void quickFixSpaceKeyShortcut() {
        gamePanel.requestFocusInWindow();
    }

}
