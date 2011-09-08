package magic.data;

import magic.model.MagicCardDefinition;

import java.awt.image.BufferedImage;

/** 
 * Interface for getting image of a card
 */
public interface CardImagesProvider {

	String IMAGE_EXTENSION=".jpg";

    //native resolution of images from magiccards.info
	int CARD_WIDTH=312;
	int CARD_HEIGHT=445;

	BufferedImage getImage(
            final MagicCardDefinition cardDefinition,
            final int index,
            final boolean high);

}
