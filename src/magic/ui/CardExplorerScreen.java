package magic.ui;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import magic.ui.widget.MenuButton;

@SuppressWarnings("serial")
public class CardExplorerScreen
    extends MagScreen
    implements IMagStatusBar, IMagActionBar, IMagScreenOptionsMenu {

    private final MagicFrame frame;

    public CardExplorerScreen(final MagicFrame frame0) {
        super(getScreenContent(frame0), frame0);
        frame = frame0;
    }

    private static JPanel getScreenContent(MagicFrame frame) {
        final ExplorerPanel content = new ExplorerPanel(frame);
        return content;
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagStatusBar#getScreenCaption()
     */
    @Override
    public String getScreenCaption() {
        return "Card Explorer";
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getLeftAction()
     */
    @Override
    public MenuButton getLeftAction() {
        return new MenuButton("Close", new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
              frame.closeActiveScreen(false);
          }
      });
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getRightAction()
     */
    @Override
    public MenuButton getRightAction() {
        return null;
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getMiddleActions()
     */
    @Override
    public List<MenuButton> getMiddleActions() {
        return null;
    }

    /* (non-Javadoc)
     * @see magic.ui.MagScreen#canScreenClose()
     */
    @Override
    public boolean isScreenReadyToClose(final MagScreen nextScreen) {
        return true;
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagScreenOptionsMenu#showOptionsMenuOverlay()
     */
    @Override
    public void showOptionsMenuOverlay() {
        new CardExplorerScreenOptions(frame);
    }

}
