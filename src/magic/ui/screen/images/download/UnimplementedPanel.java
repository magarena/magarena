package magic.ui.screen.images.download;

import java.util.Date;
import java.util.stream.Stream;
import magic.data.CardDefinitions;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;
import magic.ui.CardTextLanguage;

@SuppressWarnings("serial")
class UnimplementedPanel extends DownloadPanel {

    // translatable strings
    private static final String _S1 = "Unimplemented";
    private static final String _S2 = "Download";

    UnimplementedPanel(DownloadMode aMode, CardTextLanguage aLang, DialogMainPanel aPanel) {
        super(aMode, aLang, aPanel);
    }

    @Override
    protected String getProgressCaption() {
        return UiString.get(_S1);
    }

    @Override
    public Stream<MagicCardDefinition> getCards(final DownloadMode mode) {
        return DownloadPanel.getCards(
                CardDefinitions.getMissingCards(),
                GeneralConfig.getInstance().getUnimplementedImagesDownloadDate(),
                mode
            );
    }

    @Override
    protected String getDownloadButtonCaption() {
        return UiString.get(_S2);
    }

    @Override
    public void doCustomActionAfterDownload() {
        GeneralConfig.getInstance().setUnimplementedImagesDownloadDate(new Date());
        GeneralConfig.getInstance().save();
    }

}
