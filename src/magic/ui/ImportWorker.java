package magic.ui;

import magic.translate.UiString;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import magic.data.CardDefinitions;
import magic.data.DuelConfig;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.model.player.PlayerProfiles;
import magic.utility.FileIO;
import magic.utility.MagicFileSystem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;

public class ImportWorker extends SwingWorker<Void, Void> {

    // translatable strings
    private static final String _S1 = "FAIL";
    private static final String _S2 = "- new cards snapshot...";
    private static final String _S3 = "OK";
    private static final String _S4 = "- themes...";
    private static final String _S5 = "- preferences...";
    private static final String _S6 = "- avatars...";
    private static final String _S7 = "- custom decks...";
    private static final String _S8 = "- player profiles...";
    private static final String _S9 = "- new duel settings...";
    private static final String _S10 = "- card images...";
    private static final String _S11 = "CANCELLED";

    private static final String OK_STRING = String.format("%s\n", UiString.get(_S3));

    private final Path importDataPath;
    private String progressNote = "";

    public ImportWorker(final File magarenaDirectory) {
        this.importDataPath = magarenaDirectory.toPath().resolve(MagicFileSystem.DATA_DIRECTORY_NAME);
    }

    @Override
    public Void doInBackground() throws IOException {
        if (!isCancelled()) { importPreferences(); }
        if (!isCancelled()) { importNewDuelConfig(); }
        if (!isCancelled()) { importPlayerProfiles(); }
        if (!isCancelled()) { importCustomDecks(); }
        if (!isCancelled()) { importAvatars(); }
        if (!isCancelled()) { importMods(); }
        if (!isCancelled()) { importCardImages(); }
        if (!isCancelled()) { updateNewCardsLog(); }
        return null;
    }

    @Override
    public void done() {
        try {
            get();
        } catch (InterruptedException | ExecutionException ex) {
            System.err.println(ex.getCause());
            setProgressNote(String.format("%s\n", UiString.get(_S1)));
        } catch (CancellationException ex) {
            // cancelled by user.
        }
        setProgressNote("");
        setProgress(0);
    }

    /**
     * Rebuilds the "newcards.log" file so that it contains all the new playable cards which
     * have been added since the imported and current versions.
     */
    private void updateNewCardsLog() {
        setProgressNote(UiString.get(_S2));
        setProgress(0);
        final File scriptsDirectory = this.importDataPath.resolve("scripts").toFile();
        final File[] scriptFiles = MagicFileSystem.getSortedScriptFiles(scriptsDirectory);
        final List<String> cards = new ArrayList<>();
        final int countMax = scriptFiles.length;
        int count = 0;
        for (final File file : scriptFiles) {
            final Properties content = FileIO.toProp(file);
            cards.add(content.getProperty("name"));
            count++;
            setProgress((int)((count / (double)countMax) * 100));

        }
        CardDefinitions.updateNewCardsLog(cards);
        setProgressNote(OK_STRING);
    }

    /**
     * Merges "mods" folder and sub-folders.
     * If file already exists then imported version takes precedence.
     */
    private void importMods() throws IOException {
        setProgressNote(UiString.get(_S4));
        final String directoryName = "mods";
        final Path sourcePath = importDataPath.resolve(directoryName);
        if (sourcePath.toFile().exists()) {
            final Path targetPath = MagicFileSystem.getDataPath().resolve(directoryName);
            FileUtils.copyDirectory(sourcePath.toFile(), targetPath.toFile(), getModsFileFilter());
        }
        setProgressNote(OK_STRING);
    }

    /**
     * Creates a filter that returns everything in the "mods" folder except
     * predefined cubes which are distributed with each new release.
     */
    private FileFilter getModsFileFilter() {
        final String[] excludedCubes = new String[]{
            "legacy_cube.txt", "modern_cube.txt", "standard_cube.txt", "extended_cube.txt", "ubeefx_cube.txt"
        };
        final IOFileFilter excludedFiles = new NameFileFilter(excludedCubes, IOCase.INSENSITIVE);
        final IOFileFilter excludeFilter = FileFilterUtils.notFileFilter(excludedFiles);
        return FileFilterUtils.or(DirectoryFileFilter.DIRECTORY, excludeFilter);
    }

