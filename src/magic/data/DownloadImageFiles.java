package magic.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import magic.MagicMain;
import magic.model.MagicCardDefinition;

/** 
 * Download the necessary images from the WWW
 */
public class DownloadImageFiles extends ArrayList<DownloadImageFile> {

	private static final long serialVersionUID = 1L;

	public DownloadImageFiles(final String filename) {
		try {
			loadDownloadImageFiles(filename);
		} catch (final Exception ex) {
            System.err.println("ERROR! Unable to download images");
        }
	}	
	
	private void loadDownloadImageFiles(final String filename) throws IOException {
		
		final InputStream stream;
		if (filename.startsWith("file://")) {
			stream=new FileInputStream(filename.substring(7));
		} else {
			stream=this.getClass().getResourceAsStream(filename);
		}
		final BufferedReader reader=new BufferedReader(new InputStreamReader(stream));
		final File gamePathFile=new File(MagicMain.getGamePath());
		File imagesPathFile=null;

		while (true) {
			
			final String line=reader.readLine();
			if (line==null) {
				break;
			}
			if (line.startsWith(">")) {
				imagesPathFile=new File(gamePathFile,line.substring(1).trim());
				final boolean isCreated = imagesPathFile.mkdir();
                if (!isCreated) {
                    System.err.println("ERROR! Unable to create " + imagesPathFile);
                }
			} else {
				final String parts[]=line.trim().split(";");
				if (parts.length==2&&!parts[1].isEmpty()) {
					final File imageFile=new File(imagesPathFile,parts[0]);
					if (!imageFile.exists()) {
						add(new DownloadImageFile(imageFile,new URL(parts[1])));
					}
				}
			}
		}
		
		reader.close();
		
		final File cardsPathFile=new File(gamePathFile,"cards");
		for (final MagicCardDefinition cardDefinition : CardDefinitions.getInstance().getCards()) {
			final String imageURL=cardDefinition.getImageURL();
			if (imageURL!=null) {
				final File imageFile=new File(cardsPathFile,cardDefinition.getImageName()+".jpg");
				if (!imageFile.exists()) {
					add(new DownloadImageFile(imageFile,new URL(imageURL)));
				}
			}
		}
	}
}
