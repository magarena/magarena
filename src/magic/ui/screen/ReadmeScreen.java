package magic.ui.screen;

import java.nio.file.Paths;

@SuppressWarnings("serial")
public class ReadmeScreen extends TextFileReaderScreen {

    public ReadmeScreen() {
        super(Paths.get("README.txt"));
    }

}
