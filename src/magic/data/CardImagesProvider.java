package magic.data;

import magic.model.MagicCardDefinition;

import java.awt.image.BufferedImage;

/** 
 * Interface for getting image of a card
 */
public interface CardImagesProvider {

	public static final String IMAGE_EXTENSION=".jpg";

    //native resolution of images from magiccards.info
	public static final int CARD_WIDTH=312;
	public static final int CARD_HEIGHT=445;

	public BufferedImage getImage(
            final MagicCardDefinition cardDefinition,
            final int index,
            final boolean high);

}
