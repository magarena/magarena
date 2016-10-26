package magic.ui;

import magic.translate.MText;
import java.nio.file.Path;
import javax.swing.JFileChooser;
import magic.utility.MagicFileSystem;

@SuppressWarnings("serial")
public class MagarenaDirectoryChooser extends JFileChooser {

    private static final String _S1 = "Select existing Magarena directory";
    private static final String _S2 = "Magarena not found!";
    private static final String _S3 = "This directory does not contain a valid version of Magarena.";

    public MagarenaDirectoryChooser(Path currentDirectoryPath) {
        super(currentDirectoryPath.toFile());
        setDialogTitle(MText.get(_S1));
        setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        setAcceptAllFileFilterUsed(false);
    }

    public MagarenaDirectoryChooser() {
        this(MagicFileSystem.getDefaultImportDirectory());
    }

    @Override
    public void approveSelection() {
        final Path importPath = getSelectedFile().toPath();
        if (verifyImportPath(importPath)) {
            super.approveSelection();
        } else {
            ScreenController.showWarningMessage(String.format("<html><b>%s</b><br>%s</html>",
                            MText.get(_S2),
                            MText.get(_S3))
            );
        }
    }

    private boolean verifyImportPath(final Path importPath) {
        return importPath.resolve("Magarena.exe").toFile().exists()
                && importPath.resolve("Magarena").toFile().exists()
                && !importPath.resolve("Magarena").equals(MagicFileSystem.getDataPath());
    }

}
