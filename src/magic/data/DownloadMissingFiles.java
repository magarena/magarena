package magic.data;

import magic.model.MagicCardDefinition;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
 * Download the necessary images and files from the WWW
 */
public class DownloadMissingFiles extends ArrayList<WebDownloader> {

    private static final long serialVersionUID = 1L;

    public DownloadMissingFiles() {
        loadDownloadImageFiles();
    }

    private void loadDownloadImageFiles() {

        final File cardImagesPath = GeneralConfig.getInstance().getCardImagesPath().toFile();

        final File cardsPathFile=new File(cardImagesPath, CardDefinitions.CARD_IMAGE_FOLDER);
        if (!cardsPathFile.exists() && !cardsPathFile.mkdir()) {
            System.err.println("WARNING. Unable to create " + cardsPathFile);
        }

        final File tokensPathFile = new File(cardImagesPath, CardDefinitions.TOKEN_IMAGE_FOLDER);
        if (!tokensPathFile.exists() && !tokensPathFile.mkdir()) {
            System.err.println("WARNING. Unable to create " + tokensPathFile);
        }

        for (final MagicCardDefinition cardDefinition : CardDefinitions.getCards()) {
            final String imageURL = cardDefinition.getImageURL();
            if (imageURL != null) {
                final String imageFilename = cardDefinition.getImageName() + CardDefinitions.CARD_IMAGE_EXT;
                final File imageFile = new File(cardDefinition.isToken() ? tokensPathFile : cardsPathFile, imageFilename);
                try { //create URL
                    final WebDownloader dl = new DownloadImageFile(imageFile,new URL(imageURL),cardDefinition);
                    if (!dl.exists()) {
                        add(dl);
                    }
                } catch (final java.net.MalformedURLException ex) {
                    System.err.println("ERROR! URL malformed " + imageURL);
                }
            }
        }
    }
}
