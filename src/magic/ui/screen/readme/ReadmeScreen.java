package magic.ui.screen.readme;

import java.nio.file.Paths;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.widget.M.MTextFileViewer;

@SuppressWarnings("serial")
public class ReadmeScreen extends HeaderFooterScreen {

    private final MTextFileViewer mainView;

    public ReadmeScreen() {
        super("README");
        this.mainView = new MTextFileViewer();
        mainView.setTextFile(Paths.get("README.txt"));
        mainView.setFileLinkVisible(false);
        setMainContent(mainView.component());
    }
}
