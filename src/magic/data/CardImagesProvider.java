package magic.data;

import magic.model.MagicCardDefinition;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 * Interface for getting image of a card
 */
public interface CardImagesProvider {

    String IMAGE_EXTENSION=".jpg";

    //native resolution of images from mtgimage.com
    int CARD_WIDTH=480;
    int CARD_HEIGHT=680;

    Dimension CARD_DIMENSION = new Dimension(CARD_WIDTH, CARD_HEIGHT);

    BufferedImage getImage(
            final MagicCardDefinition cardDefinition,
            final int index,
            final boolean high);

    void clearCache();
}
