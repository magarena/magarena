package magic.ui;

import java.awt.Dimension;
import magic.model.MagicCardDefinition;

import java.awt.image.BufferedImage;

/**
 * Interface for getting image of a card
 */
public interface CardImagesProvider {

    public static final Dimension MAXIMUM_CARD_SIZE = new Dimension(480, 680);
    public static final Dimension HIGH_QUALITY_IMAGE_SIZE = new Dimension(480, 680);

    BufferedImage getImage(
            final MagicCardDefinition cardDefinition,
            final int index,
            final boolean high);

    void clearCache();
}
