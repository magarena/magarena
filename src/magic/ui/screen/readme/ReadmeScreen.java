package magic.ui.screen.readme;

import java.nio.file.Paths;

import magic.ui.mwidgets.MTextFileViewer;
import magic.ui.screen.HeaderFooterScreen;

@SuppressWarnings("serial")
public class ReadmeScreen extends HeaderFooterScreen {

    private final MTextFileViewer mainView = new MTextFileViewer();

    public ReadmeScreen() {
        super("README");
        setDefaultProperties();
        setMainContent(mainView.component());
    }

    private void setDefaultProperties() {
        mainView.setTextFile(Paths.get("README.txt"));
        mainView.setFileLinkVisible(false);
    }
}
