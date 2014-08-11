package magic.ui.dialog;


import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import magic.MagicMain;
import magic.data.CardDefinitions;
import magic.data.DuelConfig;
import magic.data.FileIO;
import magic.data.GeneralConfig;
import magic.data.MissingImages;
import magic.data.WebDownloader;
import magic.model.player.PlayerProfiles;
import magic.ui.MagicFrame;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;

@SuppressWarnings("serial")
public class ImportDialog extends JDialog implements PropertyChangeListener {

    // ui components
    private final MigLayout migLayout = new MigLayout();
    private final JTextArea taskOutput = new JTextArea(5, 20);
    private final JProgressBar progressBar = new JProgressBar();
    private final JButton importButton = new JButton("Import...");
    private final JButton cancelButton = new JButton("Cancel");

    // properties
    private ImportCardDataWorker importWorker;
    private final JFrame frame;
    private boolean importCardImages = true;

    // CTR
    public ImportDialog(final JFrame frame) {
        super(frame, true);
        this.frame = frame;
        setLookAndFeel(frame);
        refreshLayout(false);
        setActions();
        setVisible(true);
    }

    private void setActions() {

        // actions
        final AbstractAction cancelAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doCancelImportAndClose();
            }
        };
        final AbstractAction importAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doImport();
            }
        };

        // assign actions to buttons.
        importButton.addActionListener(importAction);
        cancelButton.addActionListener(cancelAction);

        // assign actions to keys.
        setKeyStrokeAction("escapeKeyAction", KeyEvent.VK_ESCAPE, cancelAction);

    }

    private void setKeyStrokeAction(final String keyAction, final int keyEvent, final AbstractAction action) {
        final JRootPane root = getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keyEvent, 0), keyAction);
        root.getActionMap().put(keyAction, action);
    }

    private void setLookAndFeel(final JFrame frame) {
        // dialog frame
        setTitle("Import");
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 400);
        setLocationRelativeTo(frame);
        setUndecorated(true);
        ((JComponent)getContentPane()).setBorder(BorderFactory.createMatteBorder(8, 8, 8, 8, ThemeFactory.getInstance().getCurrentTheme().getColor(Theme.COLOR_TITLE_BACKGROUND)));
        // Layout manager.
        setLayout(migLayout);
        // JTextArea
        taskOutput.setEditable(false);
        taskOutput.setEnabled(false);
        taskOutput.setDisabledTextColor(taskOutput.getForeground());
        taskOutput.setFont(FontsAndBorders.FONT1);
        taskOutput.append("This will import the following data from\n");
        taskOutput.append("an existing Magarena directory.\n\n");
        taskOutput.append("- Player profiles & stats.\n");
        taskOutput.append("- New duel configuration settings.\n");
        taskOutput.append("- Custom decks.\n");
        taskOutput.append("- Avatar images.\n");
        taskOutput.append("- Themes.\n");
        taskOutput.append("- Preferences.\n");
        taskOutput.append("- Downloaded card & token images.");
        // JProgressBar
        progressBar.setIndeterminate(false);
        progressBar.setValue(0);
        // Cancel JButton
        cancelButton.setFocusable(false);
    }

    private void refreshLayout(final boolean isImporting) {
        getContentPane().removeAll();
        migLayout.setLayoutConstraints("flowy, insets 2");
        add(taskOutput, "w 10:100%, h 100%");
        add(isImporting ? progressBar : importButton, "w 100%, h " + importButton.getPreferredSize().height + "!");
        add(cancelButton, "w 100%");
        revalidate();
    }

    private void doCancelImportAndClose() {
        if (importWorker != null) {
            importWorker.cancel(false);
        }
        dispose();
    }

    private void doImport() {
        final JFileChooser fileChooser = new MagarenaDirectoryChooser(getDefaultImportDirectory().toString());
        final int action = fileChooser.showOpenDialog(MagicMain.rootFrame);
        if (action==JFileChooser.APPROVE_OPTION) {
            taskOutput.setText("Importing...\n\n");
            importWorker = new ImportCardDataWorker(fileChooser.getSelectedFile());
            importWorker.addPropertyChangeListener(this);
            importWorker.execute();
            refreshLayout(true);
        }
    }

    private static Path getDefaultImportDirectory() {
        return Paths.get(MagicMain.getGamePath()).getParent().getParent();
    }

    private class ImportCardDataWorker extends SwingWorker<Void, Void> {

        private final Path dataPath;
        private String progressNote = "";

        public ImportCardDataWorker(final File magarenaDirectory) {
            this.dataPath = magarenaDirectory.toPath().resolve("Magarena");
        }

        @Override
        public Void doInBackground() {
            try {
                if (!isCancelled()) { importNewDuelConfig(); }
                if (!isCancelled()) { importPlayerProfiles(); }
                if (!isCancelled()) { importCustomDecks(); }
                if (!isCancelled()) { importAvatars(); }
                if (!isCancelled()) { importMods(); }
                // order is important.
                if (!isCancelled()) { importPreferences(); }
                if (!isCancelled()) { importCardData(); }
                if (!isCancelled()) { updateNewCardsLog(); }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        @Override
        public void done() {
            try {
                get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            } catch (CancellationException e2) {
                // cancelled by user.
            }
            setProgressNote("");
            setProgress(0);
            taskOutput.append("\nImport complete.");
            ((MagicFrame)frame).refreshUI();
        }

        /**
         * Rebuilds the "newcards.log" file so that it contains all the new playable cards
         * which have been added since the imported and current versions.
         */
        private void updateNewCardsLog() {
            final File scriptsDirectory = this.dataPath.resolve("scripts").toFile();
            final File[] scriptFiles = CardDefinitions.getSortedScriptFiles(scriptsDirectory);
            final List<String> cards = new ArrayList<>();
            for (final File file : scriptFiles) {
                final Properties content = FileIO.toProp(file);
                cards.add(content.getProperty("name"));
            }
            CardDefinitions.updateNewCardsLog(cards);
        }

        private void importMods() throws IOException {
            setProgressNote("- themes...");
            final String directoryName = "mods";
            final Path sourcePath = dataPath.resolve(directoryName);
            if (sourcePath.toFile().exists()) {
                final Path targetPath = Paths.get(MagicMain.getGamePath()).resolve(directoryName);
                FileUtils.copyDirectory(sourcePath.toFile(), targetPath.toFile(), getModsFileFilter());
            }
            setProgressNote("OK\n");
        }

        /**
         * Creates a filter that returns everything in the "mods" folder except
         * predefined cubes which are distributed with each new release.
         */
        private FileFilter getModsFileFilter() {
            final String[] excludedCubes = new String[] {
                "legacy_cube.txt", "modern_cube.txt", "standard_cube.txt", "extended_cube.txt", "ubeefx_cube.txt"
            };
            final IOFileFilter excludedFiles = new NameFileFilter(excludedCubes, IOCase.INSENSITIVE);
            final IOFileFilter excludeFilter = FileFilterUtils.notFileFilter(excludedFiles);
            return FileFilterUtils.orFileFilter(DirectoryFileFilter.DIRECTORY, excludeFilter);            
        }

        private void importPreferences() throws IOException {
            setProgressNote("- preferences...");
            final String CONFIG_FILENAME = "general.cfg";
            final File generalConfigFile = new File(MagicMain.getGamePath(), CONFIG_FILENAME);
            final Properties p1 = FileIO.toProp(dataPath.resolve(CONFIG_FILENAME).toFile());
            GeneralConfig.getInstance().save(); // ensure the config file exists.
            final Properties p2 = FileIO.toProp(generalConfigFile);
            // defined list of property keys to be import.
            final List<String> keys = new ArrayList<String>(p2.stringPropertyNames());
            final List<String> excludedKeys = Arrays.asList("left", "fullScreen", "top", "height", "width");
            keys.removeAll(excludedKeys);
            // update preferences.
            for (String key : keys) {
                if (p1.containsKey(key)) {
                    if (!p1.getProperty(key).equals(p2.getProperty(key))) {
                        p2.setProperty(key, p1.getProperty(key));
                    }
                    if (key.equals("cardImagesPath")) {
                        importCardImages = (p1.getProperty(key).isEmpty());
                    }
                }
            }
            // save updated preferences.
            FileIO.toFile(generalConfigFile, p2, "General configuration");
            // refresh
            GeneralConfig.getInstance().load();
            ThemeFactory.getInstance().setCurrentTheme(GeneralConfig.getInstance().getTheme());
            frame.repaint();
            setProgressNote("OK\n");
        }

        private void importAvatars() throws IOException {
            setProgressNote("- avatars...");
            final String directoryName = "avatars";
            final Path sourcePath = dataPath.resolve(directoryName);
            if (sourcePath.toFile().exists()) {
                final Path targetPath = Paths.get(MagicMain.getGamePath()).resolve(directoryName);
                FileUtils.copyDirectory(sourcePath.toFile(), targetPath.toFile());
            }
            setProgressNote("OK\n");
        }

        private void importCustomDecks() throws IOException {
            setProgressNote("- custom decks...");
            final String directoryName = "decks";
            final Path sourcePath = dataPath.resolve(directoryName);
            if (sourcePath.toFile().exists()) {
                final Path targetPath = Paths.get(MagicMain.getGamePath()).resolve(directoryName);
                final IOFileFilter deckSuffixFilter = FileFilterUtils.suffixFileFilter(".dec");
                FileUtils.copyDirectory(sourcePath.toFile(), targetPath.toFile(), deckSuffixFilter);
            }
            setProgressNote("OK\n");
        }

        private void importPlayerProfiles() throws IOException {
            setProgressNote("- player profiles...");
            final String directoryName = "players";
            final Path targetPath = Paths.get(MagicMain.getGamePath()).resolve(directoryName);
            FileUtils.deleteDirectory(targetPath.toFile());
            final Path sourcePath = dataPath.resolve(directoryName);
            if (sourcePath.toFile().exists()) {
                FileUtils.copyDirectory(sourcePath.toFile(), targetPath.toFile());
                PlayerProfiles.refreshMap();
            }
            setProgressNote("OK\n");
        }

        private void importNewDuelConfig() throws IOException {
            setProgressNote("- new duel settings...");
            final String directoryName = "duels";
            final Path targetPath = Paths.get(MagicMain.getGamePath()).resolve(directoryName);
            FileUtils.deleteDirectory(targetPath.toFile());
            final Path sourcePath = dataPath.resolve(directoryName);
            if (sourcePath.toFile().exists()) {
                FileUtils.copyDirectory(sourcePath.toFile(), targetPath.toFile());
                DuelConfig.getInstance().load();
            }
            setProgressNote("OK\n");
        }

        private void importCardData() {

            setProgressNote("- card & token images...");

            boolean isMissingFiles = false;
            if (importCardImages) {

                final File[] oldDirs = {
                        new File(dataPath.toFile(), CardDefinitions.CARD_IMAGE_FOLDER),
                        new File(dataPath.toFile(), CardDefinitions.TOKEN_IMAGE_FOLDER)
                    };

                final MissingImages files = new MissingImages(CardDefinitions.getAllCards());
                final double totalFiles = files.size();
                int loopCount = 0;

                for (final WebDownloader file : files) {

                    //check if file is in previous version
                    for (final File oldDir : oldDirs) {
                        final File oldFile = new File(oldDir, file.getFilename());
                        if (oldFile.exists()) {
                            try {
                                FileUtils.copyFile(oldFile, file.getFile());
                                break;
                            } catch (IOException ex) {
                                System.err.println("Unable to copy " + oldFile);
                            }
                        } else {
                            isMissingFiles = true;
                        }
                    }

                    loopCount++;
                    setProgress((int)((loopCount / totalFiles) * 100));

                    if (isCancelled()) {
                        setProgressNote("CANCELLED\n");
                        return;
                    }

                }

                // refresh
                magic.data.HighQualityCardImagesProvider.getInstance().clearCache();

            }

            GeneralConfig.getInstance().setIsMissingFiles(isMissingFiles);

            setProgressNote("OK\n" + (isMissingFiles ? "- New card images are available to download.\n" : ""));

        }

        public String getProgressNote() {
            return progressNote;
        }

        public void setProgressNote(final String progressNote) {
            if (getPropertyChangeSupport().hasListeners("progressNote")) {
                firePropertyChange("progressNote", this.progressNote, progressNote);
            }
            this.progressNote = progressNote;
            setProgress(0);
        }

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equalsIgnoreCase("progress")) {
            final int progressValue = (int)evt.getNewValue();
            progressBar.setIndeterminate(progressValue == 0 && importWorker.getProgressNote() != "");
            progressBar.setValue(progressValue);
        } else if (evt.getPropertyName().equalsIgnoreCase("state")) {
            progressBar.setIndeterminate(!evt.getNewValue().toString().equalsIgnoreCase("done"));
        } else if (evt.getPropertyName().equalsIgnoreCase("progressNote")) {
            taskOutput.append(evt.getNewValue().toString());
        }
    }

    private static class MagarenaDirectoryChooser extends JFileChooser {
        public MagarenaDirectoryChooser(String currentDirectoryPath) {
            super(currentDirectoryPath);
            setDialogTitle("Select existing Magarena directory");
            setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            setAcceptAllFileFilterUsed(false);
        }
        @Override
        public void approveSelection() {
            final Path importPath = getSelectedFile().toPath();
            if (verifyImportPath(importPath)) {
                super.approveSelection();
            } else {
                JOptionPane.showMessageDialog(
                        MagicMain.rootFrame,
                        "<html><b>Magarena not found!</b><br>This directory does not contain a valid version of Magarena.",
                        "Invalid Magarena Directory",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
        private boolean verifyImportPath(final Path importPath) {
            return importPath.resolve("Magarena.exe").toFile().exists() &&
                   importPath.resolve("Magarena").toFile().exists() &&
                   !importPath.resolve("Magarena").equals(Paths.get(MagicMain.getGamePath()));
        }
    }

}
