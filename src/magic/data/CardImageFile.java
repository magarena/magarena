package magic.data;

import java.net.MalformedURLException;
import java.net.URL;
import magic.model.IRenderableCard;
import magic.model.MagicCardDefinition;
import magic.utility.MagicFileSystem;

public class CardImageFile extends DownloadableFile {

    private final MagicCardDefinition card;

    public CardImageFile(IRenderableCard face) throws MalformedURLException {
        super(
            MagicFileSystem.getPrintedCardImage(face),
            new URL(face.getImageUrl())
        );
        this.card = face.getCardDefinition();
    }

    public CardImageFile(final MagicCardDefinition aCard) throws MalformedURLException {
        super(
            MagicFileSystem.getPrintedCardImage(aCard),
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
