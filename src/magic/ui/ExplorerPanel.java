package magic.ui;

import magic.MagicMain;
import magic.data.CardImagesProvider;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.model.MagicDeckConstructionRule;
import magic.model.MagicRandom;
import magic.ui.viewer.CardViewer;
import magic.ui.viewer.DeckStatisticsViewer;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;


public class ExplorerPanel extends TexturedPanel {

    private static final long serialVersionUID = 1L;

    private static final String ADD_BUTTON_TEXT = "Add";
    private static final String REMOVE_BUTTON_TEXT = "Remove";
    private static final String CARD_POOL_TITLE = "Card Pool";
    private static final int SPACING=10;

    private CardTable cardPoolTable;
    private CardTable deckTable;
    private ExplorerFilterPanel filterPanel;
    private JButton addButton;
    private JButton removeButton;
    private List<MagicCardDefinition> cardPoolDefs;
    private MagicDeck deckDefs;
    private final boolean isDeckEditor;
    private MagicDeck deck;
    private MagicDeck originalDeck;
    private DeckEditorButtonsPanel buttonsPanel;
    private SideBarPanel sideBarPanel;

    // CTR - Card Explorer
    public ExplorerPanel() {
        this.isDeckEditor = false;
        setupExplorerPanel(null);
    }
    // CTR - Deck Editor
    public ExplorerPanel(final MagicDeck deck) {
        this.isDeckEditor = true;
        setupExplorerPanel(deck);
    }

    private void setupExplorerPanel(final MagicDeck deck0) {

        this.deck = new MagicDeck();
        this.originalDeck = deck0;
        if (deck0 != null) {
            // work with a copy of the original deck so it is easy to cancel updates.
            this.deck.setContent(deck0);
        }

        setBackground(FontsAndBorders.MAGSCREEN_FADE_COLOR);

        final SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);

        buttonsPanel = new DeckEditorButtonsPanel(isDeckEditor());
        add(buttonsPanel);

        sideBarPanel = new SideBarPanel(isDeckEditor());
        add(sideBarPanel);

        filterPanel = new ExplorerFilterPanel(this);
        add(filterPanel);

        // card pool
        cardPoolDefs = filterPanel.getCardDefinitions();

        // deck
        final Container cardsPanel; // reference panel holding both card pool and deck

        cardPoolTable = new CardTable(cardPoolDefs, sideBarPanel.getCardViewer(), generatePoolTitle(), false);

        if (isDeckEditor()) {

            cardPoolTable.addMouseListener(new CardPoolMouseListener());
            cardPoolTable.setDeckEditorSelectionMode();

            deckDefs = this.deck;
            deckTable = new CardTable(deckDefs, sideBarPanel.getCardViewer(), generateDeckTitle(deckDefs), true);
            deckTable.addMouseListener(new DeckMouseListener());
            deckTable.setDeckEditorSelectionMode();

            final JSplitPane cardsSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            cardsSplitPane.setOneTouchExpandable(true);
            cardsSplitPane.setLeftComponent(cardPoolTable);
            cardsSplitPane.setRightComponent(deckTable);
            cardsSplitPane.setResizeWeight(0.5);

            add(cardsSplitPane);
            cardsPanel = cardsSplitPane;

            // update deck stats
            sideBarPanel.getStatsViewer().setDeck(this.deck);

        } else {
            // no deck
            deckDefs = null;
            deckTable = null;

            add(cardPoolTable);
            cardsPanel = cardPoolTable;
        }

        // set sizes by defining gaps between components
        final Container contentPane = this;

        // card image's gap (top left)
        springLayout.putConstraint(SpringLayout.WEST, sideBarPanel,
                             SPACING, SpringLayout.WEST, contentPane);
        springLayout.putConstraint(SpringLayout.NORTH, sideBarPanel,
                             SPACING, SpringLayout.NORTH, contentPane);

        // gap between card image and filter
        springLayout.putConstraint(SpringLayout.WEST, filterPanel,
                             SPACING, SpringLayout.EAST, sideBarPanel);

        // filter panel's gaps (top right)
        springLayout.putConstraint(SpringLayout.NORTH, filterPanel,
                             0, SpringLayout.NORTH, sideBarPanel);
        springLayout.putConstraint(SpringLayout.EAST, filterPanel,
                             -SPACING, SpringLayout.EAST, contentPane);

