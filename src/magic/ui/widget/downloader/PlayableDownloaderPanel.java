package magic.ui.widget.downloader;

import java.util.Collection;
import javax.swing.SwingUtilities;
import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;

@SuppressWarnings("serial")
public class PlayableDownloaderPanel extends ImageDownloadPanel {
    
    @Override
    protected String getProgressCaption() {
        return "Playable cards, missing images = ";
    }

    @Override
    protected Collection<MagicCardDefinition> getCards() {
        assert !SwingUtilities.isEventDispatchThread();
        return CardDefinitions.getCards();
    }

    @Override
    protected String getLogFilename() {
        return "downloads.log";
    }

}
