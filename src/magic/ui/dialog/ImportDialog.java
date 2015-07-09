package magic.ui.dialog;


import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.Proxy;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import magic.data.CardDefinitions;
import magic.data.DownloadableFile;
import magic.data.DuelConfig;
import magic.utility.FileIO;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.model.player.PlayerProfiles;
import magic.ui.MagicFrame;
import magic.ui.ScreenController;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.MagicDownload;
import magic.utility.MagicFileSystem;
import magic.ui.utility.MagicStyle;
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
//    private final JCheckBox highQualityCheckBox = new JCheckBox();

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

    private JLabel getDialogCaptionLabel() {
        final JLabel lbl = new JLabel(getTitle());
        lbl.setOpaque(true);
        lbl.setBackground(MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND));
        lbl.setForeground(MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_FOREGROUND));
        lbl.setFont(FontsAndBorders.FONT1.deriveFont(14f));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        return lbl;
    }

    private void setLookAndFeel(final JFrame frame) {
        // dialog frame
        setTitle("Import");
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(380, 460);
        setLocationRelativeTo(frame);
        setUndecorated(true);
        ((JComponent)getContentPane()).setBorder(BorderFactory.createMatteBorder(0, 8, 8, 8, MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND)));
        // Layout manager.
        setLayout(migLayout);
        // JTextArea
        taskOutput.setEditable(false);
        taskOutput.setEnabled(false);
        taskOutput.setDisabledTextColor(taskOutput.getForeground());
        taskOutput.setFont(FontsAndBorders.FONT1);
        taskOutput.setBackground(getBackground());
        taskOutput.setLineWrap(true);
        taskOutput.setWrapStyleWord(true);
        taskOutput.append("This will import the following data from an existing Magarena directory...\n\n");
        taskOutput.append("- Player profiles & stats.\n");
        taskOutput.append("- New duel configuration settings.\n");
        taskOutput.append("- Custom decks.\n");
        taskOutput.append("- Avatar images.\n");
        taskOutput.append("- Themes.\n");
        taskOutput.append("- Preferences.\n");
        taskOutput.append("- Downloaded card & token images.\n\n");
//        taskOutput.append("Low Quality Image Updater\n");
//        taskOutput.append("There are likely to be a number of low quality card images in the imported collection of downloaded images. To check whether any new higher quality images have since been released tick the checkbox below and they will be downloaded during the import process if found.");
        // JProgressBar
        progressBar.setIndeterminate(false);
        progressBar.setValue(0);
        // Cancel JButton
        cancelButton.setFocusable(false);
        // low quality image updater
//        highQualityCheckBox.setText("Run low quality image updater");
//        highQualityCheckBox.setSelected(false);
    }

    private void refreshLayout(final boolean isImporting) {
        getContentPane().removeAll();
        migLayout.setLayoutConstraints("flowy, gapy 0, insets 0");
        add(getDialogCaptionLabel(), "w 100%, h 26!");
        add(taskOutput, "w 10:100%, h 100%");
//        if (!isImporting) { add(getOptionsPanel(), "w 100%, gaptop 10, gapleft 4"); }
        add(isImporting ? progressBar : importButton, "w 100%, h " + importButton.getPreferredSize().height + "!, gaptop 10");
        add(cancelButton, "w 100%");
        revalidate();
    }

