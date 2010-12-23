package magic.data;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import magic.MagicMain;
import magic.model.MagicCardDefinition;

public class DefaultCardImagesProvider implements CardImagesProvider {
	
	public static final int CARD_WIDTH=203;
	public static final int CARD_HEIGHT=289;
	public static final int CARD_FULL_WIDTH=223;
	public static final int CARD_FULL_HEIGHT=310;
	
	private static final DefaultCardImagesProvider INSTANCE=new DefaultCardImagesProvider();

	private final Map<MagicCardDefinition,BufferedImage[]> imagesMap;
	
	private DefaultCardImagesProvider() {
		
		imagesMap=new HashMap<MagicCardDefinition,BufferedImage[]>();
	}
	
	private String[] getFilenames(final MagicCardDefinition card) {

		final String filenames[]=new String[card.getImageCount()];
		final String imagePath=MagicMain.getGamePath()+File.separator+(card.isToken()?"tokens":"cards")+File.separator;
		final String imageFilename=card.getImageName();
		for (int index=0;index<card.getImageCount();index++) {
		
			filenames[index]=imagePath+imageFilename+(index>0?String.valueOf(index+1):"")+IMAGE_EXTENSION;
		}		
		return filenames;
	}
	
	private static BufferedImage loadCardImage(final String filename) {

		try {			
			final InputStream inputStream=new FileInputStream(filename);
			BufferedImage fullImage=ImageIO.read(inputStream);
			inputStream.close();
			// Rescale and save card image when dimension is not as expected.
			if (Math.abs(fullImage.getWidth()-CARD_FULL_WIDTH)>2) {
				System.out.println("Rescaling : "+filename);
				final Image scaledImage=fullImage.getScaledInstance(CARD_FULL_WIDTH,CARD_FULL_HEIGHT,Image.SCALE_SMOOTH);
				fullImage=new BufferedImage(CARD_FULL_WIDTH,CARD_FULL_HEIGHT,BufferedImage.TYPE_INT_RGB);
				fullImage.getGraphics().drawImage(scaledImage,0,0,null);
				final OutputStream outputStream=new FileOutputStream(filename);
				ImageIO.write(fullImage,"jpg",outputStream);
				outputStream.flush();
				outputStream.close();
			}
			return fullImage.getSubimage(10,10,CARD_FULL_WIDTH-20,CARD_FULL_HEIGHT-21);
		} catch (final Exception ex) {
			return IconImages.MISSING;
		}
	}
	
	public BufferedImage getImage(final MagicCardDefinition card,final int index) {
		
		if (card==null) {
			return IconImages.MISSING;
		}
		
		synchronized (imagesMap) {
			final BufferedImage images[]=imagesMap.get(card);
			if (images!=null) {
				return images[index%images.length];
			}
		}
		
		final String[] filenames=getFilenames(card);
		final BufferedImage images[]=new BufferedImage[filenames.length];
		for (int i=0;i<filenames.length;i++) {

			images[i]=loadCardImage(filenames[i]);
		}
		
		synchronized (imagesMap) {
			imagesMap.put(card,images);			
			return images[index%images.length];
		}
	}
	
	public static CardImagesProvider getInstance() {
		
		return INSTANCE;
	}
}