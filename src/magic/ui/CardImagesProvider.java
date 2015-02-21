package magic.ui;

import magic.model.MagicCardDefinition;

import java.awt.image.BufferedImage;

/**
 * Interface for getting image of a card
 */
public interface CardImagesProvider {

    BufferedImage getImage(
            final MagicCardDefinition cardDefinition,
            final int index,
            final boolean high);

    void clearCache();
}
