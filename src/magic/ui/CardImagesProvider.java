package magic.ui;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import magic.model.MagicCardDefinition;

/**
 * Interface for getting image of a card
 */
public interface CardImagesProvider {

    public static final Dimension MAXIMUM_CARD_SIZE = new Dimension(480, 680);
    public static final Dimension HIGH_QUALITY_IMAGE_SIZE = new Dimension(480, 680);
    public static final Dimension SMALL_SCREEN_IMAGE_SIZE = new Dimension(312, 445);

    // The most common size of card retrieved from http://mtgimage.com.
    public static final Dimension PREFERRED_CARD_SIZE = HIGH_QUALITY_IMAGE_SIZE;

    BufferedImage getImage(
            final MagicCardDefinition cardDefinition,
            final int index,
            final boolean high);

    void clearCache();
}
