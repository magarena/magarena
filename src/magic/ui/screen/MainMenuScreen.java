package magic.ui.screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.nio.file.Files;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.MagicUtility;
import magic.data.GeneralConfig;
import magic.data.IconImages;
import magic.game.state.GameStateFileReader;
import magic.game.state.GameLoader;
import magic.ui.ScreenController;
import magic.ui.dialog.ImportDialog;
import magic.ui.screen.interfaces.IThemeStyle;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuPanel;
import magic.ui.theme.Theme;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;
import magic.ui.screen.interfaces.IWikiPage;
import magic.ui.widget.alerter.AlertPanel;
import magic.utility.MagicFileSystem;
import magic.utility.MagicStyle;

@SuppressWarnings("serial")
public class MainMenuScreen extends AbstractScreen implements IWikiPage {

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();
    private static final AlertPanel alertPanel = new AlertPanel();

    public MainMenuScreen() {
        setContent(getScreenContent());
        showImportDialogOnNewInstall();
        alertPanel.refreshAlerts();
    }

    private void showImportDialogOnNewInstall() {
        if (!Files.exists(MagicFileSystem.getDataPath().resolve(GeneralConfig.CONFIG_FILENAME))) {
            new ImportDialog(getFrame());
        }
    }

    private JPanel getScreenContent() {

        final MenuPanel menuPanel = getMenuPanel();
        menuPanel.refreshLayout();

        final JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new MigLayout("insets 0, gap 0, alignx center, flowy"));
        mainPanel.add(menuPanel);
        if (MagicUtility.isDevMode()) {
            mainPanel.add(new IconPanel());
        }

        final JPanel content = new JPanel();
        content.setOpaque(false);

        final MigLayout layout = new MigLayout();
        layout.setLayoutConstraints("insets 0, gap 0, flowy");
        layout.setRowConstraints("[30!][100%, center][30!]");
        content.setLayout(layout);
        content.add(new JLabel(), "w 100%, h 100%");
        content.add(mainPanel, "w 100%");
        content.add(alertPanel, "w 100%, h 100%");
        return content;
        
    }
    
    private MenuPanel getMenuPanel() {

        final MenuPanel menuPanel = new MenuPanel("Main Menu");

        if (MagicUtility.isDevMode()) {
            menuPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.BLACK));
            menuPanel.setPreferredSize(new Dimension(300, 340));
            menuPanel.setMaximumSize(new Dimension(300, 340));
        }

        menuPanel.addMenuItem("New duel", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.showDuelPlayersScreen();
            }
        });
        menuPanel.addMenuItem("Resume duel", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                getFrame().loadDuel();
            }
        });
        menuPanel.addMenuItem("Card explorer", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.showCardExplorerScreen();
            }
        });
        menuPanel.addMenuItem("Deck editor", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.showDeckEditor();
            }
        });
        menuPanel.addMenuItem("Settings", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.showSettingsMenuScreen();
            }
        });
        menuPanel.addMenuItem("Help", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.showHelpMenuScreen();
            }
        });
        menuPanel.addMenuItem("Quit to desktop", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.closeActiveScreen(false);
            }
        });

        return menuPanel;
    }


    private class IconPanel extends TexturedPanel implements IThemeStyle  {

        private final MigLayout miglayout = new MigLayout();

        public IconPanel() {
            setLayout(miglayout);
            setPreferredSize(new Dimension(300, 40));
            setMaximumSize(new Dimension(300, 40));
            refreshStyle();
            refreshLayout();
        }

        private void refreshLayout() {
            miglayout.setLayoutConstraints("insets 4 0 0 0");
            final ActionBarButton btn = new ActionBarButton(
                        IconImages.LOAD_ICON,
                        "Resume saved game", "Select a previously saved game and run.",
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                loadSavedGame();
                            }
                        }
                );
            add(btn);
        }

        @Override
        public void refreshStyle() {
            final Color refBG = MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND);
            final Color thisBG = MagicStyle.getTranslucentColor(refBG, 200);
            setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK));
            setBackground(thisBG);
        }

    }

    /* (non-Javadoc)
     * @see magic.ui.MagScreen#isScreenReadyToClose(magic.ui.MagScreen)
     */
    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        return true;
    }

    public void updateMissingImagesNotification() {
        if (CONFIG.isMissingFiles()) {
            CONFIG.setIsMissingFiles(false);
            alertPanel.refreshAlerts();
        }
    }

    private void loadSavedGame() {
        final String filename = GameStateFileReader.getSaveGameFilename();
        if (!filename.isEmpty()) {
            ScreenController.getMainFrame().openGame(GameLoader.loadSavedGame(filename));
        }
    }

    @Override
    public String getWikiPageName() {
        return "Main-Menu-Screen";
    }
    
}
