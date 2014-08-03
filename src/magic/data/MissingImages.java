package magic.data;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import magic.model.MagicCardDefinition;

@SuppressWarnings("serial")
public class MissingImages extends ArrayList<WebDownloader> {

    private final Collection<MagicCardDefinition> cards;

    public MissingImages(final Collection<MagicCardDefinition> cards) {
        this.cards = cards;
        loadDownloadImageFiles();
    }

    private void loadDownloadImageFiles() {
        final File cardImagesPath = GeneralConfig.getInstance().getCardImagesPath().toFile();

        final File cardsPathFile = new File(cardImagesPath, CardDefinitions.CARD_IMAGE_FOLDER);
        if (!cardsPathFile.exists() && !cardsPathFile.mkdir()) {
            System.err.println("WARNING. Unable to create " + cardsPathFile);
        }

        final File tokensPathFile = new File(cardImagesPath, CardDefinitions.TOKEN_IMAGE_FOLDER);
        if (!tokensPathFile.exists() && !tokensPathFile.mkdir()) {
            System.err.println("WARNING. Unable to create " + tokensPathFile);
        }

        for (final MagicCardDefinition cardDefinition : cards) {
            process(cardDefinition, cardsPathFile, tokensPathFile);
        }
        process(MagicCardDefinition.FACE_DOWN, cardsPathFile, tokensPathFile);

        Collections.sort(this, new Comparator<WebDownloader>() {
            @Override
            public int compare(WebDownloader o1, WebDownloader o2) {
                return o1.getFilename().compareTo(o2.getFilename());
            }
        });

    }
        
    private void process(final MagicCardDefinition cardDefinition, final File cardsPathFile, final File tokensPathFile) {
        final String imageURL = cardDefinition.getImageURL();
        if (imageURL != null) {
            final String imageFilename = cardDefinition.getImageName() + CardDefinitions.CARD_IMAGE_EXT;
            final File imageFile = new File(cardDefinition.isToken() ? tokensPathFile : cardsPathFile, imageFilename);
            try { //create URL
                final WebDownloader dl = new DownloadImageFile(imageFile, new URL(imageURL), cardDefinition);
                if (!dl.exists()) {
                    add(dl);
                }
            } catch (final java.net.MalformedURLException ex) {
                System.err.println("ERROR! URL malformed " + imageURL);
            }
        }
    }
}
