package magic.ui.screen.duel.setup;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Path;
import javax.swing.JPanel;
import magic.data.DeckType;
import magic.utility.DeckUtils;
import magic.data.DuelConfig;
import magic.data.MagicFormat;
import magic.exception.InvalidDeckException;
import magic.model.MagicDeck;
import magic.model.player.IPlayerProfileListener;
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;
import magic.ui.ScreenController;
import magic.translate.MText;
import magic.ui.screen.widget.DuelSettingsPanel;
import magic.ui.screen.widget.MenuButton;
import magic.ui.utility.MagicStyle;
import magic.ui.WikiPage;
import magic.ui.helpers.MouseHelper;
import magic.ui.screen.HeaderFooterScreen;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class NewDuelSettingsScreen extends HeaderFooterScreen {

    // translatable strings
    private static final String _S1 = "New Duel Settings";
    private static final String _S2 = "Cancel";
    private static final String _S3 = "Next";
    private static final String _S4 = "Invalid Deck\n%s";
    private static final String _S5 = "%s's deck is invalid.";
    private static final String _S6 = "The following player decks are invalid :-\n\n";

    private static final int PLAYERS_COUNT = 2;
    private static final DuelConfig duelConfig = DuelConfig.getInstance();

    private final ScreenContent content;

    public NewDuelSettingsScreen() {
        super(MText.get(_S1));
        duelConfig.load();
        content = new ScreenContent(duelConfig);
        setMainContent(content);
        setLeftFooter(MenuButton.getCloseScreenButton(MText.get(_S2)));
        setRightFooter(MenuButton.build(this::doNextAction, MText.get(_S3)));
        setWikiPage(WikiPage.NEW_DUEL);
    }

    private void doNextAction() {
        if (isEachPlayerDeckValid(true)) {
            updateDuelConfig();
            ScreenController.closeActiveScreen(false);
            try {
                ScreenController.getFrame().newDuel(duelConfig);
            } catch (InvalidDeckException ex) {
                ScreenController.showWarningMessage(MText.get(_S4, ex.getMessage()));
            }
        }
    }

    private boolean isEachPlayerDeckValid(final boolean showErrorDialog) {
        boolean isEachDeckValid = true;
        final StringBuffer sb = new StringBuffer();
        if (!content.isDeckValid(0)) {
            sb.append(MText.get(_S5, content.getPlayerProfile(0).getPlayerName())).append("\n");
            isEachDeckValid = false;
        }
        if (!content.isDeckValid(1)) {
            sb.append(MText.get(_S5, content.getPlayerProfile(1).getPlayerName()));
            isEachDeckValid = false;
        }
        if (!isEachDeckValid && showErrorDialog) {
            sb.insert(0, MText.get(_S6));
            ScreenController.showWarningMessage(sb.toString());
        }
        return isEachDeckValid;
    }

    private void updateDuelConfig() {
        duelConfig.setStartLife(content.getStartLife());
        duelConfig.setHandSize(content.getHandSize());
        duelConfig.setNrOfGames(content.getNrOfGames());
        duelConfig.setCube(content.getCube());
        for (int i = 0; i < PLAYERS_COUNT; i++) {
            duelConfig.setPlayerProfile(i, content.getPlayerProfile(i));
            duelConfig.setPlayerDeckProfile(i, content.getDeckType(i), content.getDeckValue(i));
        }
    }

    @Override
    public boolean isScreenReadyToClose(final Object nextScreen) {
        if (isEachPlayerDeckValid(false)) {
            updateDuelConfig();
        }
        return true;
    }

    private class ScreenContent extends JPanel implements IPlayerProfileListener {

        private final MigLayout migLayout = new MigLayout();
        private final DuelSettingsPanel duelSettingsPanel;
        private final DuelPlayerPanel[] playerPanels = new DuelPlayerPanel[PLAYERS_COUNT];
        private final DuelPlayerDeckPanel[] newPlayerDeckPanels = new DuelPlayerDeckPanel[PLAYERS_COUNT];

        public ScreenContent(final DuelConfig config) {
            this.duelSettingsPanel = new DuelSettingsPanel(config);
            for (int i = 0; i < PLAYERS_COUNT; i++) {
                this.playerPanels[i] = getNewDuelPlayerPanel(config.getPlayerProfile(i));
                this.newPlayerDeckPanels[i] = new DuelPlayerDeckPanel(config.getPlayerDeckProfile(i));
            }
            setLookAndFeel();
            refreshLayout();
        }

        private void setLookAndFeel() {
            setOpaque(false);
            setLayout(migLayout);
        }

        private void refreshLayout() {
            migLayout.setLayoutConstraints("insets 0, center, center, wrap 2");
            migLayout.setColumnConstraints("[310, fill]6[310, fill]");
            migLayout.setRowConstraints("[40, fill]6[270, fill]4[60, fill]");
            add(duelSettingsPanel, "spanx 2");
            add(playerPanels[0]);
            add(playerPanels[1]);
            add(newPlayerDeckPanels[0]);
            add(newPlayerDeckPanels[1]);
        }

        private DuelPlayerPanel getNewDuelPlayerPanel(final PlayerProfile player) {
            final DuelPlayerPanel panel = new DuelPlayerPanel(player);
            panel.addMouseListener(getDuelPlayerPanelMouseAdapter(panel));
            return panel;
        }

        private MouseAdapter getDuelPlayerPanelMouseAdapter(final DuelPlayerPanel panel) {
            return new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    MouseHelper.showBusyCursor();
                    selectNewProfile(panel.getPlayer());
                    mouseExited(e);
                    MouseHelper.showDefaultCursor();
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    MagicStyle.setHightlight(panel, true);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    MagicStyle.setHightlight(panel, false);
                }
            };
        }

        private void selectNewProfile(final PlayerProfile playerProfile) {
            if (playerProfile.isHuman()) {
                ScreenController.showSelectHumanPlayerScreen(this, playerProfile);
            } else {
                ScreenController.showSelectAiProfileScreen(this, playerProfile);
            }
        }

        public MagicFormat getCube() {
            return duelSettingsPanel.getCube();
        }

        public int getNrOfGames() {
            return duelSettingsPanel.getNrOfGames();
        }

        public int getHandSize() {
            return duelSettingsPanel.getHandSize();
        }

        public int getStartLife() {
            return duelSettingsPanel.getStartLife();
        }

        public String getDeckValue(final int playerIndex) {
            return newPlayerDeckPanels[playerIndex].getDeckValue();
        }
        public DeckType getDeckType(final int playerIndex) {
            return newPlayerDeckPanels[playerIndex].getDeckType();
        }

        public PlayerProfile getPlayerProfile(final int index) {
            return playerPanels[index].getPlayer();
        }

        private DuelPlayerPanel getDuelPlayerPanel(final PlayerProfile player) {
            return playerPanels[player.isHuman() ? 0 : 1];
        }

        @Override
        public void PlayerProfileUpdated(PlayerProfile player) {
            getDuelPlayerPanel(player).setPlayer(player);
        }

        @Override
        public void PlayerProfileDeleted(PlayerProfile deletedPlayer) {
            if (deletedPlayer.isHuman()) {
                final PlayerProfile playerProfile = PlayerProfiles.getDefaultHumanPlayer();
                DuelConfig.getInstance().setPlayerProfile(0, playerProfile);
                getDuelPlayerPanel(playerProfile).setPlayer(playerProfile);
            } else {
                final PlayerProfile playerProfile = PlayerProfiles.getDefaultAiPlayer();
                DuelConfig.getInstance().setPlayerProfile(1,  playerProfile);
                getDuelPlayerPanel(playerProfile).setPlayer(playerProfile);
            }
        }

        @Override
        public void PlayerProfileSelected(PlayerProfile player) {
            getDuelPlayerPanel(player).setPlayer(player);
            saveSelectedPlayerProfile(player);
        }

        private void saveSelectedPlayerProfile(final PlayerProfile player) {
            DuelConfig.getInstance().setPlayerProfile(player.isHuman() ? 0 : 1, player);
        }

        private boolean isDeckValid(int i) {
            if (getDeckType(i) != DeckType.Random) {
                final String deckFilename = getDeckValue(i) + DeckUtils.DECK_EXTENSION;
                final Path deckFolder = DeckType.getDeckFolder(getDeckType(i));
                try {
                    final MagicDeck deck = DeckUtils.loadDeckFromFile(deckFolder.resolve(deckFilename));
                    return deck.isValid();
                } catch (InvalidDeckException ex) {
                    return false;
                }
            } else {
                return true;
            }
        }

    }

}
