package magic.data;

import magic.model.MagicCardDefinition;

import java.awt.image.BufferedImage;

/**
 * Interface for getting image of a card
 */
public interface CardImagesProvider {

    String IMAGE_EXTENSION=".jpg";

    BufferedImage getImage(
            final MagicCardDefinition cardDefinition,
            final int index,
            final boolean high);

    void clearCache();
}
