package magic.ui.screen.duel.decks;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import magic.data.DeckType;
import magic.data.DuelConfig;
import magic.data.MagicIcon;
import magic.exception.InvalidDeckException;
import magic.model.DuelPlayerConfig;
import magic.model.MagicDeck;
import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.player.IPlayerProfileListener;
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;
import magic.translate.MText;
import magic.ui.FontsAndBorders;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.ui.screen.deck.editor.DeckSideBar;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuButton;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.cards.table.CardTablePanelB;
import magic.ui.widget.deck.stats.IPwlWorkerListener;
import magic.ui.widget.deck.stats.PwlWorker;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class DuelDecksPanel extends TexturedPanel
    implements IPlayerProfileListener, IPwlWorkerListener {

    // translatable strings
    private static final String _S1 = "UNSAVED";
    private static final String _S7 = "Swap Decks";
    private static final String _S8 = "Swap your deck with your opponent's.";
    private static final String _S15 = "Generate another deck";
    private static final String _S16 = "Based on the duel settings, randomly selects an existing prebuilt deck or<br>generates a random deck for the selected player.";

    // change properties
    static final String CP_DECK_CHANGED = "1fe41854-83e4-4a98-9c4b-46ca9f4c9550";

    private final MigLayout migLayout = new MigLayout();
    private final MagicDuel duel;
    private final JTabbedPane tabbedPane;
    private final CardTablePanelB[] cardTables;
    private final DeckSideBar sidebar;
    private final ActionBarButton newDeckButton;
    private boolean isTabChanged = false;
    private PwlWorker pwlWorker;

    DuelDecksPanel(final MagicDuel duel) {

        this.duel = duel;
        newDeckButton = getNewDeckActionBarButton();

        setBackground(FontsAndBorders.MAGSCREEN_FADE_COLOR);

        sidebar = new DeckSideBar();

        // create tabs for each player
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);

        final DuelPlayerConfig[] players = duel.getPlayers();
        cardTables = new CardTablePanelB[players.length];

        for (int i = 0; i < players.length; i++) {

            final DuelPlayerConfig player = players[i];

            cardTables[i] = new CardTablePanelB(player.getDeck(), generateTitle(player.getDeck()), true);
            cardTables[i].addCardSelectionListener(sidebar.getCardViewer());
            cardTables[i].showCardCount(true);
            tabbedPane.addTab(null, cardTables[i]);

            final DuelConfig duelConfig = duel.getConfiguration();
            final PlayerProfile profile = duelConfig.getPlayerProfile(i);
            tabbedPane.setTabComponentAt(i, new PlayerPanel(profile, duel));
        }

        tabbedPane.addChangeListener((ChangeEvent e) -> {
            int newIndex = tabbedPane.getSelectedIndex();
            int oldIndex = newIndex == 0 ? 1 : 0;
            ((PlayerPanel) tabbedPane.getTabComponentAt(newIndex)).setSelected(true);
            ((PlayerPanel) tabbedPane.getTabComponentAt(oldIndex)).setSelected(false);
            setDeck();
            isTabChanged = true;
        });

        tabbedPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (!isTabChanged && duel.getGamesPlayed() == 0) {
                    doSelectPlayer();
                }
                isTabChanged = false;
            }
        });

        tabbedPane.setPreferredSize(new Dimension(800, 0));

        // layout screen components.
        migLayout.setLayoutConstraints("insets 0, gap 0");
        setLayout(migLayout);
        add(sidebar, "h 100%");
        add(tabbedPane, "h 100%, w 100%");

        ((PlayerPanel) tabbedPane.getTabComponentAt(1)).setSelected(false);

        setDeck();
    }

    private void doSelectPlayer() {
        PlayerProfile playerProfile = getSelectedPlayer().getProfile();
        if (playerProfile.isHuman()) {
            ScreenController.showSelectHumanPlayerScreen(this, playerProfile);
        } else {
            ScreenController.showSelectAiProfileScreen(this, playerProfile);
        }
    }

    private void setDeck() {
        DuelPlayerConfig player = getSelectedPlayer();
        MagicDeck deck = player.getDeck();
        sidebar.setDeck(deck);
        doPWLStatsQuery(deck);
        sidebar.setCard(deck.get(0));
        boolean isRandomDeck = player.getDeckProfile().getDeckType() == DeckType.Random;
        newDeckButton.setEnabled(isRandomDeck && duel.getGamesPlayed() == 0);
    }

    void setDeck(MagicDeck newDeck) {
        getSelectedPlayer().setDeck(newDeck);
        setDeck();
        updateDecksAfterEdit();
    }

    private String generateTitle(final MagicDeck deck) {
        return deck.isUnsaved()
            ? MText.get(_S1) + "  /  " + deck.getName()
            : deck.getQualifiedName();
    }

    MagicDuel getDuel() {
        return duel;
    }

    DuelPlayerConfig getSelectedPlayer() {
        return duel.getPlayers()[tabbedPane.getSelectedIndex()];
    }

    void updateDecksAfterEdit() {
        for (int i = 0; i < duel.getPlayers().length; i++) {
            final DuelPlayerConfig player = duel.getPlayers()[i];
            final MagicDeck deck = player.getDeck();
            cardTables[i].setCards(deck);
            cardTables[i].setTitle(generateTitle(deck));
        }
        setDeck();
        firePropertyChange(CP_DECK_CHANGED, true, false);
    }

    private void generateDeck() {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            duel.buildDeck(getSelectedPlayer());
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } catch (InvalidDeckException ex) {
            ScreenController.showWarningMessage(ex.getMessage());
        }
        updateDecksAfterEdit();
    }

    private ActionBarButton getNewDeckActionBarButton() {
        return new ActionBarButton(
            MagicImages.getIcon(MagicIcon.RANDOM),
            MText.get(_S15),
            MText.get(_S16),
            new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    generateDeck();
                }
            }
        );
    }

    private void swapDecks() {
        duel.restart();
        final DuelPlayerConfig[] players = duel.getPlayers();
        final MagicDeckProfile deckProfile1 = players[0].getDeckProfile();
        final MagicDeckProfile deckProfile2 = players[1].getDeckProfile();
        final MagicDeck deck1 = new MagicDeck(players[0].getDeck());
        final MagicDeck deck2 = new MagicDeck(players[1].getDeck());
        players[0].setDeckProfile(deckProfile2);
        players[0].setDeck(deck2);
        players[1].setDeckProfile(deckProfile1);
        players[1].setDeck(deck1);
        ScreenController.closeActiveScreen(false);
        ScreenController.getFrame().showDuel();
    }

    private ActionBarButton getSwapDecksButton() {
        return new ActionBarButton(
            MagicImages.getIcon(MagicIcon.SWAP),
            MText.get(_S7), MText.get(_S8),
            new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    try {
                        swapDecks();
                    } catch (InvalidDeckException ex) {
                        ScreenController.showWarningMessage(ex.getMessage());
                    }
                }
            }
        );
    }

    MenuButton[] getActionBarButtons() {
        final List<MenuButton> buttons = new ArrayList<>();
        buttons.add(newDeckButton);
        buttons.add(getSwapDecksButton());
        return buttons.toArray(new MenuButton[buttons.size()]);
    }

    @Override
    public void playerProfileUpdated(PlayerProfile player) {
        tabbedPane.setTabComponentAt(
            tabbedPane.getSelectedIndex(),
            new PlayerPanel(player, duel)
        );
    }

    @Override
    public void PlayerProfileDeleted(PlayerProfile deletedPlayer) {
        PlayerProfile newPlayer = deletedPlayer.isHuman()
            ? PlayerProfiles.getDefaultHumanPlayer()
            : PlayerProfiles.getDefaultAiPlayer();
        duel.getConfiguration().setPlayerProfile(deletedPlayer.isHuman() ? 0 : 1, newPlayer);
        tabbedPane.setTabComponentAt(
            tabbedPane.getSelectedIndex(),
            new PlayerPanel(newPlayer, duel)
        );
    }

    @Override
    public void PlayerProfileSelected(PlayerProfile player) {
        saveSelectedPlayerProfile(player);
        tabbedPane.setTabComponentAt(
            tabbedPane.getSelectedIndex(),
            new PlayerPanel(player, duel)
        );
    }

    private void saveSelectedPlayerProfile(final PlayerProfile player) {
        duel.getConfiguration().setPlayerProfile(player.isHuman() ? 0 : 1, player);
    }

    void refreshLayout() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void setCardsTableStyle() {
        cardTables[0].setStyle();
        cardTables[1].setStyle();
    }

    @Override
    public void setPlayedWonLost(String pwl) {
        sidebar.setPlayedWonLost(pwl);
    }

    private void doPWLStatsQuery(MagicDeck deck) {
        pwlWorker = new PwlWorker(deck);
        pwlWorker.setListeners(sidebar);
        pwlWorker.execute();
    }
}
