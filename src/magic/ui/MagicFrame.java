package magic.ui;

import java.awt.dnd.DropTarget;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ToolTipManager;

import org.apache.commons.io.FileUtils;

import magic.data.CardDefinitions;
import magic.data.GeneralConfig;
import magic.data.OSXAdapter;
import magic.data.settings.BooleanSetting;
import magic.exception.DesktopNotSupportedException;
import magic.game.state.GameLoader;
import magic.model.MagicDeck;
import magic.model.MagicDeckConstructionRule;
import magic.model.MagicGameLog;
import magic.translate.MText;
import magic.ui.helpers.DesktopHelper;
import magic.ui.helpers.ImageHelper;
import magic.ui.screen.MScreen;
import magic.ui.screen.ScreenHelper;
import magic.ui.theme.ThemeFactory;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import magic.utility.MagicSystem;

@SuppressWarnings("serial")
public class MagicFrame extends MagicStickyFrame implements IDragDropListener {

    // translatable strings
    private static final String _S1 = "F11 : full screen";
    private static final String _S3 = "%s's deck is illegal.";
    private static final String _S4 = "Are you sure you want to quit Magarena?";
    private static final String _S5 = "Confirm Quit to Desktop";
    private static final String _S6 = "Invalid action!";

    private boolean confirmQuitToDesktop = true;

    // Check if we are on Mac OS X.  This is crucial to loading and using the OSXAdapter class.
    public static final boolean MAC_OS_X = System.getProperty("os.name").toLowerCase(Locale.ENGLISH).startsWith("mac os x");

    private final MagicFramePanel contentPanel;

    public MagicFrame(final String frameTitle) {

        ToolTipManager.sharedInstance().setInitialDelay(400);

        // Setup frame.
        this.setTitle(String.format("%s [%s]", frameTitle, MText.get(_S1)));
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
    }

    private void addWindowListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent event) {
                onClose();
            }
        });
    }

    public boolean isLegalDeckAndShowErrors(final MagicDeck deck, final String playerName) {
        final String brokenRulesText =
                MagicDeckConstructionRule.getRulesText(MagicDeckConstructionRule.checkDeck(deck));

        if (brokenRulesText.length() > 0) {
            ScreenController.showWarningMessage(String.format("%s\n\n%s", MText.get(_S3, playerName), brokenRulesText));
            return false;
        }

        return true;
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
            final String message = String.format("%s\n", MText.get(_S4));
            final Object[] params = {message};
            final int n = JOptionPane.showConfirmDialog(contentPanel,
                    params,
                    MText.get(_S5),
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

    private void doScreenshot() {
        try {
            final Path filePath = MagicFileSystem.getDataPath(DataPath.LOGS).resolve("screenshot.png");
            final File imageFile = ImageHelper.doScreenshotToFile(this.getContentPane(), filePath);
            DesktopHelper.openFileInDefaultOsEditor(imageFile);
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
        GeneralConfig.setIsMissingFiles(false);
        CardDefinitions.checkForMissingFiles();
        ThemeFactory.getInstance().loadThemes();
        refreshLookAndFeel();
    }

    public void setScreen(MScreen s) {
        contentPanel.setScreen(s);
    }

    private boolean replaceBackgroundImage(File newImage) {
        try {
            FileUtils.copyFile(newImage, MagicFileSystem.getBackgroundImageFile());
            return true;
        } catch (IOException ex) {
            ScreenController.showWarningMessage(String.format("%s\n\n%s",
                    MText.get(_S6),
                    ex.getMessage())
            );
        }
        return false;
    }

    private void doSetCustomBackgroundImage(File imageFile) {

        if (!ImageHelper.isValidImageFile(imageFile)) {
            ScreenController.showWarningMessage("Invalid image file.");
            return;
        }

        final String message = String.format("%s\n", MText.get("Replace background image?"));
        final Object[] params = {message};
        final int response = JOptionPane.showConfirmDialog(contentPanel,
                params,
                MText.get("Confirmation required..."),
                JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            if (replaceBackgroundImage(imageFile)) {
                GeneralConfig.set(BooleanSetting.CUSTOM_BACKGROUND, true);
                GeneralConfig.saveToFile();
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
        final MScreen screen = ScreenController.getActiveScreen();
        screen.setVisible(!screen.isVisible());
    }

    @Override
    public void onGameSnapshotDropped(File aFile) {
        MagicSystem.setIsTestGame(true);
        ScreenController.showDuelGameScreen(GameLoader.loadSavedGame(aFile));
    }

}
