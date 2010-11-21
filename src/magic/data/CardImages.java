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

public class CardImages {
	
	public static final int CARD_WIDTH=203;
	public static final int CARD_HEIGHT=289;
	public static final int CARD_FULL_WIDTH=223;
	public static final int CARD_FULL_HEIGHT=310;
	
	private static final CardImages INSTANCE=new CardImages();

	private final Map<MagicCardDefinition,BufferedImage> imagesMap;
	
	private CardImages() {
		
		imagesMap=new HashMap<MagicCardDefinition,BufferedImage>();
	}
	
	private String getFilename(final MagicCardDefinition card) {
		
		return MagicMain.getGamePath()+File.separator+(card.isToken()?"tokens":"cards")+File.separator+card.getImageName();
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
	
	public BufferedImage getImage(final MagicCardDefinition card) {
		
		if (card==null) {
			return IconImages.MISSING;
		}
		
		synchronized (imagesMap) {
			final BufferedImage image=imagesMap.get(card);
			if (image!=null) {
				return image;
			}
		}
		
		final String filename=getFilename(card);
		final BufferedImage image=loadCardImage(filename);
		
		synchronized (imagesMap) {
			imagesMap.put(card,image);			
			return image;
		}
	}
	
	public static CardImages getInstance() {
		
		return INSTANCE;
	}
}