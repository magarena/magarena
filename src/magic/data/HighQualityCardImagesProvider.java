package magic.data;

import java.awt.Image;
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

/**
 * For a given MagicCardDefinition object returns the corresponding image from
 * the cards directory
 */
public class HighQualityCardImagesProvider implements CardImagesProvider {
	
	private static final CardImagesProvider INSTANCE=new HighQualityCardImagesProvider();
	
	private static final int MAX_IMAGES=100;

	private final Map<String,BufferedImage> lowImagesMap;
	private final Map<String,BufferedImage> highImagesMap;
	private final LinkedList<String> retrievedList;
	
	public HighQualityCardImagesProvider() {

		lowImagesMap=new HashMap<String,BufferedImage>();
		highImagesMap=new HashMap<String,BufferedImage>();
		retrievedList=new LinkedList<String>();
	}
	
	private static final String getFilename(
            final MagicCardDefinition cardDefinition,
            final int index) {
		final int imageIndex=index%cardDefinition.getImageCount();
		final StringBuffer buffer=new StringBuffer();
		buffer.append(MagicMain.getGamePath()).append(File.separator);
		buffer.append(cardDefinition.isToken()?"tokens":"cards").append(File.separator);
		buffer.append(cardDefinition.getImageName())
        buffer.append(imageIndex>0?String.valueOf(imageIndex+1):"");
		buffer.append(IMAGE_EXTENSION);
		return buffer.toString();
	}
	
	private static BufferedImage loadCardImage(final String filename) {
		try {			
			final InputStream inputStream=new FileInputStream(filename);
			final BufferedImage fullImage=ImageIO.read(inputStream);
			inputStream.close();
			return fullImage;
		} catch (final Exception ex) {
			return IconImages.MISSING;
		}
	}
	
	@Override
	public BufferedImage getImage(
            final MagicCardDefinition cardDefinition,
            final int index,
            final boolean high) {

		if (cardDefinition == null) {
			return IconImages.MISSING;
		}

		final String filename=getFilename(cardDefinition,index);
		final Map<String,BufferedImage> map=high?highImagesMap:lowImagesMap;
		BufferedImage image=map.get(filename);

		if (image != null) {
			retrievedList.remove(filename);
			retrievedList.addLast(filename);
			return image;
		}

		if (retrievedList.size() >= MAX_IMAGES) {
			final String first=retrievedList.removeFirst();
			lowImagesMap.remove(first);
			highImagesMap.remove(first);
		}

		image=loadCardImage(filename);
		BufferedImage lowImage;
		if (image==IconImages.MISSING) {
			lowImage=image;
		} else {
			final Image scaledImage=image.getScaledInstance(
                    CARD_WIDTH,
                    CARD_HEIGHT,
                    Image.SCALE_SMOOTH);
			lowImage=new BufferedImage(
                    CARD_WIDTH,
                    CARD_HEIGHT,
                    BufferedImage.TYPE_INT_RGB);
			lowImage.getGraphics().drawImage(scaledImage,0,0,null);
		}
		lowImagesMap.put(filename,lowImage);
		highImagesMap.put(filename,image);
		retrievedList.addLast(filename);
		return high?image:lowImage;
	}
	
	public static CardImagesProvider getInstance() {
		return INSTANCE;
	}
}
