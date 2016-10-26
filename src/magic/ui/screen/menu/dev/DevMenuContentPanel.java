package magic.ui.screen.menu.dev;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import magic.data.CardDefinitions;
import magic.data.MagicSetDefinitions;
import magic.game.state.GameLoader;
import magic.game.state.GameStateFileReader;
import magic.ui.ScreenController;
import magic.ui.dialog.GameStateRunner;
import magic.ui.screen.menu.MenuScreenContentPanel;
import magic.utility.MagicFileSystem;
import magic.utility.MagicSystem;

@SuppressWarnings("serial")
class DevMenuContentPanel extends MenuScreenContentPanel {

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

    DevMenuContentPanel() {
        super("DevMode Menu", true);
        addMenuItem("Load game", this::doLoadSavedGame);
        addMenuItem("Load test class", this::doLoadTestClass);
        addSpace();
        addMenuItem("Create missing cards file", "Creates CardsMissingInMagarena.txt for use with ScriptsBuilder.", this::doSaveMissingCardsFile);
        addMenuItem("Create card stats file", "Creates CardStatistics.txt to view current card completion.", this::doCreateSetStats);
        addSpace();
        addMenuItem("Test screen", this::showTestScreen);
        addSpace();
        addMenuItem("Main menu", this::onCloseMenu);
        refreshMenuLayout();
    }

    private void showTestScreen() {
        ScreenController.showTestScreen();
    }
    
    private void doLoadTestClass() {
        MagicSystem.setIsTestGame(true);
        new GameStateRunner();
    }
    
    private void onCloseMenu() {
        ScreenController.closeActiveScreen(false);
    }

    private void doLoadSavedGame() {
        MagicSystem.setIsTestGame(true);
        loadSavedGame();
    }
    
    private static File getSaveGameFile() {
        final JFileChooser fileChooser = new JFileChooser(MagicFileSystem.getDataPath().toFile());
        fileChooser.setDialogTitle("Load & resume saved game");
        fileChooser.setFileFilter(TEST_FILE_FILTER);
        fileChooser.setAcceptAllFileFilterUsed(false);
        // Add the description preview pane
        fileChooser.setAccessory(new DeckDescriptionPreview(fileChooser));
        final int action = fileChooser.showOpenDialog(ScreenController.getFrame());
        if (action == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    private void loadSavedGame() {
        final File file = getSaveGameFile();
        if (file != null) {
            ScreenController.showDuelGameScreen(GameLoader.loadSavedGame(file));
        }
    }

    private void saveMissingCardsList() throws IOException {
        final List<String> missingCards = CardDefinitions.getMissingCardNames();
        Collections.sort(missingCards);
        final Path savePath = MagicFileSystem.getDataPath(MagicFileSystem.DataPath.LOGS).resolve("CardsMissingInMagarena.txt");
        try (final PrintWriter writer = new PrintWriter(savePath.toFile())) {
            missingCards.forEach(writer::println);
        }
        Desktop.getDesktop().open(MagicFileSystem.getDataPath(MagicFileSystem.DataPath.LOGS).toFile());
    }

    private void doSaveMissingCardsFile() {
        try {
            saveMissingCardsList();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void doCreateSetStats() {
        try {
            MagicSetDefinitions.createSetStatistics();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
