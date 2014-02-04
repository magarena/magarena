package magic.ui.screen;

import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import net.miginfocom.swing.MigLayout;
import magic.data.GeneralConfig;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicGameReport;
import magic.ui.GameLayeredPane;
import magic.ui.GamePanel;
import magic.ui.MagicFrame;
import magic.ui.ScreenOptionsOverlay;
import magic.ui.screen.interfaces.IOptionsMenu;
import magic.ui.screen.widget.MenuPanel;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.ZoneBackgroundLabel;


@SuppressWarnings("serial")
public class DuelGameScreen extends AbstractScreen implements IOptionsMenu {

    private GamePanel gamePanel;
    private GameLayeredPane gamePane;

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
                } catch (InterruptedException | ExecutionException e1) {
                    e1.printStackTrace();
                    MagicGameReport.reportException(Thread.currentThread(), e1);
                }
            }
        };
        worker.execute();

    }

    public DuelGameScreen(final MagicGame game) {
        setContent(getScreenContent(game));
    }

    private JPanel getScreenContent(final MagicGame game) {
        final ZoneBackgroundLabel backgroundLabel = new ZoneBackgroundLabel();
        backgroundLabel.setGame(true);
        gamePanel = new GamePanel(getFrame(), game, backgroundLabel);
        gamePane = new GameLayeredPane(gamePanel, backgroundLabel);
        final JPanel container = new JPanel(new MigLayout("insets 0, gap 0"));
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
            final boolean isTextMode = GeneralConfig.getInstance().getTextView();
            menu.addMenuItem(isTextMode ? "Image mode" : "Text mode", new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    GeneralConfig.getInstance().setTextView(!isTextMode);
                    screen.updateView();
                    setVisible(false);
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
            menu.setBackground(FontsAndBorders.IMENUOVERLAY_MENUPANEL_COLOR);
            return menu;

        }

    }

}
