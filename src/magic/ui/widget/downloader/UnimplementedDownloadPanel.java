package magic.ui.widget.downloader;

import java.util.Date;
import java.util.stream.Stream;
import magic.data.CardDefinitions;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;
import magic.ui.dialog.IImageDownloadListener;

@SuppressWarnings("serial")
public class UnimplementedDownloadPanel extends MissingImagesDownloadPanel {

    // translatable strings
    private static final String _S1 = "Unimplemented cards, missing images";
    private static final String _S2 = "Download new images";

    public UnimplementedDownloadPanel(IImageDownloadListener listener) {
        super(listener);
    }

    @Override
    protected String getProgressCaption() {
        return UiString.get(_S1);
    }

    @Override
    protected Stream<MagicCardDefinition> getCards() {
        return MissingImagesDownloadPanel.getCards(
            CardDefinitions.getMissingCards(),
            GeneralConfig.getInstance().getMissingImagesDownloadDate()
        );
    }

    @Override
    protected String getLogFilename() {
        return "unimplemented.log";
    }

    @Override
    protected String getDownloadButtonCaption() {
        return UiString.get(_S2);
    }

    @Override
    protected void doCustomActionAfterDownload() {
        GeneralConfig.getInstance().setMissingImagesDownloadDate(new Date());
        GeneralConfig.getInstance().save();
    }

}