//    private JPanel getOptionsPanel() {
//        final JPanel panel = new JPanel(new MigLayout("insets 2"));
//        panel.add(highQualityCheckBox, "w 100%");
//        return panel;
//    }

    private void doCancelImportAndClose() {
        if (importWorker != null) {
            importWorker.cancel(false);
        }
        dispose();
    }

    private void doImport() {
        final JFileChooser fileChooser = new MagarenaDirectoryChooser(getDefaultImportDirectory().toString());
        final int action = fileChooser.showOpenDialog(ScreenController.getMainFrame());
        if (action==JFileChooser.APPROVE_OPTION) {
            taskOutput.setText("Importing...\n\n");
            importWorker = new ImportCardDataWorker(fileChooser.getSelectedFile());
            importWorker.addPropertyChangeListener(this);
            importWorker.execute();
            refreshLayout(true);
        }
    }

    /**
     * Should return the directory containing the current installation of Magarena.
     * <p>
     * The idea being that a new version of Magarena would most likely be
     * installed to a new directory at the same level as the previous version,
     * so it would display the previous version all ready to select & import.
     */
    private static Path getDefaultImportDirectory() {
        final Path p = MagicFileSystem.getDataPath().getParent().getParent();
        if (p == null) {
            // triggered if using a single relative path for -Dmagarena.dir.
            return MagicFileSystem.getDataPath().getParent();
        }
        return p;
    }

    private class ImportCardDataWorker extends SwingWorker<Void, Void> {

        private final Path importDataPath;
        private String progressNote = "";

        public ImportCardDataWorker(final File magarenaDirectory) {
            this.importDataPath = magarenaDirectory.toPath().resolve(MagicFileSystem.DATA_DIRECTORY_NAME);
        }

        @Override
        public Void doInBackground() throws IOException {
            if (!isCancelled()) { importNewDuelConfig(); }
            if (!isCancelled()) { importPlayerProfiles(); }
            if (!isCancelled()) { importCustomDecks(); }
            if (!isCancelled()) { importAvatars(); }
            if (!isCancelled()) { importMods(); }
            // order is important from this point onwards.
            if (!isCancelled()) { importPreferences(); }
            if (!isCancelled()) { importCardData(); }
            if (!isCancelled()) { updateNewCardsLog(); }
//            if (!isCancelled()) { updateLowQualityCardImages(); }
            return null;
        }

        @Override
        public void done() {
            try {
                get();
            } catch (InterruptedException | ExecutionException ex) {
                System.err.println(ex.getCause().getMessage());
                taskOutput.append("  !!! ERROR - See console for details !!!\n");
            } catch (CancellationException e2) {
                // cancelled by user.
            }
            setProgressNote("");
            setProgress(0);
            taskOutput.append("\nImport complete.");
            ((MagicFrame)frame).refreshUI();
        }

//        private void updateLowQualityCardImages() throws IOException {
//            if (highQualityCheckBox.isSelected()) {
//                setProgressNote("- Scanning for low quality images...");
//                final List<MagicCardDefinition> cards = MagicDownload.getLowQualityImageCards();
//                setProgressNote(cards.size() + " found.\n");
//                setProgressNote("- Downloading high quality images (if available)...");
//                final ImagesDownloadList downloads = new ImagesDownloadList(cards);
//                final int downloadCount = doDownloadHighQualityImages(downloads, GeneralConfig.getInstance().getProxy());
//                setProgressNote(downloadCount + "\n");
//            }
//        }

        /**
         * Rebuilds the "newcards.log" file so that it contains all the new playable cards
         * which have been added since the imported and current versions.
         */
        private void updateNewCardsLog() {
            setProgressNote("- Card Explorer new cards snapshot...");
            final File scriptsDirectory = this.importDataPath.resolve("scripts").toFile();
            final File[] scriptFiles = MagicFileSystem.getSortedScriptFiles(scriptsDirectory);
            final List<String> cards = new ArrayList<>();
            for (final File file : scriptFiles) {
                final Properties content = FileIO.toProp(file);
                cards.add(content.getProperty("name"));
            }
            CardDefinitions.updateNewCardsLog(cards);
            setProgressNote("OK\n");
        }

        private void importMods() throws IOException {
            setProgressNote("- themes...");
            final String directoryName = "mods";
            final Path sourcePath = importDataPath.resolve(directoryName);
            if (sourcePath.toFile().exists()) {
                final Path targetPath = MagicFileSystem.getDataPath().resolve(directoryName);
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
            return FileFilterUtils.or(DirectoryFileFilter.DIRECTORY, excludeFilter);            
        }

        private void importPreferences() throws IOException {
            setProgressNote("- preferences...");
            final String CONFIG_FILENAME = "general.cfg";
            final File generalConfigFile = MagicFileSystem.getDataPath().resolve(CONFIG_FILENAME).toFile();
            final Properties p1 = FileIO.toProp(importDataPath.resolve(CONFIG_FILENAME).toFile());
            GeneralConfig.getInstance().save(); // ensure the config file exists.
            final Properties p2 = FileIO.toProp(generalConfigFile);
            // defined list of property keys to be import.
            final List<String> keys = new ArrayList<>(p2.stringPropertyNames());
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
            final Path sourcePath = importDataPath.resolve(directoryName);
            if (sourcePath.toFile().exists()) {
                final Path targetPath = MagicFileSystem.getDataPath().resolve(directoryName);
                FileUtils.copyDirectory(sourcePath.toFile(), targetPath.toFile());
            }
            setProgressNote("OK\n");
        }

        private void importCustomDecks() throws IOException {
            setProgressNote("- custom decks...");
            final String directoryName = "decks";
            final Path sourcePath = importDataPath.resolve(directoryName);
            if (sourcePath.toFile().exists()) {
                final Path targetPath = MagicFileSystem.getDataPath().resolve(directoryName);
                final IOFileFilter deckSuffixFilter = FileFilterUtils.suffixFileFilter(".dec");
                FileUtils.copyDirectory(sourcePath.toFile(), targetPath.toFile(), deckSuffixFilter);
            }
            setProgressNote("OK\n");
        }

        private void importPlayerProfiles() throws IOException {
            setProgressNote("- player profiles...");
            final String directoryName = "players";
            final Path targetPath = MagicFileSystem.getDataPath().resolve(directoryName);
            FileUtils.deleteDirectory(targetPath.toFile());
            final Path sourcePath = importDataPath.resolve(directoryName);
            if (sourcePath.toFile().exists()) {
                FileUtils.copyDirectory(sourcePath.toFile(), targetPath.toFile());
                PlayerProfiles.refreshMap();
            }
            setProgressNote("OK\n");
        }

        private void importNewDuelConfig() throws IOException {
            setProgressNote("- new duel settings...");
            final String directoryName = "duels";
            final Path targetPath = MagicFileSystem.getDataPath().resolve(directoryName);
            FileUtils.deleteDirectory(targetPath.toFile());
            final Path sourcePath = importDataPath.resolve(directoryName);
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
                        new File(importDataPath.toFile(), MagicFileSystem.CARD_IMAGE_FOLDER),
                        new File(importDataPath.toFile(), MagicFileSystem.TOKEN_IMAGE_FOLDER)
                    };

                final List<MagicCardDefinition> cards = CardDefinitions.getAllCards();
                final double totalFiles = cards.size();
                int loopCount = 0;

                for (final MagicCardDefinition card : cards) {

                    //check if file is in previous version
                    for (final File oldDir : oldDirs) {
                        final File newFile = MagicFileSystem.getCardImageFile(card);
                        final File oldFile = new File(oldDir, newFile.getName());
                        if (oldFile.exists()) {
                            try {
                                FileUtils.copyFile(oldFile, newFile);
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
                magic.ui.CachedImagesProvider.getInstance().clearCache();

            }

            GeneralConfig.getInstance().setIsMissingFiles(isMissingFiles);

            setProgressNote("OK\n" + (isMissingFiles ? "- New card images are available to download.\n" : ""));

        }

        private int doDownloadHighQualityImages(final Collection<DownloadableFile> downloadList, final Proxy proxy) throws IOException {
            int errorCount = 0;
            int totalCount = downloadList.size();
            int fileCount = 0;
            int imageSizeChangedCount = 0;
            for (DownloadableFile downloadableFile : downloadList) {
                try {
                    if (MagicDownload.isRemoteFileDownloadable(downloadableFile)) {
                        imageSizeChangedCount += MagicDownload.doDownloadImageFile(downloadableFile, proxy);
                    }
                } catch (IOException ex) {
                    final String msg = ex.toString() + " [" + downloadableFile.getFilename() + "]";
                    System.err.println(msg);
                }
                fileCount++;
                if (isCancelled()) {
                    break;
                } else {
                    setProgress((int)(((double)fileCount / totalCount) * 100));
                }

            }
            magic.ui.CachedImagesProvider.getInstance().clearCache();
            return imageSizeChangedCount;
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
            progressBar.setIndeterminate(progressValue == 0 && !"".equals(importWorker.getProgressNote()));
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
                ScreenController.showWarningMessage("<html><b>Magarena not found!</b><br>This directory does not contain a valid version of Magarena.");
            }
        }
        private boolean verifyImportPath(final Path importPath) {
            return importPath.resolve("Magarena.exe").toFile().exists() &&
                   importPath.resolve("Magarena").toFile().exists() &&
                   !importPath.resolve("Magarena").equals(MagicFileSystem.getDataPath());
        }
    }

}
