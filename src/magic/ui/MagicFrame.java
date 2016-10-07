package magic.ui;

import magic.translate.UiString;
import java.awt.dnd.DropTarget;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import magic.data.CardDefinitions;
import magic.data.DuelConfig;
import magic.data.OSXAdapter;
import magic.exception.DesktopNotSupportedException;
import magic.model.MagicDeck;
import magic.model.MagicDeckConstructionRule;
import magic.model.MagicDuel;
import magic.model.MagicGameLog;
import magic.ui.screen.ScreenHelper;
import magic.ui.theme.ThemeFactory;
import magic.ui.utility.DesktopUtils;
import magic.ui.utility.GraphicsUtils;
import magic.utility.MagicFileSystem.DataPath;
import magic.utility.MagicFileSystem;
import magic.utility.MagicSystem;
import org.apache.commons.io.FileUtils;

@SuppressWarnings("serial")
public class MagicFrame extends MagicStickyFrame implements IDragDropListener {

    // translatable strings
    private static final String _S1 = "F11 : full screen";
    private static final String _S2 = "No saved duel found.";
    private static final String _S3 = "%s's deck is illegal.";
    private static final String _S4 = "Are you sure you want to quit Magarena?";
    private static final String _S5 = "Confirm Quit to Desktop";
    private static final String _S6 = "Invalid action!";

    private boolean confirmQuitToDesktop = true;

    // Check if we are on Mac OS X.  This is crucial to loading and using the OSXAdapter class.
    public static final boolean MAC_OS_X = System.getProperty("os.name").toLowerCase().startsWith("mac os x");

    private final MagicFramePanel contentPanel;
    private MagicDuel duel;

    public MagicFrame(final String frameTitle) {

        ToolTipManager.sharedInstance().setInitialDelay(400);

        // Setup frame.
        this.setTitle(String.format("%s [%s]", frameTitle, UiString.get(_S1)));
        this.setIconImage(MagicImages.APP_LOGO);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListeners();
        registerForMacOSXEvents();

        // Setup content container with a painted background based on theme.
        contentPanel = new MagicFramePanel();
        contentPanel.setOpaque(true);
        setContentPane(contentPanel);

        setKeyboardEventActions();

        // Enable drag and drop of background image file.
        new DropTarget(this, new FileDropTargetListener(this));

        setVisible(true);
    }
    
    private void setKeyboardEventActions() {
        ScreenHelper.setKeyEvent(contentPanel, KeyEvent.VK_F10, this::doScreenshot);
        ScreenHelper.setKeyEvent(contentPanel, KeyEvent.VK_F11, this::toggleFullScreenMode);
        ScreenHelper.setKeyEvent(contentPanel, KeyEvent.VK_F12, this::toggleUI);
        ScreenHelper.setKeyEvent(contentPanel, KeyEvent.VK_ESCAPE, this::doEscapeKeyAction);
    }

    private void doEscapeKeyAction() {
        ScreenController.closeActiveScreen(true);
    }
    
    private void addWindowListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent event) {
                onClose();
            }
        });
    }

    public void showDuel() {
        if (duel!=null) {
            ScreenController.showDuelDecksScreen(duel);
            if (MagicSystem.isAiVersusAi()) {
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
        final File duelFile=MagicDuel.getLatestDuelFile();
        if (duelFile.exists()) {
            duel=new MagicDuel();
            duel.load(duelFile);
            showDuel();
        } else {
            ScreenController.showWarningMessage(UiString.get(_S2));
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
            ScreenController.showWarningMessage(
                    String.format("%s\n\n%s", UiString.get(_S3, playerName), brokenRulesText));
            return false;
        }

        return true;
    }

    public void nextGame() {
        ScreenController.showDuelGameScreen(duel);
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
            final String message = String.format("%s\n", UiString.get(_S4));
            final Object[] params = {message};
            final int n = JOptionPane.showConfirmDialog(
                    contentPanel,
                    params,
                    UiString.get(_S5),
                    JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                doShutdownMagarena();
            }
        }
        // set the ApplicationEvent as handled (for OS X)
        return false;
    }

    private void doShutdownMagarena() {

        saveSizeAndPosition();

        MagicGameLog.close();
        MagicSound.shutdown();

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

    /**
     *
     */
    public void closeDuelScreen() {
        ScreenController.closeActiveScreen(false);
        showDuel();
    }

    private void doScreenshot() {
        try {
            final Path filePath = MagicFileSystem.getDataPath(DataPath.LOGS).resolve("screenshot.png");
            final File imageFile = GraphicsUtils.doScreenshotToFile(this.getContentPane(), filePath);
            DesktopUtils.openFileInDefaultOsEditor(imageFile);
        } catch (IOException | DesktopNotSupportedException e) {
            e.printStackTrace();
            ScreenController.showWarningMessage(e.toString());
        }
    }

    private void refreshBackground() {
        contentPanel.refreshBackground();
    }


    public void refreshLookAndFeel() {
        ScreenController.refreshStyle();
        refreshBackground();
    }

    public void refreshUI() {
        config.setIsMissingFiles(false);
        CardDefinitions.checkForMissingFiles();
        ThemeFactory.getInstance().loadThemes();
        refreshLookAndFeel();
    }

    public void setContentPanel(JPanel aPanel) {
        contentPanel.setContentPanel(aPanel);
    }

    private boolean replaceBackgroundImage(File newImage) {
        try {
            FileUtils.copyFile(newImage, MagicFileSystem.getBackgroundImageFile());
            return true;
        } catch (IOException ex) {
            ScreenController.showWarningMessage(String.format("%s\n\n%s", 
                    UiString.get(_S6),
                    ex.getMessage())
            );
        }
        return false;
    }

    private void doSetCustomBackgroundImage(File imageFile) {

        if (!GraphicsUtils.isValidImageFile(imageFile)) {
            ScreenController.showWarningMessage("Invalid image file.");
            return;
        }

        final String message = String.format("%s\n", UiString.get("Replace background image?"));
        final Object[] params = {message};
        final int response = JOptionPane.showConfirmDialog(
                contentPanel,
                params,
                UiString.get("Confirmation required..."),
                JOptionPane.YES_NO_OPTION);
        
        if (response == JOptionPane.YES_OPTION) {
            if (replaceBackgroundImage(imageFile)) {
                config.setCustomBackground(true);
                config.save();
                refreshLookAndFeel();
            }
        }
    }

    @Override
    public void onImageFileDropped(File imageFile) {
        doSetCustomBackgroundImage(imageFile);
    }

    @Override
    public void onZipFileDropped(File zipFile) {
        System.out.println("[TODO] onZipFileDropped : " + zipFile);
    }

    private void toggleUI() {
        final JPanel activeScreen = ScreenController.getActiveScreen();
        activeScreen.setVisible(!activeScreen.isVisible());
    }

}
