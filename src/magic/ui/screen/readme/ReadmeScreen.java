package magic.ui.screen.readme;

import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JPanel;
import magic.ui.screen.TextFileReaderScreen;
import magic.ui.screen.interfaces.IStatusBar;

@SuppressWarnings("serial")
public class ReadmeScreen extends TextFileReaderScreen implements IStatusBar {

    private static final Path TEXT_FILE = Paths.get("README.txt");

    public ReadmeScreen() {
        setTextFile(TEXT_FILE);
    }

    @Override
    protected String reprocessFileContents(String fileContent) {
        return fileContent;
    }

    @Override
    public String getScreenCaption() {
        return TEXT_FILE.getFileName().toString();
    }

    @Override
    public JPanel getStatusPanel() {
        return null;
    }

}
