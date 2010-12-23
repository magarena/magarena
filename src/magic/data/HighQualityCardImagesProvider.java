package magic.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.imageio.ImageIO;

import magic.MagicMain;
import magic.model.MagicCardDefinition;

public class HighQualityCardImagesProvider implements CardImagesProvider {
	
	private static final CardImagesProvider INSTANCE=new HighQualityCardImagesProvider();
	
	private static final int MAX_IMAGES=50;

	private final Map<String,BufferedImage> imagesMap;
	private final LinkedList<String> retrievedList;
	
	public HighQualityCardImagesProvider() {
	
		imagesMap=new HashMap<String,BufferedImage>();
		retrievedList=new LinkedList<String>();
	}
	
	private static final String getFilename(final MagicCardDefinition cardDefinition,final int index) {

		final int imageIndex=index%cardDefinition.getImageCount();
		final StringBuffer buffer=new StringBuffer();
		buffer.append(MagicMain.getGamePath()).append(File.separator);
		buffer.append(cardDefinition.isToken()?"tokens":"hqcards").append(File.separator);
		buffer.append(cardDefinition.getImageName()).append(imageIndex>0?String.valueOf(imageIndex+1):"");
		buffer.append(IMAGE_EXTENSION);
		return buffer.toString();
	}
	
	private static BufferedImage loadCardImage(final String filename) {

		try {			
			final InputStream inputStream=new FileInputStream(filename);
			BufferedImage fullImage=ImageIO.read(inputStream);
			inputStream.close();
			return fullImage;
		} catch (final Exception ex) {
			return IconImages.MISSING;
		}
	}
	
	@Override
	public BufferedImage getImage(final MagicCardDefinition cardDefinition,final int index) {

		final String filename=getFilename(cardDefinition,index);
		BufferedImage image=imagesMap.get(filename);
		if (image!=null) {
			retrievedList.remove(filename);
			retrievedList.addLast(filename);
			return image;
		}
		if (retrievedList.size()>=MAX_IMAGES) {
			imagesMap.remove(retrievedList.removeFirst());
		}
		image=loadCardImage(filename);
		imagesMap.put(filename,image);
		retrievedList.addLast(filename);		
		return image;
	}
	
	public static CardImagesProvider getInstance() {
		
		return INSTANCE;
	}
}