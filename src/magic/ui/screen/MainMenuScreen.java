package magic.ui.screen;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import magic.data.GeneralConfig;
import magic.data.MagicIcon;
import magic.exception.InvalidDeckException;
import magic.game.state.GameLoader;
import magic.game.state.GameStateFileReader;
import magic.ui.IconImages;
import magic.ui.ScreenController;
import magic.ui.UiString;
import magic.ui.screen.interfaces.IWikiPage;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuPanel;
import magic.ui.widget.alerter.AlertPanel;
import magic.utility.MagicFileSystem;
import magic.utility.MagicSystem;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class MainMenuScreen extends AbstractScreen implements IWikiPage {

    // translatable strings.
    private static final String _S1 = "Main Menu";
    private static final String _S2 = "New duel";
    private static final String _S3 = "Resume duel";
    private static final String _S4 = "Card explorer";
    private static final String _S5 = "Deck editor";
    private static final String _S6 = "Settings";
    private static final String _S7 = "Help";
    private static final String _S8 = "Quit to desktop";

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();
    private static final AlertPanel alertPanel = new AlertPanel();

    public MainMenuScreen() {
        setContent(getScreenContent());
        alertPanel.refreshAlerts();
    }

    private JPanel getScreenContent() {

        final MenuPanel menuPanel = getMenuPanel();
        menuPanel.refreshLayout();

        if (MagicSystem.isDevMode()) {
            menuPanel.add(new IconPanel(), "w 100%, aligny bottom, pushy");
        }

        final JPanel content = new JPanel();
        content.setOpaque(false);

        final MigLayout layout = new MigLayout();
        layout.setLayoutConstraints("insets 0, gap 0, flowy");
        layout.setRowConstraints("[30!][100%, center][30!]");
        layout.setColumnConstraints("[center]");
        content.setLayout(layout);

        content.add(menuPanel, "cell 0 1");
        content.add(alertPanel, "w 100%, h 100%, cell 0 2");

        return content;

    }

    private MenuPanel getMenuPanel() {

        final MenuPanel menuPanel = new MenuPanel(UiString.get(_S1));

        menuPanel.addMenuItem(UiString.get(_S2), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.showDuelPlayersScreen();
            }
        });
        menuPanel.addMenuItem(UiString.get(_S3), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    getFrame().loadDuel();
                } catch (InvalidDeckException ex) {
                    ScreenController.showWarningMessage(ex.getMessage());
                }
            }
        });
        menuPanel.addMenuItem(UiString.get(_S4), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.showCardExplorerScreen();
            }
        });
        menuPanel.addMenuItem(UiString.get(_S5), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.showDeckEditor();
            }
        });
        menuPanel.addMenuItem(UiString.get(_S6), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.showSettingsMenuScreen();
            }
        });
        menuPanel.addMenuItem(UiString.get(_S7), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.showHelpMenuScreen();
            }
        });
        menuPanel.addMenuItem(UiString.get(_S8), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.closeActiveScreen(false);
            }
        });

        return menuPanel;
    }

    private class IconPanel extends JPanel {

        private final MigLayout miglayout = new MigLayout();

        public IconPanel() {
            setOpaque(false);
            setLayout(miglayout);
            refreshLayout();
        }

        private void refreshLayout() {
            miglayout.setLayoutConstraints("insets 4 0 0 0");
            final ActionBarButton btn = new ActionBarButton(
                    IconImages.getIcon(MagicIcon.LOAD_ICON),
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

    }

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
        final String filename = getSaveGameFilename();
        if (!filename.isEmpty()) {
            ScreenController.showDuelGameScreen(GameLoader.loadSavedGame(filename));
        }
    }

    private static String getSaveGameFilename() {
        final JFileChooser fileChooser = new JFileChooser(MagicFileSystem.getDataPath(MagicFileSystem.DataPath.SAVED_GAMES).toFile());
        fileChooser.setDialogTitle("Load & resume saved game");
        fileChooser.setFileFilter(TEST_FILE_FILTER);
        fileChooser.setAcceptAllFileFilterUsed(false);
        // Add the description preview pane
//        fileChooser.setAccessory(new DeckDescriptionPreview(fileChooser));
        final int action = fileChooser.showOpenDialog(ScreenController.getMainFrame());
        if (action == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getName();
        } else {
            return "";
        }
    }

    private static final FileFilter TEST_FILE_FILTER = new FileFilter() {
        @Override
        public boolean accept(final File file) {
            return file.isDirectory() || file.getName().endsWith(GameStateFileReader.TEST_FILE_EXTENSION);
        }

        @Override
        public String getDescription() {
            return "Saved Game File";
        }
    };

    @Override
    public String getWikiPageName() {
        return "Main-Menu-Screen";
    }

}
