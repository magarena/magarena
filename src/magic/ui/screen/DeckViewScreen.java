package magic.ui.screen;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import magic.data.GeneralConfig;
import magic.data.IconImages;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.ui.canvas.cards.CardsCanvas;
import magic.ui.canvas.cards.CardsCanvas.LayoutMode;
import magic.ui.canvas.cards.ICardCanvas;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuButton;

@SuppressWarnings("serial")
public class DeckViewScreen
    extends AbstractScreen
    implements IStatusBar, IActionBar {

    private final static Dimension cardSize = GeneralConfig.PREFERRED_CARD_SIZE;

    private CardsCanvas content;
    private final MagicDeck deck;

    public DeckViewScreen(final MagicDeck deck) {
        this.deck = deck;
        content = new CardsCanvas(cardSize);
        content.setAnimationEnabled(false);
        content.setStackDuplicateCards(true);
        content.setLayoutMode(LayoutMode.SCALE_TO_FIT);
        content.refresh(getDeckCards(deck), cardSize);
        setContent(content);
    }

    private List<? extends ICardCanvas> getDeckCards(final MagicDeck deck) {
        final List<MagicCard> cards = new ArrayList<MagicCard>();
        for (MagicCardDefinition magicCardDef : deck) {
            cards.add(new MagicCard(magicCardDef, null, 0));
        }
        if (cards.size() > 0) {
            Collections.sort(cards);
            return cards;
        } else {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagStatusBar#getScreenCaption()
     */
    @Override
    public String getScreenCaption() {
        return "Deck View";
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getLeftAction()
     */
    @Override
    public MenuButton getLeftAction() {
        return new ActionBarButton("Close", new AbstractAction() {
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
        final List<MenuButton> buttons = new ArrayList<MenuButton>();
        buttons.add(
                new ActionBarButton(
                        IconImages.HAND_ICON,
                        "Sample Hand", "See what kind of Hand you might be dealt from this deck.",
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                if (deck.size() >= 7) {
                                    getFrame().showSampleHandGenerator(deck);
                                } else {
                                    showInvalidActionMessage("A deck with a minimum of 7 cards is required first.");
                                }
                            }
                        })
                );
        return buttons;
    }

    private void showInvalidActionMessage(final String message) {
        JOptionPane.showMessageDialog(this, message, "Invalid Action", JOptionPane.INFORMATION_MESSAGE);
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