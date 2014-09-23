package magic.ui.explorer;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import magic.MagicMain;
import magic.data.GeneralConfig;
import magic.data.IconImages;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.ui.cardtable.CardTablePanel;
import magic.ui.screen.widget.ActionBarButton;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckTabPanel extends JPanel {

    // fired when contents of deck list are updated.
    public static final String CP_DECKLIST= "DeckList";
    // fired when card selection changes
    public static final String CP_CARD_SELECTED = CardTablePanel.CP_CARD_SELECTED;
    // fired on add card to deck action.
    public static final String CP_ADD_TO_DECK = "addCardToDeck";
    // fired on remove card from deck action.
    public static final String CP_REMOVE_FROM_DECK = "removeCardFromDeck";

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();

    // UI components
    private final DeckCardPoolActionBar actionBar;
    private final DeckListOptionBar optionBar;
    private final CardTablePanel deckTable;
    //
    private final MigLayout miglayout = new MigLayout();
    
    private MagicDeck deck;
    private MagicDeck originalDeck;

    public DeckTabPanel(final MagicDeck originalDeck) {
        this.originalDeck = originalDeck;
        this.deck = getDeckCopy(originalDeck);
        //
        optionBar = new DeckListOptionBar();
        optionBar.setVisible(false);
        setOptionBarActions();
        //
        actionBar = new DeckCardPoolActionBar(getPlusButtonAction(), getMinusButtonAction());
        setActionBarPropChangeListener();
        //
        deckTable = new CardTablePanel(this.deck, getDeckTitle(this.deck));
        deckTable.setDeckEditorSelectionMode();
        deckTable.setHeaderVisible(false);
        deckTable.showCardCount(true);
        setDeckTablePropChangeListeners();
        //
        setLookAndFeel();
        refreshLayout();
        //
        if (this.deck.size() > 0) {
            deckTable.selectFirstRow();
        }
    }

    /**
     * work with a copy of the original deck so it is easy to cancel updates.
     */
    private MagicDeck getDeckCopy(final MagicDeck deck) {
        final MagicDeck deckCopy = new MagicDeck();
        if (deck != null) {
            deckCopy.setContent(originalDeck);
        }
        return deckCopy;
    }

    private void setActionBarPropChangeListener() {
        actionBar.addPropertyChangeListener(
                DeckCardPoolActionBar.CP_OPTIONBAR,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        optionBar.setVisible(!optionBar.isVisible());
                        refreshLayout();
                    }
                });
    }
    
    private void setOptionBarActions() {
        optionBar.addActionButton(
                new ActionBarButton(
                        IconImages.CLEAR_ICON,
                        "Clear deck",
                        "Remove all cards from deck. Confirmation required.",
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                doClearDeck();
                            }
                        }, true));
        optionBar.addActionButton(
                new ActionBarButton(
                        IconImages.SAVE_ICON,
                        "Save Deck", "Save deck to file.",
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                //saveDeck();
                            }
                        })
        );
    }

    private void setDeckTablePropChangeListeners() {
        deckTable.addPropertyChangeListener(
                CardTablePanel.CP_CARD_SELECTED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        firePropertyChange(CP_CARD_SELECTED, false, true);
                    }
                });
        deckTable.addPropertyChangeListener(
                CardTablePanel.CP_CARD_LCLICKED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        addSelectedCardToDeck();
                    }
                });
        deckTable.addPropertyChangeListener(
                CardTablePanel.CP_CARD_RCLICKED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        removeSelectedCardFromDeck(true);
                    }
                });
    }

    private String getDeckTitle(final MagicDeck deck) {
        return "  " + deck.getName();
    }

    private void setLookAndFeel() {
        setOpaque(false);
        setLayout(miglayout);
    }

    private void refreshLayout() {
        removeAll();
        miglayout.setLayoutConstraints("insets 0, gap 0, wrap 2, flowy");
        add(actionBar, "w 40!, h 100%, spany 2");
        add(optionBar, "w 100%, h 36!, hidemode 3");
        add(deckTable, "w 100%, h 100%");
        revalidate();
    }

    private void doClearDeck() {
        if (deck.size() > 0) {
            final int userResponse
                    = JOptionPane.showConfirmDialog(
                            MagicMain.rootFrame,
                            "Remove all cards from deck?",
                            "Clear Deck",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
            if (userResponse == JOptionPane.YES_OPTION) {
                setDeck(new MagicDeck());
                firePropertyChange(CP_DECKLIST, false, true);
//                if (isStandaloneDeckEditor()) {
                    CONFIG.setMostRecentDeckFilename("");
                    CONFIG.save();
//                }
            }
        } else {
            JOptionPane.showMessageDialog(MagicMain.rootFrame, "Invalid action.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setDeck(final MagicDeck originalDeck) {
        if (originalDeck == null) {
            this.originalDeck = new MagicDeck();
        } else {
            this.originalDeck = originalDeck;
        }
        this.deck = getDeckCopy(originalDeck);
        deckTable.setTitle(getDeckTitle(this.deck));
        deckTable.setCards(deck);
        deckTable.selectFirstRow();
        firePropertyChange(CP_DECKLIST, false, true);
    }

    public MagicDeck getDeck() {
        return deck;
    }

    public MagicCardDefinition getSelectedCard() {
        if (deckTable.getSelectedCards().size() > 0) {
            return deckTable.getSelectedCards().get(0);
        } else {
            return MagicCardDefinition.UNKNOWN;
        }
    }

    private AbstractAction getPlusButtonAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSelectedCardToDeck();
            }
        };
    }

    private AbstractAction getMinusButtonAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelectedCardFromDeck(false);
            }
        };
    }

    private void addSelectedCardToDeck() {
        for (int i = 0; i < actionBar.getQuantity(); i++) {
            firePropertyChange(CP_ADD_TO_DECK, false, true);
        }
    }

    private void removeSelectedCardFromDeck(final boolean isMouseClick) {
        final int cardCount = deck.getCardCount(getSelectedCard());
        if (cardCount == 1 && isMouseClick) {
            // prevent removal of card from deck via mouseclick
        } else {
            final int quantity = Math.min(cardCount, actionBar.getQuantity());
            for (int i = 0; i < quantity; i++) {
                firePropertyChange(CP_REMOVE_FROM_DECK, false, true);
            }
        }
    }

    public void addCardToDeck(final MagicCardDefinition card) {
        deck.add(card);
        deckTable.setCards(deck);
        deckTable.setSelectedCard(card);
        firePropertyChange(CP_DECKLIST, false, true);
    }

    void removeCardFromDeck(MagicCardDefinition card) {
        deck.remove(card);
        deckTable.setCards(deck);
        deckTable.setSelectedCard(card);
        firePropertyChange(CP_DECKLIST, false, true);
    }

    @Override
    public boolean requestFocusInWindow() {
        if (deckTable.getSelectedCards().isEmpty()) {
            deckTable.selectFirstRow();
        }
        return super.requestFocusInWindow();
    }

    void updateOriginalDeck() {
        originalDeck.setContent(deck);        
    }

    boolean isUpdatingExistingDeck() {
        return originalDeck != null;
    }

}
