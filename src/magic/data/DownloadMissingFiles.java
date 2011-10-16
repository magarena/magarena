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

        final Scanner sc = new Scanner(content);
		final File gamePathFile=new File(MagicMain.getGamePath());
        File imagesPathFile = null;

		while (sc.hasNextLine()) {
			final String line = sc.nextLine();
			if (line.startsWith(">")) {
				imagesPathFile=new File(gamePathFile,line.substring(1).trim());
				final boolean isCreated = imagesPathFile.mkdir();
                if (!isCreated) {
                    System.err.println("WARNING. Unable to create " + imagesPathFile);
                }
			} else {
				final String parts[]=line.trim().split(";");
				if (parts.length==2&&!parts[1].isEmpty()) {
					final File imageFile=new File(imagesPathFile,parts[0]);

                    //download if the file does not exist OR it is zero length
					if (!imageFile.exists() || imageFile.length() == 0L) {
                        try { //create URL
    						add(new DownloadImageFile(imageFile,new URL(parts[1])));
                        } catch (final java.net.MalformedURLException ex) {
                            System.err.println("ERROR! URL malformed " + parts[1]);
                        }
					}
				}
			}
		}
		
		// download card images and texts
		final File cardsPathFile=new File(gamePathFile, CardDefinitions.CARD_IMAGE_FOLDER);
		final File tokensPathFile = new File(gamePathFile, CardDefinitions.TOKEN_IMAGE_FOLDER);
		final File textPathFile = new File(gamePathFile, CardDefinitions.CARD_TEXT_FOLDER);
		
		if (!tokensPathFile.mkdir()) {
            System.err.println("WARNING. Unable to create " + tokensPathFile);
        }
        if (!textPathFile.mkdir()) {
            System.err.println("WARNING. Unable to create " + textPathFile);
        }
		
		for (final MagicCardDefinition cardDefinition : CardDefinitions.getInstance().getCards()) {
			// card image
			final String imageURL = cardDefinition.getImageURL();
			if (imageURL != null) {
				final File imageFile = cardDefinition.isToken()? 
					new File(tokensPathFile, cardDefinition.getImageName() + CardDefinitions.CARD_IMAGE_EXT) :
					new File(cardsPathFile, cardDefinition.getImageName() + CardDefinitions.CARD_IMAGE_EXT);

                //download if the file does not exists OR it is zero length OR it is outdated
				if (!imageFile.exists() || 
						imageFile.length() == 0L ||
						cardDefinition.isIgnored(imageFile.length())) {
                    try { //create URL
    					add(new DownloadImageFile(imageFile,new URL(imageURL)));
                    } catch (final java.net.MalformedURLException ex) {
                        System.err.println("ERROR! URL malformed " + imageURL);
                    }
				}
			}
			
			// card text
			final String textUrl = cardDefinition.getCardInfoURL();
			if (textUrl != null && textUrl.length() > 0) {
				final File textFile = new File(textPathFile, cardDefinition.getCardTextName() + CardDefinitions.CARD_TEXT_EXT);
				
				// download if the file does not exists OR it is zero length OR it is outdated
				if (!textFile.exists() || 
						textFile.length() == 0L ||
						cardDefinition.isIgnored(textFile.length())) {
                    try { // create URL
    					add(new DownloadCardTextFile(textFile, new URL(textUrl)));
                    } catch (final java.net.MalformedURLException ex) {
                        System.err.println("ERROR! URL malformed " + textUrl);
                    }
				}
			}
		}
	}
}
