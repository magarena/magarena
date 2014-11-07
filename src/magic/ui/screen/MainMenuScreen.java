package magic.ui.screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import magic.MagicMain;
import magic.MagicUtility;
import magic.data.CardDefinitions;
import magic.data.GeneralConfig;
import magic.data.IconImages;
import magic.game.state.GameStateFileReader;
import magic.game.state.GameLoader;
import magic.ui.dialog.ImportDialog;
import magic.ui.screen.interfaces.IThemeStyle;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuPanel;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.TexturedPanel;
import magic.utility.MagicFileSystem;
import net.miginfocom.swing.MigLayout;
import magic.ui.screen.interfaces.IWikiPage;

@SuppressWarnings("serial")
public class MainMenuScreen extends AbstractScreen implements IWikiPage {

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();

    public MainMenuScreen() {
        setContent(getScreenContent());
        checkForMissingFiles();
        showImportDialogOnNewInstall();
    }

    private void showImportDialogOnNewInstall() {
        if (!Files.exists(MagicFileSystem.getDataPath().resolve(GeneralConfig.CONFIG_FILENAME))) {
            new ImportDialog(getFrame());
        }
    }

    private JPanel getScreenContent() {

        final JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new MigLayout("insets 0, gap 0, center, center, flowy"));

        final MenuPanel menuPanel = new MenuPanel("Main Menu");
        menuPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.BLACK));
        menuPanel.setPreferredSize(new Dimension(300, 340));
        menuPanel.setMaximumSize(new Dimension(300, 340));

        content.add(menuPanel);
        if (MagicUtility.isDevMode()) {
            content.add(new IconPanel());
        }

        menuPanel.addMenuItem("New duel", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                getFrame().showDuelPlayersScreen();
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
                getFrame().showCardExplorerScreen();
            }
        });
        menuPanel.addMenuItem("Deck editor", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                getFrame().showDeckEditor();
            }
        });
        menuPanel.addMenuItem("Settings", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                getFrame().showSettingsMenuScreen();
            }
        });
        menuPanel.addMenuItem("Help", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                getFrame().showHelpMenuScreen();
            }
        });
        menuPanel.addMenuItem("Quit to desktop", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                getFrame().closeActiveScreen(false);
            }
        });

        menuPanel.refreshLayout();
        return content;
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
            final Theme THEME = ThemeFactory.getInstance().getCurrentTheme();
            final Color refBG = THEME.getColor(Theme.COLOR_TITLE_BACKGROUND);
            final Color thisBG = new Color(refBG.getRed(), refBG.getGreen(), refBG.getBlue(), 200);
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

    private void checkForMissingFiles() {
        new SwingWorker<Boolean, Void> () {
            @Override
            protected Boolean doInBackground() throws Exception {
                return CardDefinitions.isMissingImages();
            }
            @Override
            protected void done() {
                try {
                    CONFIG.setIsMissingFiles(get());
                    repaint();
                } catch (InterruptedException | ExecutionException e1) {
                    throw new RuntimeException(e1);
                }
            }
        }.execute();
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (CONFIG.isMissingFiles()) {
            g.setFont(new Font("Dialog", Font.PLAIN, 22));
            drawStringWithOutline(g, "New card images are ready to download!", 20, 30);
            g.setFont(new Font("Dialog", Font.PLAIN, 18));
            drawStringWithOutline(g, "Click Settings >> Download card images", 20, 56);
        }
    }

    private void drawStringWithOutline(final Graphics g, final String str, int x, int y) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Color.BLACK);
        for (int i = 1; i <= 1; i++) {
            g.drawString(str,x+i,y);
            g.drawString(str,x-i,y);
            g.drawString(str,x,y+i);
            g.drawString(str,x,y-i);
        }
        g.setColor(Color.WHITE);
        g.drawString(str,x,y);
    }

    public void updateMissingImagesNotification() {
        if (CONFIG.isMissingFiles()) {
            CONFIG.setIsMissingFiles(false);
            repaint();
            checkForMissingFiles();
        }
    }

    private void loadSavedGame() {
        final String filename = GameStateFileReader.getSaveGameFilename();
        if (!filename.isEmpty()) {
            MagicMain.rootFrame.openGame(GameLoader.loadSavedGame(filename));
        }
    }

    @Override
    public String getWikiPageName() {
        return "Main-Menu-Screen";
    }
	
}
