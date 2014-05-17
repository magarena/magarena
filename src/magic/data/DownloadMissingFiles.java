package magic.data;

import magic.MagicMain;
import magic.model.MagicCardDefinition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Download the necessary images and files from the WWW
 */
public class DownloadMissingFiles extends ArrayList<WebDownloader> {

    private static final long serialVersionUID = 1L;

    public DownloadMissingFiles(final String filename) {
        loadDownloadImageFiles(filename);
    }

    private void loadDownloadImageFiles(final String filename) {
        final InputStream stream;

        // download additional images
        if (filename.startsWith("file://")) {
            try { //create file input stream
                stream=new FileInputStream(filename.substring(7));
            } catch (final FileNotFoundException ex) {
                System.err.println("ERROR! Unable to find " + filename);
                return;
            }
        } else {
            stream=this.getClass().getResourceAsStream(filename);
        }

        String content = null;
        try { //load list of images
            content = FileIO.toStr(stream);
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to read " + filename);
            return;
        }

        final File gamePathFile=new File(MagicMain.getGamePath());
        File imagesPathFile = null;

        try (final Scanner sc = new Scanner(content)) {
            while (sc.hasNextLine()) {
                final String line = sc.nextLine();
                if (line.startsWith(">")) {
                    imagesPathFile=new File(gamePathFile,line.substring(1).trim());
                    if (!imagesPathFile.exists() && !imagesPathFile.mkdir()) {
                        System.err.println("WARNING. Unable to create " + imagesPathFile);
                    }
                } else {
                    final String[] parts=line.trim().split(";");
                    if (parts.length==2&&!parts[1].isEmpty()) {
                        final File imageFile=new File(imagesPathFile,parts[0]);

                        try { //create URL
                            final WebDownloader dl = new DownloadImageFile(imageFile,new URL(parts[1]));
                            if (!dl.exists()) {
                                add(dl);
                            }
                        } catch (final java.net.MalformedURLException ex) {
                            System.err.println("ERROR! URL malformed " + parts[1]);
                        }
                    }
                }
            }
        }

        // download card images.
        final File cardsPathFile=new File(gamePathFile, CardDefinitions.CARD_IMAGE_FOLDER);
        final File tokensPathFile = new File(gamePathFile, CardDefinitions.TOKEN_IMAGE_FOLDER);

        if (!tokensPathFile.exists() && !tokensPathFile.mkdir()) {
            System.err.println("WARNING. Unable to create " + tokensPathFile);
        }

        for (final MagicCardDefinition cardDefinition : CardDefinitions.getCards()) {
            // card image
            final String imageURL = cardDefinition.getImageURL();
            if (imageURL != null) {
                final File imageFile = cardDefinition.isToken()?
                    new File(tokensPathFile,
                             cardDefinition.getImageName() + CardDefinitions.CARD_IMAGE_EXT) :
                    new File(cardsPathFile,
                             cardDefinition.getImageName() + CardDefinitions.CARD_IMAGE_EXT);

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
