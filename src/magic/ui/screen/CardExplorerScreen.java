package magic.ui.screen;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;

import magic.ui.ExplorerPanel;
import magic.ui.MagicFrame;
import magic.ui.ScreenOptionsOverlay;
import magic.ui.interfaces.IMagActionBar;
import magic.ui.interfaces.IMagScreenOptionsMenu;
import magic.ui.interfaces.IMagStatusBar;
import magic.ui.widget.MenuButton;
import magic.ui.widget.MenuPanel;

@SuppressWarnings("serial")
public class CardExplorerScreen
    extends AbstractScreen
    implements IMagStatusBar, IMagActionBar, IMagScreenOptionsMenu {

    public CardExplorerScreen() {
        super(new ExplorerPanel());
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
          public void actionPerformed(final ActionEvent e) {
              getFrame().closeActiveScreen(false);
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
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        return true;
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagScreenOptionsMenu#showOptionsMenuOverlay()
     */
    @Override
    public void showOptionsMenuOverlay() {
        new ScreenOptions(getFrame());
    }

    private class ScreenOptions extends ScreenOptionsOverlay {

        public ScreenOptions(final MagicFrame frame) {
            super(frame);
        }

        /* (non-Javadoc)
         * @see magic.ui.ScreenOptionsOverlay#getScreenMenu()
         */
        @Override
        protected MenuPanel getScreenMenu() {
            return null;
        }

    }

}
