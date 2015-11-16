package magic.ui.widget.downloader;

import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import magic.data.CardImageFile;
import magic.data.DownloadableFile;
import magic.model.MagicCardDefinition;
import magic.ui.dialog.IImageDownloadListener;
import magic.utility.MagicFileSystem;

@SuppressWarnings("serial")
public abstract class MissingImagesDownloadPanel extends ImageDownloadPanel {
    
    protected MissingImagesDownloadPanel(final IImageDownloadListener listener) {
        super(listener);
    }

    @Override
    protected String doFileDownloadAndGetName(final DownloadableFile file, final Proxy proxy) throws IOException {
        file.download(proxy);
        if (file instanceof CardImageFile) {
            return ((CardImageFile) file).getCardName();
        } else {
            return "";
        }
    }

    @Override
    protected int getCustomCount(int countInteger) {
        return countInteger;
    }

    protected static Collection<MagicCardDefinition> getCards(Collection<MagicCardDefinition> cards, Date aDate) {
        final List<MagicCardDefinition> downloads = new ArrayList<>();
        for (final MagicCardDefinition card : cards) {
            if (card.getImageURL() != null) {
                final boolean isImageMissing = MagicFileSystem.getCardImageFile(card).exists() == false;
                final boolean isImageUpdated = card.isImageUpdatedAfter(aDate);
                if (isImageMissing || isImageUpdated) {
                    downloads.add(card);
                }
            }
        }
        return downloads;
    }

}
