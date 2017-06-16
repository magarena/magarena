package magic.ui.screen.duel.setup;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Path;
import javax.swing.JPanel;
import magic.data.DeckType;
import magic.data.DuelConfig;
import magic.data.MagicFormat;
import magic.exception.InvalidDeckException;
import magic.model.MagicDeck;
import magic.model.player.IPlayerProfileListener;
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;
import magic.ui.ScreenController;
import magic.ui.helpers.MouseHelper;
import magic.ui.screen.widget.DuelSettingsPanel;
import magic.ui.utility.MagicStyle;
import magic.utility.DeckUtils;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ScreenContentPanel extends JPanel implements IPlayerProfileListener {

    private static final int PLAYERS_COUNT = 2;

    private final MigLayout migLayout = new MigLayout();
    private final DuelSettingsPanel duelSettingsPanel;
    private final DuelPlayerPanel[] playerPanels = new DuelPlayerPanel[PLAYERS_COUNT];
    private final DuelPlayerDeckPanel[] newPlayerDeckPanels = new DuelPlayerDeckPanel[PLAYERS_COUNT];

    ScreenContentPanel(final DuelConfig config) {
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

    MagicFormat getCube() {
        return duelSettingsPanel.getCube();
    }

    int getNrOfGames() {
        return duelSettingsPanel.getNrOfGames();
    }

    int getHandSize() {
        return duelSettingsPanel.getHandSize();
    }

    int getStartLife() {
        return duelSettingsPanel.getStartLife();
    }

    String getDeckValue(final int playerIndex) {
        return newPlayerDeckPanels[playerIndex].getDeckValue();
    }

    DeckType getDeckType(final int playerIndex) {
        return newPlayerDeckPanels[playerIndex].getDeckType();
    }

    PlayerProfile getPlayerProfile(final int index) {
        return playerPanels[index].getPlayer();
    }

    private DuelPlayerPanel getDuelPlayerPanel(final PlayerProfile player) {
        return playerPanels[player.isHuman() ? 0 : 1];
    }

    @Override
    public void playerProfileUpdated(PlayerProfile player) {
        getDuelPlayerPanel(player).setPlayer(player);
    }

    @Override
    public void playerProfileDeleted(PlayerProfile deletedPlayer) {
        if (deletedPlayer.isHuman()) {
            final PlayerProfile playerProfile = PlayerProfiles.getDefaultHumanPlayer();
            DuelConfig.getInstance().setPlayerProfile(0, playerProfile);
            getDuelPlayerPanel(playerProfile).setPlayer(playerProfile);
        } else {
            final PlayerProfile playerProfile = PlayerProfiles.getDefaultAiPlayer();
            DuelConfig.getInstance().setPlayerProfile(1, playerProfile);
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

    boolean isDeckValid(int i) {
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
