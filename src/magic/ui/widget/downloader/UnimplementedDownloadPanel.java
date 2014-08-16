package magic.ui.widget.downloader;

import java.util.Collection;
import javax.swing.SwingUtilities;
import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;

@SuppressWarnings("serial")
public class UnimplementedDownloadPanel extends MissingImagesDownloadPanel {

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

    @Override
    protected String getDownloadButtonCaption() {
        return "Download new images";
    }
}
