package magic.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Stack;
import javax.activation.MimetypesFileTypeMap;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;
import magic.MagicMain;
import magic.MagicUtility;
import magic.data.DuelConfig;
import magic.data.GeneralConfig;
import magic.data.IconImages;
import magic.data.OSXAdapter;
import magic.model.MagicCardDefinition;
import magic.model.MagicCardList;
import magic.model.MagicDeck;
import magic.model.MagicDeckConstructionRule;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicGameLog;
import magic.model.player.IPlayerProfileListener;
import magic.model.player.PlayerProfile;
import magic.ui.choice.MulliganChoicePanel;
import magic.ui.dialog.PreferencesDialog;
import magic.ui.screen.AbstractScreen;
import magic.ui.screen.AvatarImagesScreen;
import magic.ui.screen.CardExplorerScreen;
import magic.ui.screen.CardScriptScreen;
import magic.ui.screen.CardZoneScreen;
import magic.ui.screen.DeckEditorScreen;
import magic.ui.screen.DeckViewScreen;
import magic.ui.screen.DecksScreen;
import magic.ui.screen.DuelDecksScreen;
import magic.ui.screen.DuelGameScreen;
import magic.ui.screen.GameLogScreen;
import magic.ui.screen.HelpMenuScreen;
import magic.ui.screen.KeywordsScreen;
import magic.ui.screen.MainMenuScreen;
import magic.ui.screen.MulliganScreen;
import magic.ui.screen.NewDuelSettingsScreen;
import magic.ui.screen.ReadmeScreen;
import magic.ui.screen.SampleHandScreen;
import magic.ui.screen.SelectAiPlayerScreen;
import magic.ui.screen.SelectHumanPlayerScreen;
import magic.ui.screen.SettingsMenuScreen;
import magic.ui.screen.interfaces.IAvatarImageConsumer;
import magic.ui.screen.interfaces.IDeckConsumer;
import magic.ui.screen.interfaces.IThemeStyle;
import magic.ui.utility.GraphicsUtilities;
import magic.utility.MagicFiles;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.io.FileUtils;

@SuppressWarnings("serial")
public class MagicFrame extends JFrame {

    private boolean ignoreWindowDeactivate;
    private boolean confirmQuitToDesktop = true;

    private static final Dimension MIN_SIZE = new Dimension(GeneralConfig.DEFAULT_WIDTH, GeneralConfig.DEFAULT_HEIGHT);

    // Check if we are on Mac OS X.  This is crucial to loading and using the OSXAdapter class.
    public static final boolean MAC_OS_X = System.getProperty("os.name").toLowerCase().startsWith("mac os x");

    private final GeneralConfig config;
    private final JPanel contentPanel;
    private MagicDuel duel;
    private final Stack<AbstractScreen> screens;
    private final boolean dontShowAgain = true;

    public MagicFrame(final String frameTitle) {

        ToolTipManager.sharedInstance().setInitialDelay(400);

        // Load settings.
        config = GeneralConfig.getInstance();
        config.load();

        screens = new Stack<AbstractScreen>();

        // Setup frame.
        this.setTitle(frameTitle + "  [F11 : full screen]");
        this.setIconImage(IconImages.ARENA.getImage());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListeners();
        registerForMacOSXEvents();
        setSizeAndPosition();

        // Setup content container with a painted background based on theme.
        contentPanel = new BackgroundPanel(new MigLayout("insets 0, gap 0"));
        contentPanel.setOpaque(true);
        setContentPane(contentPanel);
        setF10KeyInputMap();
        setF11KeyInputMap();
        setF12KeyInputMap();

        // Enable drag and drop of background image file.
        new DropTarget(this, new ImageDragDropListener());

        setVisible(true);
    }

