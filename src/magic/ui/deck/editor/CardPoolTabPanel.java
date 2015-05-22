package magic.ui.deck.editor;

import magic.ui.deck.editor.CardPoolOptionBar;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import magic.data.MagicIcon;
import magic.ui.IconImages;
import magic.model.MagicCardDefinition;
import magic.ui.screen.widget.ActionBarButton;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class CardPoolTabPanel extends JPanel {

    // fired when contents of cardPoolTable are updated.
    public static final String CP_CARDPOOL = DeckEditorCardPoolPanel.CP_CARDPOOL;
    // fired when card selection changes
    public static final String CP_CARD_SELECTED = DeckEditorCardPoolPanel.CP_CARD_SELECTED;
    // fired on add card to deck action.
    public static final String CP_ADD_TO_DECK = "addCardToDeck";
    // fired on remove card from deck action.
    public static final String CP_REMOVE_FROM_DECK = "removeCardFromDeck";
    // fire on create random deck.
    public static final String CP_RANDOM_DECK = "createRandomDeck";

    // UI components
    private final DeckEditorCardPoolPanel cardPoolPanel;
    private final DeckCardPoolActionBar actionBar;
    private final CardPoolOptionBar optionBar;
    //
    private final MigLayout miglayout = new MigLayout();
    private boolean isFilterVisible = true;

    public CardPoolTabPanel() {
        //
        cardPoolPanel = new DeckEditorCardPoolPanel();
        cardPoolPanel.setFilterVisible(isFilterVisible);
        //
        optionBar = new CardPoolOptionBar();
        optionBar.setVisible(false);
        optionBar.addActionButton(getFilterActionButton());
        optionBar.addActionButton(getRandomDeckActionButton());
        //
        actionBar = new DeckCardPoolActionBar(getPlusButtonAction(), getMinusButtonAction());
        setActionBarPropChangeListener();
        //
        setLookAndFeel();
        refreshLayout();
        //
        cardPoolPanel.addPropertyChangeListener(
                DeckEditorCardPoolPanel.CP_CARDPOOL,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        firePropertyChange(CP_CARDPOOL, false, true);
                    }
                });
        cardPoolPanel.addPropertyChangeListener(
                DeckEditorCardPoolPanel.CP_CARD_SELECTED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        firePropertyChange(CP_CARD_SELECTED, false, true);
                    }
                });
        cardPoolPanel.addPropertyChangeListener(
                DeckEditorCardPoolPanel.CP_CARD_LCLICKED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        addSelectedCardToDeck();
                    }
                });
        cardPoolPanel.addPropertyChangeListener(
                DeckEditorCardPoolPanel.CP_CARD_RCLICKED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        removeSelectedCardFromDeck(true);
                    }
                });
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

    private ActionBarButton getFilterActionButton() {
        return new ActionBarButton(
                IconImages.getIcon(MagicIcon.FILTER_ICON),
                "Toggle Filter Panel",
                "Hide/show the card pool filter panel.",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        isFilterVisible = !isFilterVisible;
                        cardPoolPanel.setFilterVisible(isFilterVisible);
                    }
                });
    }

    private ActionBarButton getRandomDeckActionButton() {
        return new ActionBarButton(
                IconImages.getIcon(MagicIcon.RANDOM_ICON),
                "Random Deck",
                "Generate a random deck using current set of cards in card pool.",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        CardPoolTabPanel.this.firePropertyChange(CP_RANDOM_DECK, false, true);
                    }
                }, true);
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
        add(cardPoolPanel, "w 100%, h 100%");
        revalidate();
    }
    
    public int getCardPoolSize() {
        return cardPoolPanel.getCardPool().size();
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
        for (int i = 0; i < actionBar.getQuantity(); i++) {
            firePropertyChange(CP_REMOVE_FROM_DECK, false, true);
        }
    }

    public MagicCardDefinition getSelectedCard() {
        return cardPoolPanel.getSelectedCard();
    }

    @Override
    public boolean requestFocusInWindow() {
        return cardPoolPanel.requestFocusInWindow();
    }

    Collection<MagicCardDefinition> getCardPool() {
        return cardPoolPanel.getCardPool();
    }

}