    /**
     * Merges "general.cfg" file.
     * If setting already exists then imported value takes precedence.
     *
     */
    private void importPreferences() throws IOException {
        setProgressNote(UiString.get(_S5));

        final String CONFIG_FILENAME = "general.cfg";

        // Create new config file with default settings.
        final File thisConfigFile = MagicFileSystem.getDataPath().resolve(CONFIG_FILENAME).toFile();
        if (thisConfigFile.exists()) {
            thisConfigFile.delete();
        }
        GeneralConfig.getInstance().save();

        final Properties thisProperties = FileIO.toProp(thisConfigFile);
        final Properties theirProperties = FileIO.toProp(importDataPath.resolve(CONFIG_FILENAME).toFile());

        // list of latest config settings.
        final List<String> thisSettings = new ArrayList<>(thisProperties.stringPropertyNames());

        // not interested in importing these settings.
        thisSettings.removeAll(Arrays.asList(
                "left", 
                "fullScreen", 
                "top", 
                "height", 
                "width",
                "translation"));

        // import settings...
        for (String setting : thisSettings) {
            if (theirProperties.containsKey(setting)) {
                thisProperties.setProperty(setting, theirProperties.getProperty(setting));
            }
        }

        // save updated preferences and reload.
        FileIO.toFile(thisConfigFile, thisProperties, "General configuration");
        GeneralConfig.getInstance().load();
        
        setProgressNote(OK_STRING);
    }

    /**
     * Merges "avatars" folder and sub-folders.
     * If file already exists then imported version takes precedence.
     */
    private void importAvatars() throws IOException {
        setProgressNote(UiString.get(_S6));
        final String directoryName = "avatars";
        final Path sourcePath = importDataPath.resolve(directoryName);
        if (sourcePath.toFile().exists()) {
            final Path targetPath = MagicFileSystem.getDataPath().resolve(directoryName);
            FileUtils.copyDirectory(sourcePath.toFile(), targetPath.toFile());
        }
        setProgressNote(OK_STRING);
    }

    /**
     * Merges top level "decks" folder only.
     * Does not import sub-folders (prebuilt, firemind, etc).
     * If file already exists then imported version takes precedence.
     */
    private void importCustomDecks() throws IOException {
        setProgressNote(UiString.get(_S7));
        final String directoryName = "decks";
        final Path sourcePath = importDataPath.resolve(directoryName);
        if (sourcePath.toFile().exists()) {
            final Path targetPath = MagicFileSystem.getDataPath().resolve(directoryName);
            final IOFileFilter deckSuffixFilter = FileFilterUtils.suffixFileFilter(".dec");
            FileUtils.copyDirectory(sourcePath.toFile(), targetPath.toFile(), deckSuffixFilter);
        }
        setProgressNote(OK_STRING);
    }

    /**
     * Delete "players" folder and replace with imported copy.
     */
    private void importPlayerProfiles() throws IOException {
        setProgressNote(UiString.get(_S8));
        final String directoryName = "players";
        final Path targetPath = MagicFileSystem.getDataPath().resolve(directoryName);
        FileUtils.deleteDirectory(targetPath.toFile());
        final Path sourcePath = importDataPath.resolve(directoryName);
        if (sourcePath.toFile().exists()) {
            FileUtils.copyDirectory(sourcePath.toFile(), targetPath.toFile());
            PlayerProfiles.refreshMap();
        }
        setProgressNote(OK_STRING);
    }

    /**
     * Delete "duels" folder and replace with imported copy.
     */
    private void importNewDuelConfig() throws IOException {
        setProgressNote(UiString.get(_S9));
        final String directoryName = "duels";
        final Path targetPath = MagicFileSystem.getDataPath().resolve(directoryName);
        FileUtils.deleteDirectory(targetPath.toFile());
        final Path sourcePath = importDataPath.resolve(directoryName);
        if (sourcePath.toFile().exists()) {
            FileUtils.copyDirectory(sourcePath.toFile(), targetPath.toFile());
            DuelConfig.getInstance().load();
        }
        setProgressNote(OK_STRING);
    }

    /**
     * Copy card images only if imported version stores them
     * in the "Magarena\cards" and "Magarena\tokens" directories.
     */
    private void importCardImages() {

        setProgressNote(UiString.get(_S10));

        boolean isMissingFiles = false;

        if (GeneralConfig.getInstance().isCustomCardImagesPath() == false) {

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
                setProgress((int) ((loopCount / totalFiles) * 100));

                if (isCancelled()) {
                    setProgressNote(String.format("%s\n", UiString.get(_S11)));
                    return;
                }

            }

            // refresh
            magic.ui.CachedImagesProvider.getInstance().clearCache();

        }

        GeneralConfig.getInstance().setIsMissingFiles(isMissingFiles);

        setProgressNote(OK_STRING);

    }

    private void setProgressNote(final String progressNote) {
        if (getPropertyChangeSupport().hasListeners("progressNote")) {
            firePropertyChange("progressNote", this.progressNote, progressNote);
        }
        this.progressNote = progressNote;
    }

}
