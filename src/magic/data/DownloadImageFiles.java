package magic.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import magic.MagicMain;
import magic.model.MagicCardDefinition;

/** 
 * Download the necessary images from the WWW
 */
public class DownloadImageFiles extends ArrayList<DownloadImageFile> {

	private static final long serialVersionUID = 1L;

	public DownloadImageFiles(final String filename) {
        loadDownloadImageFiles(filename);
	}	
	
	private void loadDownloadImageFiles(final String filename) {
		final InputStream stream;
		if (filename.startsWith("file://")) {
            try {
    			stream=new FileInputStream(filename.substring(7));
            } catch (final FileNotFoundException ex) {
                System.err.println("ERROR! Unale to find " + filename);
                return;
            }
		} else {
			stream=this.getClass().getResourceAsStream(filename);
		}

        String content = null;
        try {
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
					if (!imageFile.exists()) {
                        try {
    						add(new DownloadImageFile(imageFile,new URL(parts[1])));
                        } catch (final java.net.MalformedURLException ex) {
                            System.err.println("ERROR! URL malformed " + parts[1]);
                        }
					}
				}
			}
		}
		
		final File cardsPathFile=new File(gamePathFile,"cards");
		for (final MagicCardDefinition cardDefinition : CardDefinitions.getInstance().getCards()) {
			final String imageURL=cardDefinition.getImageURL();
			if (imageURL!=null) {
				final File imageFile=new File(cardsPathFile,cardDefinition.getImageName()+".jpg");
				if (!imageFile.exists()) {
                    try {
    					add(new DownloadImageFile(imageFile,new URL(imageURL)));
                    } catch (final java.net.MalformedURLException ex) {
                        System.err.println("ERROR! URL malformed " + imageURL);
                    }
				}
			}
		}
	}
}
