package magic.ui.widget.downloader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.SwingUtilities;
import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;
import magic.utility.MagicFileSystem;

@SuppressWarnings("serial")
public class PlayableDownloadPanel extends MissingImagesDownloadPanel {
    
    @Override
    protected String getProgressCaption() {
        return "Playable cards, missing images = ";
    }

    @Override
    protected Collection<MagicCardDefinition> getCards() {
        assert !SwingUtilities.isEventDispatchThread();
        final List<MagicCardDefinition> cards = new ArrayList<>();
        for (final MagicCardDefinition card : CardDefinitions.getAllPlayableCardDefs()) {
            if (card.getImageURL() != null) {
                if (!MagicFileSystem.getCardImageFile(card).exists()) {
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
        return "Download new images";
    }
}
