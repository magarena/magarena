package magic.ui.screen;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import magic.model.MagicCardList;
import magic.ui.canvas.cards.CardsCanvas;
import magic.ui.canvas.cards.CardsCanvas.LayoutMode;
import magic.ui.interfaces.IMagActionBar;
import magic.ui.interfaces.IMagStatusBar;
import magic.ui.widget.MenuButton;

@SuppressWarnings("serial")
public class CardZoneScreen
    extends AbstractScreen
    implements IMagStatusBar, IMagActionBar {

    private static CardsCanvas content;
    private final static Dimension cardSize = new Dimension(480, 680);
    private static String screenCaption;

    public CardZoneScreen(
            final MagicCardList cards,
            final String zoneName,
            final boolean animateCards) {
        super(getScreenContent(cards, zoneName, animateCards));
    }

    private static JPanel getScreenContent(
            final MagicCardList cards,
            final String zoneName,
            final boolean animateCards) {
        screenCaption = zoneName;
        content = new CardsCanvas(cardSize);
        content.setAnimationEnabled(animateCards);
        content.setLayoutMode(LayoutMode.SCALE_TO_FIT);
        Collections.sort(cards);
        // Important: uses Runnable so painting works properly.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                content.refresh(cards, cardSize);
            }
        });
        return content;
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagStatusBar#getScreenCaption()
     */
    @Override
    public String getScreenCaption() {
        return screenCaption;
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

}
