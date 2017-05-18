package magic.ui.screen.cardflow;

import java.awt.image.BufferedImage;

public interface ICardFlowProvider {
    BufferedImage getImage(int index);
    int getImagesCount();
    default int getStartImageIndex() { return 0; }

    /**
     * Duration in milliseconds to scroll one card.
     */
    default long getAnimationDuration() { return 500L; }
}