    //
    // The various (Mag)screens that can currently be displayed.
    //
    public void showDeckChooserScreen(final IDeckConsumer deckConsumer) {
        activateMagScreen(new DecksScreen(deckConsumer));
    }
    public void showCardScriptScreen(final MagicCardDefinition card) {
        activateMagScreen(new CardScriptScreen(card));
    }
    public void showGameLogScreen() {
        activateMagScreen(new GameLogScreen());
    }
    public void showDuelPlayersScreen() {
        activateMagScreen(new NewDuelSettingsScreen());
    }
    public void showAvatarImagesScreen(final IAvatarImageConsumer consumer) {
        activateMagScreen(new AvatarImagesScreen(consumer));
    }
    public void showSelectHumanPlayerScreen(final IPlayerProfileListener listener, final PlayerProfile profile) {
        activateMagScreen(new SelectHumanPlayerScreen(listener, profile));
    }
    public void showSelectAiProfileScreen(final IPlayerProfileListener listener, final PlayerProfile profile) {
        activateMagScreen(new SelectAiPlayerScreen(listener, profile));
    }
    public void showDeckView(final MagicDeck deck) {
        activateMagScreen(new DeckViewScreen(deck));
    }
    public void showMulliganScreen(final MulliganChoicePanel choicePanel, final MagicCardList hand) {
        if (screens.peek() instanceof MulliganScreen) {
            final MulliganScreen screen = (MulliganScreen)screens.peek();
            screen.dealNewHand(choicePanel, hand);
        } else {
            activateMagScreen(new MulliganScreen(choicePanel, hand));
        }
    }
    public void showCardZoneScreen(final MagicCardList cards, final String zoneName, final boolean animateCards) {
        activateMagScreen(new CardZoneScreen(cards, zoneName, animateCards));
    }
    public void showSampleHandGenerator(final MagicDeck deck) {
        activateMagScreen(new SampleHandScreen(deck));
    }
    public void showDeckEditor() {
        activateMagScreen(new DeckEditorScreen());
    }
    public void showDeckEditor(final MagicDeck deck) {
        activateMagScreen(new DeckEditorScreen(deck));
    }
    public void showCardExplorerScreen() {
        activateMagScreen(new CardExplorerScreen());
    }
    public void showReadMeScreen() {
        activateMagScreen(new ReadmeScreen());
    }
    public void showKeywordsScreen() {
        activateMagScreen(new KeywordsScreen());
    }
    public void showHelpMenuScreen() {
        activateMagScreen(new HelpMenuScreen());
    }
    public void showSettingsMenuScreen() {
        activateMagScreen(new SettingsMenuScreen());
    }
    private void showDuelDecksScreen() {
        if (screens.peek() instanceof DuelDecksScreen) {
            screens.pop();
        }
        activateMagScreen(new DuelDecksScreen(duel));
    }
    public void showMainMenuScreen() {
        screens.clear();
        activateMagScreen(new MainMenuScreen());
    }
    private void activateMagScreen(final AbstractScreen screen) {
        showMagScreen(screen);
        screens.push(screen);
        screen.requestFocus();
    }
    private void showMagScreen(final AbstractScreen screen) {
        contentPanel.removeAll();
        contentPanel.add(screen, "w 100%, h 100%");
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    public void closeActiveScreen(final boolean isEscapeKeyAction) {
        if (screens.size() == 1) {
            quitToDesktop(isEscapeKeyAction);
        } else {
            final AbstractScreen activeScreen = screens.pop();
            final AbstractScreen nextScreen = screens.peek();
            if (activeScreen.isScreenReadyToClose(nextScreen)) {
                showMagScreen(nextScreen);
                if (nextScreen instanceof DuelGameScreen) {
                    ((DuelGameScreen)nextScreen).updateView();
                } else if (nextScreen instanceof MainMenuScreen) {
                    ((MainMenuScreen)nextScreen).updateMissingImagesNotification();
                }
            } else {
                screens.push(activeScreen);
            }
        }
    }

    private void addWindowListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent event) {
                onClose();
            }
            @Override
            public void windowDeactivated(final WindowEvent e) {
                if (isFullScreen() && e.getOppositeWindow() == null && !ignoreWindowDeactivate) {
                    setState(Frame.ICONIFIED);
                }
                ignoreWindowDeactivate = false;
            }
        });
    }

    public void showDuel() {
        if (duel!=null) {
            showDuelDecksScreen();
            if (Boolean.getBoolean("selfMode")) {
                if (!duel.isFinished()) {
                    nextGame();
                } else {
                    newDuel(DuelConfig.getInstance());
                }
            }
        }
    }

    public void newDuel(final DuelConfig configuration) {
        duel = new MagicDuel(configuration);
        duel.initialize();
        showDuel();
    }

    public void loadDuel() {
        final File duelFile=MagicDuel.getDuelFile();
        if (duelFile.exists()) {
            duel=new MagicDuel();
            duel.load(duelFile);
            showDuel();
        } else {
            JOptionPane.showMessageDialog(this, "No saved duel found.", "Invalid Action", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void restartDuel() {
        if (duel!=null) {
            duel.restart();
            showDuel();
        }
    }

    public boolean isLegalDeckAndShowErrors(final MagicDeck deck, final String playerName) {
        final String brokenRulesText =
                MagicDeckConstructionRule.getRulesText(MagicDeckConstructionRule.checkDeck(deck));

        if (brokenRulesText.length() > 0) {
            JOptionPane.showMessageDialog(
                    this,
                    playerName + "'s deck is illegal.\n\n" + brokenRulesText,
                    "Illegal Deck",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public void nextGame() {
        activateMagScreen(new DuelGameScreen(duel));
    }

    public void openGame(final MagicGame game) {
        activateMagScreen(new DuelGameScreen(game));
    }

    /**
     * Set up our application to respond to the Mac OS X application menu
     */
    private void registerForMacOSXEvents() {
        if (MAC_OS_X) {
            try {
                // Generate and register the OSXAdapter, passing it a hash of all the methods we wish to
                // use as delegates for various com.apple.eawt.ApplicationListener methods
                OSXAdapter.setQuitHandler(this, getClass().getDeclaredMethod("onClose"));
                //OSXAdapter.setAboutHandler(this, getClass().getDeclaredMethod("about", (Class[])null));
                //OSXAdapter.setPreferencesHandler(this, getClass().getDeclaredMethod("preferences", (Class[])null));
                //OSXAdapter.setFileHandler(this, getClass().getDeclaredMethod("loadImageFile", new Class[] { String.class }));
            } catch (Exception e) {
                System.err.println("Error while loading the OSXAdapter:");
                e.printStackTrace();
            }
        }
    }

    public boolean onClose() {
        if (!confirmQuitToDesktop) {
            doShutdownMagarena();
        } else {
            final String message = "Are you sure you want to quit Magarena?\n";
            final Object[] params = {message};
            final int n = JOptionPane.showConfirmDialog(
                    contentPanel,
                    params,
                    "Confirm Quit to Desktop",
                    JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                doShutdownMagarena();
            }
        }
        // set the ApplicationEvent as handled (for OS X)
        return false;
    }

    private void doShutdownMagarena() {
        if (isFullScreen()) {
            config.setFullScreen(true);
        } else {
            final boolean maximized = (MagicFrame.this.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH;
            if (maximized) {
                config.setMaximized(true);
            } else {
                config.setLeft(getX());
                config.setTop(getY());
                config.setWidth(getWidth());
                config.setHeight(getHeight());
                config.setMaximized(false);
            }
        }
        config.setConfirmExit(!dontShowAgain);
        config.save();

        MagicGameLog.close();

        /*
        if (gamePanel != null) {
            gamePanel.getController().haltGame();
        }
        */
        System.exit(0);
    }

    public void quitToDesktop(final boolean confirmQuit) {
        this.confirmQuitToDesktop = confirmQuit;
        processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    public void updateGameView() {
        if (screens.peek() instanceof DuelGameScreen) {
            final DuelGameScreen screen = (DuelGameScreen)screens.peek();
            screen.updateView();
        }
    }

    public void openPreferencesDialog() {
        new PreferencesDialog(this);
    }

    private void setSizeAndPosition() {
        setMinimumSize(MIN_SIZE);
        if (config.isFullScreen()) {
            setFullScreenMode(true);
        } else {
            this.setSize(config.getWidth(),config.getHeight());
            if (config.getLeft() != -1) {
                this.setLocation(config.getLeft(),config.getTop());
            } else {
                this.setLocationRelativeTo(null);
            }
            if (config.isMaximized()) {
                this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        }
    }

    /**
     *
     */
    public void closeDuelScreen() {
        closeActiveScreen(false);
        showDuel();
    }

    public void toggleFullScreenMode() {
        setFullScreenMode(!config.isFullScreen());
    }

    private void setFullScreenMode(final boolean isFullScreen) {
        this.dispose();
        if (isFullScreen) {
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            this.setUndecorated(true);
            final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            this.setSize(screenSize.width, screenSize.height);
            config.setFullScreen(true);
        } else {
            this.setExtendedState(JFrame.NORMAL);
            this.setUndecorated(false);
            config.setFullScreen(false);
            setSizeAndPosition();
        }
        setVisible(true);
        ignoreWindowDeactivate = true;
        config.save();
    }

    private boolean isFullScreen() {
        return isMaximized() && this.isUndecorated();
    }

    private boolean isMaximized() {
        return this.getExtendedState() == JFrame.MAXIMIZED_BOTH;
    }

    /**
     * F10 take a screen shot.
     */
    private void setF10KeyInputMap() {
        contentPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), "Screenshot");
        contentPanel.getActionMap().put("Screenshot", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                MagicUtility.setBusyMouseCursor(true);
                doScreenshot();
                MagicUtility.setBusyMouseCursor(false);
            }
        });
    }

    /**
     * F11 key toggles full screen mode.
     */
    private void setF11KeyInputMap() {
        contentPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), "FullScreen");
        contentPanel.getActionMap().put("FullScreen", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                toggleFullScreenMode();
            }
        });
    }

    private void setF12KeyInputMap() {
        contentPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), "HideMenu");
        contentPanel.getActionMap().put("HideMenu", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final AbstractScreen activeScreen = screens.peek();
                activeScreen.setVisible(!activeScreen.isVisible());
            }
        });
    }

    private void doScreenshot() {
        try {
            final Path filePath = Paths.get(MagicMain.getLogsPath()).resolve("screenshot.png");
            final File imageFile = GraphicsUtilities.doScreenshotToFile(this.getContentPane(), filePath);
            MagicFiles.openFileInDefaultOsEditor(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.toString(), "Screenshot Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sets the background to an image that has been dragged into the Magarena frame.
     * <p>
     * This might be an image file or an image dragged directly from the internet browser.
     */
    private class ImageDragDropListener implements DropTargetListener {

        @Override
        public void drop(DropTargetDropEvent event) {
            processDroppedImageFile(event);
        }

        @Override
        public void dragEnter(DropTargetDragEvent event) { }
        @Override
        public void dragExit(DropTargetEvent event) { }
        @Override
        public void dragOver(DropTargetDragEvent event) { }
        @Override
        public void dropActionChanged(DropTargetDragEvent event) { }

        @SuppressWarnings("unchecked")
        private void processDroppedImageFile(final DropTargetDropEvent event) {

            event.acceptDrop(DnDConstants.ACTION_COPY);

            // Get the transfer which can provide the dropped item data
            Transferable transferable = event.getTransferable();

            // Get the data formats of the dropped item
            DataFlavor[] flavors = transferable.getTransferDataFlavors();

            for (DataFlavor flavor : flavors) {
                try {
                    // If the drop items are files then process the first one only.
                    if (flavor.isFlavorJavaFileListType()) {

                        final List<File> files = (List<File>)transferable.getTransferData(flavor);

                        // linux workaround - no need to crash out.
                        if (files == null || files.size() == 0) {
                            JOptionPane.showMessageDialog(MagicFrame.this, "Sorry, this did not work.\nTry downloading the image first and then dragging the file into Magarena.", "Drag & drop failed!", JOptionPane.ERROR_MESSAGE);
                            break;
                        }

                        final File imageFile = new File(files.get(0).getPath());

                        if (isValidImageFile(imageFile)) {
                            final Path path = Paths.get(MagicMain.getModsPath()).resolve("background.image");
                            FileUtils.copyFile(imageFile, path.toFile());
                            refreshBackground();
                            config.setCustomBackground(true);
                            config.save();
                        } else {
                            JOptionPane.showMessageDialog(contentPanel, "Invalid image!", "Invalid Image", JOptionPane.WARNING_MESSAGE);
                        }
                        break;
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            // Inform that the drop is complete
            event.dropComplete(true);

        }

        private boolean isValidImageFile(final File imageFile) {
            return isValidMimeType(imageFile, "image") &&
                    GraphicsUtilities.isValidImageFile(imageFile.toPath());
        }

        private boolean isValidMimeType(final File file, final String mimeType) {
            final MimetypesFileTypeMap mtftp = new MimetypesFileTypeMap();
            mtftp.addMimeTypes("image png tif jpg jpeg bmp");
            final String mimetype = mtftp.getContentType(file);
            final String fileType = mimetype.split("/")[0];
            return fileType.equalsIgnoreCase(mimeType);
        }

    }

    private void refreshBackground() {
        ((BackgroundPanel)contentPanel).refreshBackground();
    }

    private void refreshComponentStyle(final JComponent container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JComponent) {
                final JComponent widget = (JComponent)component;
                if (widget.getComponentCount() > 0) {
                    refreshComponentStyle(widget);
                }
                if (widget instanceof IThemeStyle) {
                    ((IThemeStyle)widget).refreshStyle();
                }
            }
        }
    }

    public void refreshLookAndFeel() {
        for (AbstractScreen screen : screens) {
            refreshComponentStyle(screen);
        }
        refreshBackground();
    }

}
