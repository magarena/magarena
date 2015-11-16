package magic.ui.widget.downloader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.swing.SwingUtilities;
import magic.data.CardDefinitions;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;
import magic.ui.dialog.IImageDownloadListener;
import magic.utility.MagicFileSystem;

@SuppressWarnings("serial")
public class PlayableDownloadPanel extends MissingImagesDownloadPanel {

    // translatable strings
    private static final String _S1 = "Playable cards, missing images";
    private static final String _S2 = "Download new images";

    public PlayableDownloadPanel(IImageDownloadListener listener) {
        super(listener);
    }
    
    @Override
    protected String getProgressCaption() {
        return UiString.get(_S1);
    }

    @Override
    protected Collection<MagicCardDefinition> getCards() {
        assert !SwingUtilities.isEventDispatchThread();
        final Date lastDownloaderRunDate = GeneralConfig.getInstance().getPlayableImagesDownloadDate();
        final List<MagicCardDefinition> cards = new ArrayList<>();
        for (final MagicCardDefinition card : CardDefinitions.getAllPlayableCardDefs()) {
            if (card.getImageURL() != null) {
                if (!MagicFileSystem.getCardImageFile(card).exists() ||
                    card.isImageUpdatedAfter(lastDownloaderRunDate)) {
                    cards.add(card);
                }
            }
        }
        return cards;
    }

    @Override
    protected String getLogFilename() {
        return "downloads.log";
    }

    @Override
    protected String getDownloadButtonCaption() {
        return UiString.get(_S2);
    }

    @Override
    protected void doCustomActionAfterDownload(int errorCount) {
        if (errorCount == 0) {
            GeneralConfig.getInstance().setPlayableImagesDownloadDate(new Date());
            GeneralConfig.getInstance().save();
        }
    }
    
}
