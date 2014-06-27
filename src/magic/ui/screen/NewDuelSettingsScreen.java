package magic.ui.screen;

import magic.MagicUtility;
import magic.data.DeckType;
import magic.data.DuelConfig;
import magic.model.player.HumanPlayer;
import magic.model.player.IPlayerProfileListener;
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;
import magic.ui.MagicFrame;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.widget.DuelSettingsPanel;
import magic.ui.screen.widget.MenuButton;
import magic.ui.widget.player.DuelPlayerDeckPanel;
import magic.ui.widget.player.DuelPlayerPanel;
import magic.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import magic.ui.screen.interfaces.IWikiPage;

@SuppressWarnings("serial")
public class NewDuelSettingsScreen
    extends AbstractScreen
    implements IStatusBar, IActionBar, IWikiPage {

    private static final DuelConfig duelConfig = DuelConfig.getInstance();

    private ScreenContent content;

    public NewDuelSettingsScreen() {
        duelConfig.load();
        content = new ScreenContent(duelConfig, getFrame());
        setContent(content);
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagStatusBar#getScreenCaption()
     */
    @Override
    public String getScreenCaption() {
        return "New Duel Settings";
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getLeftAction()
     */
    @Override
    public MenuButton getLeftAction() {
        return MenuButton.getCloseScreenButton("Main Menu");
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getRightAction()
     */
    @Override
    public MenuButton getRightAction() {
        return new MenuButton("Next", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDuelConfig();
                getFrame().closeActiveScreen(false);
                getFrame().newDuel(duelConfig);

            }
        });
    }

    private void saveDuelConfig() {
        duelConfig.setStartLife(content.getStartLife());
        duelConfig.setHandSize(content.getHandSize());
        duelConfig.setNrOfGames(content.getNrOfGames());
        duelConfig.setCube(content.getCube());
        duelConfig.setPlayerProfile(0, content.getPlayerProfile(0));
        duelConfig.setPlayerProfile(1, content.getPlayerProfile(1));
        duelConfig.setPlayerDeckProfile(0, content.getDeckType(0), content.getDeckValue(0));
        duelConfig.setPlayerDeckProfile(1, content.getDeckType(1), content.getDeckValue(1));
        duelConfig.save();
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getMiddleActions()
     */
    @Override
    public List<MenuButton> getMiddleActions() {
        return null;
    }

    /* (non-Javadoc)
     * @see magic.ui.MagScreen#canScreenClose()
     */
    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        saveDuelConfig();
        return true;
    }

    @Override
    public String getWikiPageName() {
        return "UINewDuel";
    }

    private class ScreenContent extends JPanel implements IPlayerProfileListener {

        private final MigLayout migLayout = new MigLayout();
        private final DuelSettingsPanel duelSettingsPanel;
        private final DuelPlayerPanel[] playerPanels = new DuelPlayerPanel[2];
        private final DuelPlayerDeckPanel[] newPlayerDeckPanels = new DuelPlayerDeckPanel[2];

        public ScreenContent(final DuelConfig config, final MagicFrame frame) {
            this.duelSettingsPanel = new DuelSettingsPanel(frame, config);
            this.playerPanels[0] = getNewDuelPlayerPanel(config.getPlayerProfile(0));
            this.playerPanels[1] = getNewDuelPlayerPanel(config.getPlayerProfile(1));
            this.newPlayerDeckPanels[0] = new DuelPlayerDeckPanel(frame, config.getPlayerDeckProfile(0));
            this.newPlayerDeckPanels[1] = new DuelPlayerDeckPanel(frame, config.getPlayerDeckProfile(1));
            setLookAndFeel();
            refreshLayout();
        }

        private void setLookAndFeel() {
            setOpaque(false);
            setLayout(migLayout);
        }

        private void refreshLayout() {
            removeAll();
            migLayout.setLayoutConstraints("insets 0, center, center, wrap 2");
            // duel settings
            add(duelSettingsPanel, "w 548!, h 40!, span 2, gapbottom 4");
            // players
            add(playerPanels[0], "w 270!, h 270!, gapright 4");
            add(playerPanels[1], "w 270!, h 270!, gapright 4");
            // player decks
            add(newPlayerDeckPanels[0], "w 270!, h 60!");
            add(newPlayerDeckPanels[1], "w 270!, h 60!");
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
                    MagicUtility.setBusyMouseCursor(true);
                    selectNewProfile(panel.getPlayer());
                    mouseExited(e);
                    MagicUtility.setBusyMouseCursor(false);
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
            if (playerProfile instanceof HumanPlayer) {
                getFrame().showSelectHumanPlayerScreen(this, playerProfile);
            } else {
                getFrame().showSelectAiProfileScreen(this, playerProfile);
            }
        }

        public String getCube() {
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
            if (player instanceof HumanPlayer) {
                return playerPanels[0];
            } else {
                return playerPanels[1];
            }
        }

        /* (non-Javadoc)
         * @see magic.model.player.IPlayerProfileListener#PlayerProfileUpdated(magic.model.player.PlayerProfile)
         */
        @Override
        public void PlayerProfileUpdated(PlayerProfile player) {
            getDuelPlayerPanel(player).setPlayer(player);
        }

        /* (non-Javadoc)
         * @see magic.model.player.IPlayerProfileListener#PlayerProfileDeleted(magic.model.player.PlayerProfile)
         */
        @Override
        public void PlayerProfileDeleted(PlayerProfile deletedPlayer) {
            if (deletedPlayer instanceof HumanPlayer) {
                final PlayerProfile playerProfile = PlayerProfiles.getDefaultHumanPlayer();
                DuelConfig.getInstance().setPlayerProfile(0, playerProfile);
                getDuelPlayerPanel(playerProfile).setPlayer(playerProfile);
            } else {
                final PlayerProfile playerProfile = PlayerProfiles.getDefaultAiPlayer();
                DuelConfig.getInstance().setPlayerProfile(1,  playerProfile);
                getDuelPlayerPanel(playerProfile).setPlayer(playerProfile);
            }
            DuelConfig.getInstance().save();
        }

        /* (non-Javadoc)
         * @see magic.model.player.IPlayerProfileListener#PlayerProfileSelected(magic.model.player.PlayerProfile)
         */
        @Override
        public void PlayerProfileSelected(PlayerProfile player) {
            getDuelPlayerPanel(player).setPlayer(player);
            saveSelectedPlayerProfile(player);
        }

        private void saveSelectedPlayerProfile(final PlayerProfile player) {
            if (player instanceof HumanPlayer) {
                DuelConfig.getInstance().setPlayerProfile(0, player);
            } else {
                DuelConfig.getInstance().setPlayerProfile(1, player);
            }
            DuelConfig.getInstance().save();
        }

    }

    /* (non-Javadoc)
     * @see magic.ui.interfaces.IStatusBar#getStatusPanel()
     */
    @Override
    public JPanel getStatusPanel() {
        return null;
    }

}
