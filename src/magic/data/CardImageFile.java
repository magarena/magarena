package magic.data;

import java.net.MalformedURLException;
import java.net.URL;
import magic.model.MagicCardDefinition;
import magic.utility.MagicFileSystem;

public class CardImageFile extends DownloadableFile {

    private final MagicCardDefinition card;

    public CardImageFile(final MagicCardDefinition aCard) throws MalformedURLException {
        super(
            MagicFileSystem.getCardImageFile(aCard),
            new URL(aCard.getImageURL())
        );
        this.card = aCard;
    }

    public String getCardName() {
        return card.getDistinctName();
    }

    public MagicCardDefinition getCard() {
        return card;
    }

}
