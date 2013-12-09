package magic.ui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import magic.data.GeneralConfig;
import magic.model.MagicGame;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.MenuPanel;
import magic.ui.widget.ZoneBackgroundLabel;

@SuppressWarnings("serial")
public class DuelScreen extends MagScreen implements IMagScreenOptionsMenu {

    private final MagicFrame frame;
    private static GamePanel gamePanel;

    public DuelScreen(final MagicFrame frame0, final MagicGame game) {
        super(getScreenContent(frame0, game), frame0);
        this.frame = frame0;
    }

    private static JPanel getScreenContent(final MagicFrame frame, final MagicGame game) {
        final ZoneBackgroundLabel backgroundLabel = new ZoneBackgroundLabel();
        backgroundLabel.setGame(true);
        gamePanel = new GamePanel(frame, game, backgroundLabel);
        final GameLayeredPane gamePane = new GameLayeredPane(gamePanel, backgroundLabel);
        final JPanel container = new JPanel(new MigLayout("insets 0, gap 0"));
        container.add(gamePane, "w 100%, h 100%");
        return container;
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagScreenOptionsMenu#showOptionsMenuOverlay()
     */
    @Override
    public void showOptionsMenuOverlay() {
        new ScreenOptions(frame, this);
    }

    public void updateView() {
        gamePanel.updateView();
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
    public boolean isScreenReadyToClose(final MagScreen nextScreen) {
        return true;
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#requestFocus()
     */
    @Override
    public void requestFocus() {
        gamePanel.requestFocus();
    }

    private class ScreenOptions extends ScreenOptionsOverlay {

        private final DuelScreen screen;

        public ScreenOptions(final MagicFrame frame, final DuelScreen screen0) {
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
