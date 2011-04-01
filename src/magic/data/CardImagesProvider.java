package magic.data;

import java.awt.image.BufferedImage;

import magic.model.MagicCardDefinition;

public interface CardImagesProvider {

	public static final String IMAGE_EXTENSION=".jpg";
	public static final int CARD_WIDTH=203;
	public static final int CARD_HEIGHT=289;

	public BufferedImage getImage(final MagicCardDefinition cardDefinition,final int index,final boolean high);
}