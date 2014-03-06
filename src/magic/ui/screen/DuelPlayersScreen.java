package magic.ui.screen;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import magic.data.DuelConfig;
import magic.model.player.PlayerProfile;
import magic.ui.MagicFrame;
import magic.ui.screen.AbstractScreen;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.widget.DuelSettingsPanel;
import magic.ui.screen.widget.MenuButton;
import magic.ui.widget.DeckComboPanel;
import magic.ui.widget.DuelPlayerPanel;

@SuppressWarnings("serial")
public class DuelPlayersScreen
    extends AbstractScreen
    implements IStatusBar, IActionBar {

    private static final DuelConfig duelConfig = DuelConfig.getInstance();

    private ScreenContent content;

    public DuelPlayersScreen() {
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
        return new MenuButton("Main Menu", new AbstractAction() {
          @Override
          public void actionPerformed(final ActionEvent e) {
              getFrame().closeActiveScreen(false);
          }
      });
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
        duelConfig.setPlayerOneProfile(content.getPlayerProfile(0));
        duelConfig.setPlayerTwoProfile(content.getPlayerProfile(1));
        duelConfig.setPlayerOneDeckGenerator(content.getPlayerDeckGenerator(0));
        duelConfig.setPlayerTwoDeckGenerator(content.getPlayerDeckGenerator(1));
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

    private class ScreenContent extends JPanel {

        private final DuelSettingsPanel duelSettingsPanel;
        private final DuelPlayerPanel[] playerPanels = new DuelPlayerPanel[2];
        private final DeckComboPanel[] playerDeckPanels = new DeckComboPanel[2];

        public ScreenContent(final DuelConfig config, final MagicFrame frame) {

            duelSettingsPanel = new DuelSettingsPanel(frame, config);
            playerPanels[0] = new DuelPlayerPanel(frame, config.getPlayerOneProfile());
            playerPanels[1] = new DuelPlayerPanel(frame, config.getPlayerTwoProfile());
            playerDeckPanels[0] = new DeckComboPanel(config.getPlayerOneDeckGenerator());
            playerDeckPanels[1] = new DeckComboPanel(config.getPlayerTwoDeckGenerator());

            setOpaque(false);
            doMigLayout();
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

        public String getPlayerDeckGenerator(final int index) {
            return playerDeckPanels[index].getDeckGenerator();
        }

        public PlayerProfile getPlayerProfile(final int index) {
            return playerPanels[index].getPlayerProfile();
        }

        private void doMigLayout() {
            setLayout(new MigLayout("insets 0, center, center, wrap 2"));
            add(duelSettingsPanel, "w 548!, h 40!, span 2, gapbottom 4");
            layoutPlayerPanels();
            layoutPlayerDeckPanels();
        }

        private void layoutPlayerPanels() {
            for (final DuelPlayerPanel panel : playerPanels) {
                add(panel, "w 270!, h 270!, gapright 4");
            }
        }

        private void layoutPlayerDeckPanels() {
            for (final DeckComboPanel panel : playerDeckPanels) {
                add(panel, "w 270!");
            }
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
