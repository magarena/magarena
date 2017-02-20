package magic.ui.screen.images.download;

import java.util.Date;
import java.util.stream.Stream;
import magic.data.CardDefinitions;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.translate.MText;

@SuppressWarnings("serial")
class PlayablePanel extends DownloadPanel {

    // translatable strings
    private static final String _S1 = "Missing images for playable cards";
    private static final String _S2 = "Download";

    PlayablePanel(CardImageDisplayMode aMode, DialogMainPanel aPanel) {
        super(aMode, aPanel);
    }

    @Override
    protected String getProgressCaption() {
        return MText.get(_S1);
    }

    @Override
    public Stream<MagicCardDefinition> getCards(final CardImageDisplayMode mode) {
        return DownloadPanel.getCards(
                CardDefinitions.getAllPlayableCardDefs(),
                GeneralConfig.getInstance().getPlayableImagesDownloadDate(),
                mode
            );
    }

    @Override
    protected String getDownloadButtonCaption() {
        return MText.get(_S2);
    }

    @Override
    public void doCustomActionAfterDownload() {
        GeneralConfig.getInstance().setPlayableImagesDownloadDate(new Date());
        GeneralConfig.getInstance().save();
    }

}
