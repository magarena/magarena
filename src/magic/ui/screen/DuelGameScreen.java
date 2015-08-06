package magic.ui.screen;

import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutionException;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.SwingWorker;
import magic.data.GeneralConfig;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.ui.MagicFrame;
import magic.ui.ScreenController;
import magic.ui.ScreenOptionsOverlay;
import magic.translate.UiString;
import magic.ui.duel.DuelLayeredPane;
import magic.ui.duel.DuelPanel;
import magic.ui.screen.interfaces.IOptionsMenu;
import magic.ui.screen.widget.MenuPanel;
import magic.ui.widget.ZoneBackgroundLabel;
import magic.utility.MagicSystem;

@SuppressWarnings("serial")
public class DuelGameScreen extends AbstractScreen implements IOptionsMenu {

    // translatable strings
    private static final String _S1 = "Game Options";
    private static final String _S2 = "Concede game";
    private static final String _S3 = "Restart game";
    private static final String _S4 = "Image mode";
    private static final String _S5 = "Text mode";
    private static final String _S6 = "Game Log";
    private static final String _S7 = "Sidebar Layout";
    private static final String _S8 = "Resume game";

    private final static GeneralConfig config = GeneralConfig.getInstance();

    private DuelPanel gamePanel;
    private DuelLayeredPane gamePane;

    public DuelGameScreen(final MagicDuel duel) {
        
        final SwingWorker<MagicGame, Void> worker = new SwingWorker<MagicGame, Void> () {
            @Override
            protected MagicGame doInBackground() throws Exception {
                config.setTextView(false);
                return duel.nextGame();
            }
            @Override
            protected void done() {
                try {
                    setContent(getScreenContent(get()));
                    if (!config.showMulliganScreen() || MagicSystem.isAiVersusAi()) {
                        gamePane.setVisible(true);
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
        gamePane.setVisible(true);
        gamePanel.startGameThread();
    }

    private JComponent getScreenContent(final MagicGame game) {
        final ZoneBackgroundLabel backgroundLabel = new ZoneBackgroundLabel();
        backgroundLabel.setGame(true);
        gamePanel = new DuelPanel(getFrame(), game, backgroundLabel);
        gamePane = new DuelLayeredPane(gamePanel, backgroundLabel);
        gamePane.setVisible(false);
        return gamePane;
    }

    @Override
    public void showOptionsMenuOverlay() {
        if (gamePanel.getDialogPanel().isVisible()) {
            gamePanel.getDialogPanel().setVisible(false);
        } else {
            new ScreenOptions(getFrame(), this);
        }
    }

    public void updateView() {
        gamePanel.updateView();
        gamePane.setVisible(true);
        quickFixSpaceKeyShortcut();
    }

    public void concedeGame() {
        gamePanel.getController().concede();
    }

    public void resetGame() {
        gamePanel.getController().resetGame();
    }

    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        return true;
    }

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
            menu.addMenuItem(UiString.get(_S6), new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    ScreenController.showGameLogScreen();
                }
            });
            menu.addMenuItem(UiString.get(_S7), new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    hideOverlay();
                    ScreenController.showDuelSidebarDialog(gamePanel.getController());
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
        gamePanel.requestFocusInWindow();
    }

}
