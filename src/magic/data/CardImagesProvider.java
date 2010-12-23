package magic.data;

import java.awt.image.BufferedImage;

import magic.model.MagicCardDefinition;

public interface CardImagesProvider {

	public static final String IMAGE_EXTENSION=".jpg";
	
	public BufferedImage getImage(final MagicCardDefinition cardDefinition,final int index);
}