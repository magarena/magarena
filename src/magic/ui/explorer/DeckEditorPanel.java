package magic.ui.explorer;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.model.MagicDeckConstructionRule;
import magic.ui.utility.GraphicsUtils;
import magic.ui.ScreenController;
import magic.utility.MagicSystem;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckEditorPanel extends JPanel {

    // fired when contents of deck list are updated.
    public static final String CP_DECKLIST= DeckEditorTabbedPanel.CP_DECKLIST;
        // fired when deck is cleared.
    public static final String CP_DECK_CLEARED = DeckEditorTabbedPanel.CP_DECK_CLEARED;

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();

    private final MigLayout migLayout = new MigLayout();
    private final DeckEditorSideBarPanel sideBarPanel; // LHS
    private final DeckEditorTabbedPanel tabbedPanel; // RHS

    public DeckEditorPanel(final MagicDeck deck) {
        
        MagicSystem.waitForAllCards();

        // lhs
        sideBarPanel = new DeckEditorSideBarPanel();
        // rhs
        tabbedPanel = new DeckEditorTabbedPanel(deck);
        tabbedPanel.addPropertyChangeListener(
                DeckEditorTabbedPanel.CP_DECK_CLEARED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        firePropertyChange(CP_DECK_CLEARED, false, true);
                    }
                });
        tabbedPanel.addPropertyChangeListener(
                DeckEditorTabbedPanel.CP_CARD_SELECTED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        setSelectedCard();
                    }
                });
        tabbedPanel.addPropertyChangeListener(
                DeckEditorTabbedPanel.CP_DECKLIST,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        firePropertyChange(CP_DECKLIST, false, true);
                    }
                });
        setSelectedCard();
        //
        setLookAndFeel();
        refreshLayout();
    }

    private void setLookAndFeel() {
        setOpaque(false);
        setLayout(migLayout);
    }

    private void refreshLayout() {
        final Dimension imageSize = GraphicsUtils.getMaxCardImageSize();
        removeAll();
        migLayout.setLayoutConstraints("insets 0, gap 0");
        if (CONFIG.isHighQuality()) {
            migLayout.setColumnConstraints("[][grow]");
            add(sideBarPanel, "h 100%, w 0:" + imageSize.width + ":" + imageSize.width);
            add(tabbedPanel, "h 100%, growx");
        } else {
            migLayout.setColumnConstraints("[" + imageSize.width + "!][100%]");
            add(sideBarPanel, "h 100%");
            add(tabbedPanel, "w 100%, h 100%");
        }
    }

    protected void close() {
//        filterPanel.closePopups();
    }

//    @Override
    public void setCard(final MagicCardDefinition card) {
        final int cardCount = tabbedPanel.getDeck().getCardCount(card);
        sideBarPanel.setCard(card);
        sideBarPanel.setCardCount(cardCount);
    }

    private void setSelectedCard() {
        setCard(tabbedPanel.getSelectedCard());
        sideBarPanel.getStatsViewer().setDeck(tabbedPanel.getDeck());
    }

    public void setDeck(final MagicDeck deck) {
        tabbedPanel.setDeck(deck);
    }

    public MagicDeck getDeck() {
        return tabbedPanel.getDeck();
    }

    public boolean applyDeckUpdates() {
        boolean updatesApplied = true;
        if (isUpdatingExistingDeck()) {
            if (validateDeck(false)) {
                tabbedPanel.updateOriginalDeck();
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
        final String brokenRules = getBrokenRules(tabbedPanel.getDeck());
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
        return tabbedPanel.isUpdatingExistingDeck();
    }

}
