package magic.ui.dialog;

import magic.MagicMain;
import magic.data.CardDefinitions;
import magic.data.DownloadMissingFiles;
import magic.data.DuelConfig;
import magic.data.FileIO;
import magic.data.GeneralConfig;
import magic.data.History;
import magic.data.WebDownloader;
import magic.model.player.PlayerProfiles;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import net.miginfocom.swing.MigLayout;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import javax.swing.AbstractAction;
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

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

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
        // Layout manager.
        setLayout(migLayout);
        // JTextArea
        taskOutput.setMargin(new Insets(5,5,5,5));
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
        migLayout.setLayoutConstraints("flowy");
        add(taskOutput, "w 100%, h 100%");
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
                if (!isCancelled()) { importPreferences(); };
                if (!isCancelled()) { importCardData(); }
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
        }

        private void importMods() throws IOException {
            setProgressNote("- themes...");
            final String directoryName = "mods";
            final Path sourcePath = dataPath.resolve(directoryName);
            if (sourcePath.toFile().exists()) {
                final Path targetPath = Paths.get(MagicMain.getGamePath()).resolve(directoryName);
                FileUtils.copyDirectory(sourcePath.toFile(), targetPath.toFile());
            }
            setProgressNote("OK\n");
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
                        new File(dataPath.toFile(), CardDefinitions.TOKEN_IMAGE_FOLDER),
                        new File(dataPath.toFile(), History.HISTORY_FOLDER)
                    };

                final DownloadMissingFiles files = new DownloadMissingFiles();
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

                GeneralConfig.getInstance().setIsMissingFiles(isMissingFiles);

                // refresh
                magic.data.HighQualityCardImagesProvider.getInstance().clearCache();

            }

            setProgressNote("OK\n" + (isMissingFiles ? "- New artwork is available to download.\n" : ""));

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
