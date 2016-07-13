package magic.ui.deck.editor;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.model.MagicDeckConstructionRule;
import magic.model.MagicRandom;
import magic.ui.CardFilterPanel;
import magic.ui.ICardFilterPanelListener;
import magic.ui.ScreenController;
import magic.ui.cardtable.CardTable;
import magic.ui.cardtable.ICardSelectionListener;
import magic.ui.screen.widget.ActionBar;
import magic.ui.screen.widget.StatusBar;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.utility.MagicSystem;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckEditorSplitPanel extends JPanel implements ICardSelectionListener, ICardFilterPanelListener {

    private static final int FILTERS_PANEL_HEIGHT = 88; // pixels
    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();

    private CardTable cardPoolTable;
    private CardTable deckTable;
    private CardFilterPanel filterPanel;
    private List<MagicCardDefinition> cardPoolDefs;
    private MagicDeck deckDefs;
    private MagicDeck deck;
    private MagicDeck originalDeck;
    private DeckEditorButtonsPanel buttonsPanel;
    private DeckSideBar sideBarPanel;
    private final MigLayout migLayout = new MigLayout();
    private JSplitPane cardsSplitPane;

    public DeckEditorSplitPanel(final MagicDeck deck) {
        setupExplorerPanel(deck);
    }

    private void setupExplorerPanel(final MagicDeck deck0) {

        MagicSystem.waitForAllCards();

        this.deck = new MagicDeck();
        this.originalDeck = deck0;
        if (deck0 != null) {
            // work with a copy of the original deck so it is easy to cancel updates.
            this.deck.setContent(deck0);
        }

        setOpaque(false);

        // create ui components.
        buttonsPanel = new DeckEditorButtonsPanel();
        sideBarPanel = new DeckSideBar();
        filterPanel = new CardFilterPanel(this);
        final Container cardsPanel = getMainContentContainer();

        final JPanel rhs = new JPanel();
        rhs.setLayout(new MigLayout("flowy, insets 0, gapy 0"));
        rhs.add(filterPanel, "w 100%, h " + FILTERS_PANEL_HEIGHT + "!");
        rhs.add(cardsPanel, "w 100%, h 100%");
        rhs.setOpaque(false);
        rhs.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK));

        migLayout.setLayoutConstraints("insets 0, gap 0");
        setLayout(migLayout);
        add(sideBarPanel, "h 100%");
        add(rhs, "w 100%, h 100%");

        // set initial card image
        if (cardPoolDefs.isEmpty()) {
            sideBarPanel.setCard(MagicCardDefinition.UNKNOWN);
        } else {
            final int index = MagicRandom.nextRNGInt(cardPoolDefs.size());
            sideBarPanel.setCard(cardPoolDefs.get(index));
        }

    }

    private Container getMainContentContainer() {

        // card pool
        cardPoolDefs = filterPanel.getFilteredCards();

        cardPoolTable = new CardTable(cardPoolDefs, generatePoolTitle(), false);
        cardPoolTable.addMouseListener(new CardPoolMouseListener());
        cardPoolTable.addCardSelectionListener(this);

        cardPoolTable.setDeckEditorSelectionMode();

        deckDefs = this.deck;
        deckTable = new CardTable(deckDefs, generateDeckTitle(deckDefs), true);
        deckTable.addMouseListener(new DeckMouseListener());
        deckTable.addCardSelectionListener(this);
        deckTable.setDeckEditorSelectionMode();
        deckTable.showCardCount(true);

        final JPanel deckPanel = new JPanel();
        deckPanel.setOpaque(false);
        deckPanel.setLayout(new MigLayout("insets 0, gap 0, flowy"));
        deckPanel.add(buttonsPanel, "w 100%, h 40!");
        deckPanel.add(deckTable, "w 100%, h 100%");

        cardsSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        cardsSplitPane.setOneTouchExpandable(false);
        cardsSplitPane.setLeftComponent(cardPoolTable);
        cardsSplitPane.setRightComponent(deckPanel);
        cardsSplitPane.setResizeWeight(0.5);
        cardsSplitPane.setDividerSize(14);
        cardsSplitPane.setBorder(null);
        cardsSplitPane.setOpaque(false);
        cardsSplitPane.setDividerLocation(getDividerPosition());

        // update deck stats
        sideBarPanel.setDeck(this.deck);

        return cardsSplitPane;

    }

    private int getDividerPosition() {
        final int splitPaneContentHeight =
               ScreenController.getMainFrame().getContentPane().getHeight() -
               StatusBar.PANEL_HEIGHT -
               ActionBar.PANEL_HEIGHT -
               FILTERS_PANEL_HEIGHT -
               (cardsSplitPane.getDividerSize() / 2);
        return (int)(splitPaneContentHeight * 0.46);
    }

    private String generatePoolTitle() {
        final StringBuffer sb = new StringBuffer();
        final int total = filterPanel.getTotalCardCount();
        sb.append("Cards: ").append(NumberFormat.getInstance().format(total));
        return sb.toString();
    }

    @Override
    public boolean isDeckEditor() {
        return true;
    }

    private String generateDeckTitle(final MagicDeck deck) {
        return "Deck (" + deck.getName() + ") - " + deck.size() + " cards";
    }

    public void updateCardPool() {
        cardPoolDefs = filterPanel.getFilteredCards();
        cardPoolTable.setCards(cardPoolDefs);
        cardPoolTable.setTitle(generatePoolTitle());
    }

    public void updateDeck() {
        deckDefs = this.deck;
        deckTable.setTitle(generateDeckTitle(deckDefs));
        deckTable.setCards(deckDefs);
        sideBarPanel.setDeck(deckDefs);
        validate();
    }

    private void removeSelectedFromDeck() {
        final List<MagicCardDefinition> deckCards = deckTable.getSelectedCards();

        if (deckCards.size() > 0) {
            for (final MagicCardDefinition card : deckCards) {
                this.deck.remove(card);
            }
            updateDeck();

        } else {
            ScreenController.showWarningMessage("Please select a valid card in the deck to remove it.");
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
            ScreenController.showWarningMessage("Please select a valid card in the card pool to add it to the deck.");
        }
    }

    protected void close() {
        filterPanel.closePopups();
    }

    @Override
    public void newCardSelected(final MagicCardDefinition card) {
        SwingUtilities.invokeLater(() -> {
            if (card != null) {
                sideBarPanel.setCard(card);
            }
        });
    }

    public MagicCardDefinition getSelectedCard() {
        if (cardPoolTable.getSelectedCards().size() == 1) {
            return cardPoolTable.getSelectedCards().get(0);
        } else {
            return null;
        }
    }

    @Override
    public void refreshTable() {
        updateCardPool();
    }

    private class CardPoolMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(final MouseEvent e) {
            // double-click actions.
            if (e.getClickCount() > 1) {
                addSelectedToDeck();
            }
        }
    }

    public void showCardScriptScreen() {
        if (cardPoolTable.getSelectedCards().size() == 1) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            ScreenController.showCardScriptScreen(cardPoolTable.getSelectedCards().get(0));
            setCursor (Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private class DeckMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(final MouseEvent e) {
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
                final JTable table = (JTable) (e.getSource());
                final int row = table.rowAtPoint(e.getPoint());
                table.clearSelection();
                table.addRowSelectionInterval(row, row);
            }
        }
    }

    public void setDeck(final MagicDeck deck0) {
        if (deck0 != null) {
            this.deck = deck0;
            deckDefs = this.deck;
            deckTable.setTitle(generateDeckTitle(deckDefs));
            deckTable.setCards(deckDefs);
            sideBarPanel.setDeck(deckDefs);
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
        ScreenController.showWarningMessage("This deck is illegal.\n\n" + brokenRules);
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
        return !isUpdatingExistingDeck();
    }

    private boolean isUpdatingExistingDeck() {
        return this.originalDeck != null;
    }

    private class DeckEditorButtonsPanel extends TexturedPanel implements ActionListener {

        private final MigLayout migLayout = new MigLayout();
        private final JButton addButton = new JButton();
        private final JButton removeButton = new JButton();
        private final JButton clearButton = new JButton();

        public DeckEditorButtonsPanel() {
            setLookAndFeel();
            refreshLayout();
            setActions();
        }

        private void setLookAndFeel() {
            setLayout(migLayout);
            setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);
            // JButton that adds card(s) from card pool to deck.
            addButton.setText("Add to Deck");
            addButton.setFont(FontsAndBorders.FONT1);
            addButton.setFocusable(false);
            // JButton that removes card(s) from deck.
            removeButton.setText("Remove from Deck");
            removeButton.setFont(FontsAndBorders.FONT1);
            removeButton.setFocusable(false);
            // JButton that removes all cards from deck.
            clearButton.setText("Clear Deck");
            clearButton.setFont(FontsAndBorders.FONT1);
            clearButton.setFocusable(false);
        }

        private void refreshLayout() {
            removeAll();
            migLayout.setLayoutConstraints("insets 0 6 0 0, aligny center");
            add(addButton, "w 160!, h 30!");
            add(removeButton, "w 160!, h 30!");
            add(clearButton, "w 160!, h 30!");
        }

        private void setActions() {
            removeButton.addActionListener(this);
            addButton.addActionListener(this);
            clearButton.addActionListener(this);
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            final Object source=event.getSource();
            if (source == addButton) {
                addSelectedToDeck();
            } else if (source == removeButton) {
                removeSelectedFromDeck();
            } else if (source == clearButton) {
                doClearDeck();
            }
        }

        private void doClearDeck() {
            final int userResponse =
                    JOptionPane.showConfirmDialog(
                            ScreenController.getMainFrame(),
                            "Remove all cards from deck?",
                            "Clear Deck",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
            if (userResponse == JOptionPane.YES_OPTION) {
                setDeck(new MagicDeck());
                if (isStandaloneDeckEditor()) {
                    CONFIG.setMostRecentDeckFilename("");
                    CONFIG.save();
                }
            }
        }

    }

}