        // filter panel's gap with card tables
        springLayout.putConstraint(SpringLayout.WEST, cardsPanel,
                             0, SpringLayout.WEST, filterPanel);
        springLayout.putConstraint(SpringLayout.NORTH, cardsPanel,
                             SPACING, SpringLayout.SOUTH, filterPanel);

        // card tables' gap (right)
        springLayout.putConstraint(SpringLayout.EAST, cardsPanel,
                             -SPACING, SpringLayout.EAST, contentPane);
        springLayout.putConstraint(SpringLayout.SOUTH, cardsPanel,
                                 -SPACING, SpringLayout.SOUTH, contentPane);

        // buttons' gap (top right bottom)
        springLayout.putConstraint(SpringLayout.EAST, buttonsPanel,
                             0, SpringLayout.EAST, sideBarPanel);
        springLayout.putConstraint(SpringLayout.SOUTH, sideBarPanel,
                             -SPACING, SpringLayout.NORTH, buttonsPanel);
        springLayout.putConstraint(SpringLayout.SOUTH, buttonsPanel,
                             -SPACING, SpringLayout.SOUTH, contentPane);

        // set initial card image
        if (cardPoolDefs.isEmpty()) {
            sideBarPanel.getCardViewer().setCard(MagicCardDefinition.UNKNOWN,0);
         } else {
             final int index = MagicRandom.nextRNGInt(cardPoolDefs.size());
             sideBarPanel.getCardViewer().setCard(cardPoolDefs.get(index),0);
         }

    }

     private String generatePoolTitle() {
         return CARD_POOL_TITLE + " - " + cardPoolDefs.size() + " cards";
     }

    public boolean isDeckEditor() {
        return this.isDeckEditor;
    }

    private String generateDeckTitle(final MagicDeck deck) {
        return "Deck (" + deck.getName() + ") - " + deck.size() + " cards";
    }

    public void updateCardPool() {
        cardPoolDefs = filterPanel.getCardDefinitions();
        cardPoolTable.setCards(cardPoolDefs);
        cardPoolTable.setTitle(generatePoolTitle());
    }

    public void updateDeck() {
        if (isDeckEditor()) {
            deckDefs = this.deck;
            deckTable.setTitle(generateDeckTitle(deckDefs));
            deckTable.setCards(deckDefs);
            sideBarPanel.getStatsViewer().setDeck(deckDefs);
            validate();
        }
    }

    private void removeSelectedFromDeck() {
        final List<MagicCardDefinition> deckCards = deckTable.getSelectedCards();

        if (deckCards.size() > 0) {
            for (final MagicCardDefinition card : deckCards) {
                this.deck.remove(card);
            }

            updateDeck();

        } else {
            // display error
            JOptionPane.showMessageDialog(MagicMain.rootFrame, "Select a valid card in the deck to remove it.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addSelectedToDeck() {
        final List<MagicCardDefinition> cardPoolCards = cardPoolTable.getSelectedCards();

        if (cardPoolCards.size() > 0) {
            for (final MagicCardDefinition card : cardPoolCards) {
                this.deck.add(card);
            }

            updateDeck();

        } else {
            // display error
            JOptionPane.showMessageDialog(MagicMain.rootFrame, "Select a valid card in the card pool to add it to the deck.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void close() {
        filterPanel.closePopups();
    }

    //protected abstract void closeScreen();

    private class CardPoolMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(final MouseEvent e) {
            if (isDeckEditor() && e.getClickCount() > 1) {
                addSelectedToDeck();
            }
        }
    }

    private class DeckMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(final MouseEvent e) {
            if (isDeckEditor()) {
                if (e.getClickCount() > 1) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        removeSelectedFromDeck();
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        final List<MagicCardDefinition> deckCards = deckTable.getSelectedCards();
                        if (deckCards.size() > 0) {
                            for (final MagicCardDefinition card : deckCards) {
                                deck.add(card);
                            }

                            updateDeck();

                        }
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    final JTable table = (JTable)(e.getSource());
                    final int row = table.rowAtPoint(e.getPoint());
                    table.clearSelection();
                    table.addRowSelectionInterval(row,row);
                }
            }
        }
    }

    public void setDeck(final MagicDeck deck0) {
        if (deck0 != null) {
            this.deck = deck0;
            if (isDeckEditor()) {
                deckDefs = this.deck;
                deckTable.setTitle(generateDeckTitle(deckDefs));
                deckTable.setCards(deckDefs);
                sideBarPanel.getStatsViewer().setDeck(deckDefs);
            }
        }
    }

    public MagicDeck getDeck() {
        return this.deck;
    }

    public boolean applyDeckUpdates() {
        boolean updatesApplied = true;
        if (isUpdatingExistingDeck()) {
            if (validateDeck(false)) {
                this.originalDeck.setContent(this.deck);
            } else {
                updatesApplied = false;
            }
        }
        return updatesApplied;
    }

    private String getBrokenRules(final MagicDeck deck) {
        return MagicDeckConstructionRule.getRulesText(MagicDeckConstructionRule.checkDeck(deck));
    }

    private void notifyUser(final String brokenRules) {
        JOptionPane.showMessageDialog(
                this,
                "This deck is illegal.\n\n" + brokenRules,
                "Illegal Deck",
                JOptionPane.WARNING_MESSAGE);
    }

    public boolean validateDeck(final boolean notifyUser) {
        final String brokenRules = getBrokenRules(deck);
        if (brokenRules.length() > 0) {
            if (notifyUser) {
                notifyUser(brokenRules);
            }
            return false;
        }
        return true;
    }

    public boolean isStandaloneDeckEditor() {
        return isDeckEditor() && !isUpdatingExistingDeck();
    }

    private boolean isUpdatingExistingDeck() {
        return this.originalDeck != null;
    }

    @SuppressWarnings("serial")
    private class DeckEditorButtonsPanel extends JPanel implements ActionListener {

        private final boolean isDeckEditor;

        public DeckEditorButtonsPanel(final boolean isDeckEditor) {

            this.isDeckEditor = isDeckEditor;

            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            setOpaque(false);

            if (isDeckEditor) {
                addButton = new JButton(ADD_BUTTON_TEXT);
                addButton.setFont(FontsAndBorders.FONT1);
                addButton.setFocusable(false);
                addButton.addActionListener(this);
                add(addButton);

                add(Box.createHorizontalStrut(SPACING));

                removeButton = new JButton(REMOVE_BUTTON_TEXT);
                removeButton.setFont(FontsAndBorders.FONT1);
                removeButton.setFocusable(false);
                removeButton.addActionListener(this);
                add(removeButton);

                add(Box.createHorizontalStrut(SPACING));

            } else {
                addButton = null;
                removeButton = null;
            }

        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            final Object source=event.getSource();
            if (isDeckEditor) {
                if (source == addButton) {
                    addSelectedToDeck();
                } else if (source == removeButton) {
                    removeSelectedFromDeck();
                }
            }
        }

    }

    @SuppressWarnings("serial")
    private class SideBarPanel extends JPanel {

        private final MigLayout migLayout = new MigLayout();
        private final JScrollPane cardScrollPane = new JScrollPane();
        private final CardViewer cardViewer = new CardViewer(false,true);
        private final DeckStatisticsViewer statsViewer;

        private final boolean isDeckEditorMode;

        private SideBarPanel(final boolean isDeckEditorMode) {
            this.isDeckEditorMode = isDeckEditorMode;
            statsViewer = isDeckEditorMode ? new DeckStatisticsViewer() : null;
            setLookAndFeel();
            refreshLayout();
        }

        private void setLookAndFeel() {
            setLayout(migLayout);
            setOpaque(false);
            // card image viewer
            cardViewer.setPreferredSize(CardImagesProvider.CARD_DIMENSION);
            cardViewer.setMaximumSize(CardImagesProvider.CARD_DIMENSION);
            // card image scroll pane
            cardScrollPane.setBorder(FontsAndBorders.NO_BORDER);
            cardScrollPane.setOpaque(false);
            cardScrollPane.getViewport().setOpaque(false);
            cardScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            cardScrollPane.getVerticalScrollBar().setUnitIncrement(10);
            // stats viewer
            if (statsViewer != null) {
                statsViewer.setMaximumSize(DeckStatisticsViewer.PREFERRED_SIZE);
            }
        }

        private void refreshLayout() {
            removeAll();
            migLayout.setLayoutConstraints("flowy, insets 0");
            cardScrollPane.setViewportView(cardViewer);
            add(cardScrollPane);
            if (isDeckEditorMode) {
                add(statsViewer);
            }
        }

        public CardViewer getCardViewer() {
            return cardViewer;
        }

        public DeckStatisticsViewer getStatsViewer() {
            return statsViewer;
        }

    }

}
