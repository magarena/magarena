package magic.data;

import java.awt.image.BufferedImage;
import magic.model.MagicCardDefinition;

/** 
 * Interface for getting image of a card
 */
public interface CardImagesProvider {

	public static final String IMAGE_EXTENSION=".jpg";
	public static final int CARD_WIDTH=312;
	public static final int CARD_HEIGHT=445;

	public BufferedImage getImage(
            final MagicCardDefinition cardDefinition,
            final int index,
            final boolean high);

}
