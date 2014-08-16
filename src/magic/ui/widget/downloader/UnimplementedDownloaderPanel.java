package magic.ui.widget.downloader;

import magic.ui.widget.downloader.ImageDownloadPanel;
import java.util.Collection;
import javax.swing.SwingUtilities;
import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;

@SuppressWarnings("serial")
public class UnimplementedDownloaderPanel extends ImageDownloadPanel {

    @Override
    protected String getProgressCaption() {
        return "Unimplemented cards, missing images = ";
    }

    @Override
    protected Collection<MagicCardDefinition> getCards() {
        assert !SwingUtilities.isEventDispatchThread();
        return CardDefinitions.getMissingCards();
    }

    @Override
    protected String getLogFilename() {
        return "unimplemented.log";
    }

}
