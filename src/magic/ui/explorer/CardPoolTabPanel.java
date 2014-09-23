package magic.ui.explorer;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import magic.data.IconImages;
import magic.model.MagicCardDefinition;
import magic.ui.cardpool.DeckEditorCardPoolPanel;
//import magic.ui.cardtable.ICardConsumer;
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
    private boolean isFilterVisible = false;

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
                        for (int i = 0; i < actionBar.getQuantity(); i++) {
                            CardPoolTabPanel.this.firePropertyChange(CP_ADD_TO_DECK, false, true);
                        }
                    }
                });
        cardPoolPanel.addPropertyChangeListener(
                DeckEditorCardPoolPanel.CP_CARD_RCLICKED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        for (int i = 0; i < actionBar.getQuantity(); i++) {
                            CardPoolTabPanel.this.firePropertyChange(CP_REMOVE_FROM_DECK, false, true);
                        }
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
                IconImages.FILTER_ICON,
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
                IconImages.RANDOM_ICON,
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
    
//    public void setCardConsumer(final ICardConsumer consumer) {
//        cardPoolPanel.setCardConsumer(consumer);
//    }

//    public MagicDeck getRandomDeck() {
//        if (generateRandomDeck()) {
////                          tabbedPane.setSelectedIndex(0);
//        }
//        return new MagicDeck();
//    }
//
//    private boolean generateRandomDeck() {
//        final RandomDeckGeneratorDialog dialog =
//                new RandomDeckGeneratorDialog(MagicMain.rootFrame, cardPoolPanel.getCardPool().size());
//        dialog.setVisible(true);
//        if (!dialog.isCancelled()) {
//            final DeckGenerator deckGenerator = dialog.getDeckGenerator();
//            final Collection<MagicCardDefinition> cardPool = cardPoolPanel.getCardPool(); // filterPanel.getCardDefinitions(false);
//            final MagicCubeDefinition cubeDefinition = CubeDefinitions.createCube(cardPool);
//            final RandomDeckGenerator generator = new RandomDeckGenerator(cubeDefinition);
//            final MagicDeck randomDeck = new MagicDeck();
//            final MagicDeckProfile deckProfile = MagicDeckProfile.getDeckProfile(MagicDeckProfile.ANY_ONE);
//            deckGenerator.setDeckProfile(deckProfile);
//            deckGenerator.setDeck(randomDeck);
//            generator.generateDeck(deckGenerator);
//            addBasicLandsToDeck(randomDeck, deckProfile, deckGenerator.deckSize);
////            setDeck(randomDeck);
//            return true;
//        } else {
//            return false;
//        }
//    }

//    /**
//     * Copied from MagicPlayerDefinition
//     */
//    private void addBasicLandsToDeck(final MagicDeck newDeck, final MagicDeckProfile deckProfile, final int DECK_SIZE) {
//        final int MIN_SOURCE = 16;
//        // Calculate statistics per color.
//        final int[] colorCount = new int[MagicColor.NR_COLORS];
//        final int[] colorSource = new int[MagicColor.NR_COLORS];
//        for (final MagicCardDefinition cardDefinition : newDeck) {
//            if (cardDefinition.isLand()) {
//                for (final MagicColor color : MagicColor.values()) {
//                    colorSource[color.ordinal()] += cardDefinition.getManaSource(color);
//                }
//            } else {
//                final int colorFlags = cardDefinition.getColorFlags();
//                for (final MagicColor color : deckProfile.getColors()) {
//                    if (color.hasColor(colorFlags)) {
//                        colorCount[color.ordinal()]++;
//                    }
//                }
//            }
//        }
//        // Add optimal basic lands to deck.
//        while (newDeck.size() < DECK_SIZE) {
//            MagicColor bestColor = null;
//            int lowestRatio = Integer.MAX_VALUE;
//            for (final MagicColor color : MagicColor.values()) {
//                final int index = color.ordinal();
//                final int count = colorCount[index];
//                if (count > 0) {
//                    final int source = colorSource[index];
//                    final int ratio;
//                    if (source < MIN_SOURCE) {
//                        ratio = source - count;
//                    } else {
//                        ratio = source * 100 / count;
//                    }
//                    if (ratio < lowestRatio) {
//                        lowestRatio = ratio;
//                        bestColor = color;
//                    }
//                }
//            }
//            // fix for issue 446 (http://code.google.com/p/magarena/issues/detail?id=446).
//            if (bestColor == null) {
//                bestColor = MagicColor.getColor(MagicColor.getRandomColors(1).charAt(0));
//            }
//            final MagicCardDefinition landCard = CardDefinitions.getBasicLand(bestColor);
//            colorSource[bestColor.ordinal()] += landCard.getManaSource(bestColor);
//            newDeck.add(landCard);
//        }
//    }

    public int getCardPoolSize() {
        return cardPoolPanel.getCardPool().size();
    }

    private AbstractAction getPlusButtonAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < actionBar.getQuantity(); i++) {
                    CardPoolTabPanel.this.firePropertyChange(CP_ADD_TO_DECK, false, true);
                }
            }
        };
    }

    private AbstractAction getMinusButtonAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < actionBar.getQuantity(); i++) {
                    CardPoolTabPanel.this.firePropertyChange(CP_REMOVE_FROM_DECK, false, true);
                }
            }
        };
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
