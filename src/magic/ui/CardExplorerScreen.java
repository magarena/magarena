package magic.ui;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;

import magic.ui.widget.MenuButton;
import magic.ui.widget.MenuPanel;

@SuppressWarnings("serial")
public class CardExplorerScreen
    extends MagScreen
    implements IMagStatusBar, IMagActionBar, IMagScreenOptionsMenu {

    private final MagicFrame frame;

    public CardExplorerScreen(final MagicFrame frame0) {
        super(new ExplorerPanel(), frame0);
        frame = frame0;
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
        new ScreenOptions(frame);
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
