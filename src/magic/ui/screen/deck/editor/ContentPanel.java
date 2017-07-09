package magic.ui.screen.deck.editor;

import javax.swing.JPanel;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.model.MagicDeckConstructionRule;
import magic.translate.MText;
import magic.ui.ScreenController;
import magic.ui.widget.deck.stats.PwlWorker;
import magic.utility.MagicSystem;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ContentPanel extends JPanel {

    // translatable strings
    private static final String _S1 = "This deck is illegal.\n\n%s";

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();
    private final DeckEditorController controller = DeckEditorController.instance;

    private final MigLayout migLayout = new MigLayout();
    private final DeckSideBar sideBarPanel; // LHS
    private final MainViewsPanel viewsPanel; // RHS
    private final DeckEditorScreen screen;
    private boolean isStandalone = true;
    private PwlWorker pwlWorker;

    ContentPanel(DeckEditorScreen screen) {

        MagicSystem.waitForPlayableCards();

        this.screen = screen;

        // lhs
        sideBarPanel = new DeckSideBar();
        // rhs
        viewsPanel = new MainViewsPanel(this);
        //
        setLookAndFeel();
        refreshLayout();
    }

    private void setLookAndFeel() {
        setOpaque(false);
        setLayout(migLayout);
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("insets 0, gap 0");
        add(sideBarPanel, "h 100%");
        add(viewsPanel, "w 100%, h 100%");
    }

    void setCard(final MagicCardDefinition card) {
        final int cardCount = controller.getDeck().getCardCount(card);
        sideBarPanel.setCard(card);
        sideBarPanel.setCardCount(cardCount);
    }

    void doRefreshView() {
        viewsPanel.doRefreshViews();
    }

    MagicDeck getDeck() {
        return controller.getDeck();
    }

    private String getBrokenRules(final MagicDeck deck) {
        return MagicDeckConstructionRule.getRulesText(MagicDeckConstructionRule.checkDeck(deck));
    }

    private void notifyUser(final String brokenRules) {
        ScreenController.showWarningMessage(MText.get(_S1, brokenRules));
    }

    boolean validateDeck(final boolean notifyUser) {
        final String brokenRules = getBrokenRules(controller.getDeck());
        if (brokenRules.length() > 0) {
            if (notifyUser) {
                notifyUser(brokenRules);
            }
            return false;
        }
        return true;
    }

    void setIsStandalone(final boolean b) {
        this.isStandalone = b;
    }

    void deckUpdated(MagicDeck deck) {
        if (deck.isEmpty() && isStandalone) {
            CONFIG.setMostRecentDeckFilename("");
            CONFIG.save();
        }
        screen.deckUpdated(deck);
        doPWLStatsQuery(deck);
    }

    void cardSelected(MagicCardDefinition card) {
        setCard(card);
        sideBarPanel.setDeck(getDeck());
    }

    private void doPWLStatsQuery(MagicDeck deck) {
        pwlWorker = new PwlWorker(deck);
        pwlWorker.setListeners(sideBarPanel, viewsPanel);
        pwlWorker.execute();
    }

    void showDecksScreen() {
        screen.showDecksScreen();
    }

}
