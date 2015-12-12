package magic.data;

import java.net.MalformedURLException;
import java.net.URL;
import magic.model.MagicCardDefinition;
import magic.utility.MagicFileSystem;

public class CardImageFile extends DownloadableFile {

    private final String cardName;

    public CardImageFile(final MagicCardDefinition cdef) throws MalformedURLException {
        super(
            MagicFileSystem.getCardImageFile(cdef),
            new URL(cdef.getImageURL())
        );
        cardName = cdef.getDistinctName();
    }

    public String getCardName() {
        return cardName;
    }

}
