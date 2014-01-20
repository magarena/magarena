package magic.ui.screen;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicCardList;
import magic.model.MagicDeck;
import magic.model.MagicRandom;
import magic.ui.MagicFrame;
import magic.ui.ScreenOptionsOverlay;
import magic.ui.canvas.cards.CardsCanvas;
import magic.ui.canvas.cards.CardsCanvas.LayoutMode;
import magic.ui.canvas.cards.ICardCanvas;
import magic.ui.interfaces.IMagActionBar;
import magic.ui.interfaces.IMagScreenOptionsMenu;
import magic.ui.interfaces.IMagStatusBar;
import magic.ui.widget.MenuButton;
import magic.ui.widget.MenuPanel;

@SuppressWarnings("serial")
public class SampleHandScreen
    extends AbstractScreen
    implements IMagStatusBar, IMagActionBar, IMagScreenOptionsMenu {

    private final MagicFrame frame;
    private static CardsCanvas content;
    private final MagicDeck deck;
    private final static Dimension cardSize = new Dimension(480, 680);

    public SampleHandScreen(final MagicFrame frame0, final MagicDeck deck) {
        super(getScreenContent(deck), frame0);
        frame = frame0;
        this.deck = deck;
    }

    private static JPanel getScreenContent(final MagicDeck deck) {
        content = new CardsCanvas(cardSize);
        content.setLayoutMode(LayoutMode.SCALE_TO_FIT);
        content.refresh(getHandCards(deck), cardSize);
        return content;
    }

    private static List<? extends ICardCanvas> getHandCards(final MagicDeck deck) {
        final MagicCardList library = new MagicCardList();
        for (MagicCardDefinition magicCardDef : deck) {
            library.add(new MagicCard(magicCardDef, null, 0));
        }
        library.shuffle(MagicRandom.nextRNGInt(999999));
        if (library.size() >= 7) {
            final List<MagicCard> hand = library.subList(0, 7);
            Collections.sort(hand);
            return hand;
        } else {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagStatusBar#getScreenCaption()
     */
    @Override
    public String getScreenCaption() {
        return "Sample Hand";
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
        final List<MenuButton> buttons = new ArrayList<MenuButton>();
        buttons.add(new MenuButton("Deal new hand", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                content.refresh(getHandCards(deck), cardSize);
            }
        }, "Generate a new sample hand"));
        return buttons;
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
