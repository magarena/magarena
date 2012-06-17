package magic.ui.theme;

import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;

public class PlayerAvatar {

    private static final int LARGE_SIZE  = 120;
    private static final int MEDIUM_SIZE = LARGE_SIZE/2;
    private static final int SMALL_SIZE  = LARGE_SIZE/4;

    private final ImageIcon largeIcon;
    private final ImageIcon mediumIcon;
    private final ImageIcon smallIcon;
    
    public PlayerAvatar(final BufferedImage image) {
        largeIcon  = new ImageIcon(magic.GraphicsUtilities.scale(
                     image,LARGE_SIZE,LARGE_SIZE));
        mediumIcon = new ImageIcon(magic.GraphicsUtilities.scale(
                     image,MEDIUM_SIZE,MEDIUM_SIZE));
        smallIcon  = new ImageIcon(magic.GraphicsUtilities.scale(
                     image,SMALL_SIZE,SMALL_SIZE));
    }
    
    public ImageIcon getIcon(final int size) {
        switch (size) {
            case 1: return smallIcon;
            case 2: return mediumIcon;
            case 3: return largeIcon;
            default: throw new RuntimeException("PlayerAvatar.getIcon: invalid size " + size);
        }
    }
}
