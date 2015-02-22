package magic.ui.screen;

import magic.model.MagicCardList;
import magic.ui.canvas.cards.CardsCanvas;
import magic.ui.canvas.cards.CardsCanvas.LayoutMode;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.widget.MenuButton;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.util.Collections;
import java.util.List;
import magic.ui.CardImagesProvider;

@SuppressWarnings("serial")
public class CardZoneScreen
    extends AbstractScreen
    implements IStatusBar, IActionBar {

    private final static Dimension cardSize = CardImagesProvider.PREFERRED_CARD_SIZE;

    private final CardsCanvas content;
    private final String screenCaption;

    public CardZoneScreen(final MagicCardList cards, final String zoneName, final boolean animateCards) {
        this.screenCaption = zoneName;
        this.content = new CardsCanvas(cardSize);
        this.content.setAnimationEnabled(animateCards);
        this.content.setLayoutMode(LayoutMode.SCALE_TO_FIT);
        Collections.sort(cards);
        content.refresh(cards, cardSize);
        setContent(content);
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
        return MenuButton.getCloseScreenButton("Close");
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
     * @see magic.ui.screen.interfaces.IStatusBar#getStatusPanel()
     */
    @Override
    public JPanel getStatusPanel() {
        return null;
    }

}
