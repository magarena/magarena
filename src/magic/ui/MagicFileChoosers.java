package magic.ui;

import java.awt.Component;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import static magic.utility.DeckUtils.DECK_EXTENSION;
import magic.utility.MagicFileSystem;

public class MagicFileChoosers {
    private MagicFileChoosers() {}

    public static final FileFilter DECK_FILEFILTER = getFileFilter("Magarena deck", DECK_EXTENSION);
    public static final FileFilter SAVED_GAME_FILEFILTER = getFileFilter("Saved game", ".game");

    private static FileFilter getFileFilter(final String description, final String fileExtension) {
        return new FileFilter() {
            @Override
            public boolean accept(final File file) {
                return file.isDirectory() || file.getName().endsWith(fileExtension);
            }
            @Override
            public String getDescription() {
                return description;
            }
        };
    }

    public static File getSaveGameFile(final Component dialogParent) {
        final Path saveGamePath = MagicFileSystem.getDataPath(MagicFileSystem.DataPath.SAVED_GAMES);
        @SuppressWarnings("serial")
        final JFileChooser fileChooser = new JFileChooser(saveGamePath.toFile()) {
            @Override
            public void approveSelection() {
                // first ensure filename has "dec" extension
                String filename = getSelectedFile().getAbsolutePath();
                if (!filename.endsWith(".game")) {
                    setSelectedFile(new File(filename + ".game"));
                }
                if (Files.exists(getSelectedFile().toPath())) {
                    int response = JOptionPane.showConfirmDialog(
                            ScreenController.getFrame(),
                            "Overwrite existing saved game?",
                            "Overwrite file",
                            JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        super.approveSelection();
                    }
                } else {
                    super.approveSelection();
                }
            }
        };
        fileChooser.setDialogTitle("Save game");
        fileChooser.setFileFilter(SAVED_GAME_FILEFILTER);
        fileChooser.setAcceptAllFileFilterUsed(false);
//        if (deck != null) {
//            fileChooser.setSelectedFile(new File(deck.getFilename()));
//        }
        final int action = fileChooser.showSaveDialog(dialogParent);
        if (action == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

}
