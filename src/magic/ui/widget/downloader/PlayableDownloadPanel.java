package magic.ui.widget.downloader;

import java.util.Collection;
import java.util.Date;
import magic.data.CardDefinitions;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;
import magic.ui.dialog.IImageDownloadListener;

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
        return MissingImagesDownloadPanel.getCards(
            CardDefinitions.getAllPlayableCardDefs(),
            GeneralConfig.getInstance().getPlayableImagesDownloadDate()
        );
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
    protected void doCustomActionAfterDownload() {
        GeneralConfig.getInstance().setPlayableImagesDownloadDate(new Date());
        GeneralConfig.getInstance().save();
    }
    
}
