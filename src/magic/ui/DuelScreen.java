package magic.ui;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import magic.model.MagicGame;
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
        new DuelScreenOptions(frame);
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
    public boolean isScreenReadyToClose(MagScreen nextScreen) {
        return true;
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#requestFocus()
     */
    @Override
    public void requestFocus() {
        gamePanel.requestFocus();
    }

}
