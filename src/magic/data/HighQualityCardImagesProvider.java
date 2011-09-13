package magic.data;

import magic.MagicMain;
import magic.model.MagicCardDefinition;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

/**
 * For a given MagicCardDefinition object returns the corresponding image from
 * the cards directory
 */
public class HighQualityCardImagesProvider implements CardImagesProvider {
	
	private static final CardImagesProvider INSTANCE=new HighQualityCardImagesProvider();

    private static final int MAX_IMAGES=100;
	private final Map<String,BufferedImage> scaledImages = 
        new magic.data.LRUCache<String,BufferedImage>(MAX_IMAGES);
	private final Map<String,BufferedImage> origImages = 
        new magic.data.LRUCache<String,BufferedImage>(MAX_IMAGES);
	
	private HighQualityCardImagesProvider() {}
	
	private static final String getFilename(
            final MagicCardDefinition cardDefinition,
            final int index) {
		final int imageIndex=index%cardDefinition.getImageCount();
		final StringBuilder buffer=new StringBuilder();
		buffer.append(MagicMain.getGamePath()).append(File.separator);
		buffer.append(cardDefinition.isToken()?"tokens":"cards").append(File.separator);
		buffer.append(cardDefinition.getImageName());
        buffer.append(imageIndex>0?String.valueOf(imageIndex+1):"");
		buffer.append(IMAGE_EXTENSION);
		return buffer.toString();
	}
	
	private static BufferedImage loadCardImage(final String filename) {
        return FileIO.toImg(new File(filename), IconImages.MISSING_CARD);
	}
	
	@Override
	public BufferedImage getImage(
            final MagicCardDefinition cardDefinition,
            final int index,
            final boolean orig) {
		try {
			if (cardDefinition == MagicCardDefinition.UNKNOWN) {
				return IconImages.MISSING_CARD;
			}

			final String filename=getFilename(cardDefinition,index);

			if (!origImages.containsKey(filename)) {
				origImages.put(filename, loadCardImage(filename));
			}

			if (!scaledImages.containsKey(filename)) {
				scaledImages.put(filename, 
					magic.GraphicsUtilities.scale(
						origImages.get(filename), 
						CARD_WIDTH, 
						CARD_HEIGHT
					)
				);
			}

			return orig ? origImages.get(filename) : scaledImages.get(filename);
		} catch (Exception e) {
			System.err.println("Error loading image. Got exception: "
                        + e.getMessage());
			return IconImages.MISSING_CARD;
		}
	}
	
	public static CardImagesProvider getInstance() {
		return INSTANCE;
	}
}
