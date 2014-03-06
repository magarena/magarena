package magic.ui.theme;

import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;

public class PlayerAvatar {

    public static final int LARGE_SIZE  = 120;
    public static final int MEDIUM_SIZE = LARGE_SIZE/2;
    private static final int SMALL_SIZE  = LARGE_SIZE/4;
    public static final int CUSTOM_SIZE = 54;

    private ImageIcon largeIcon;
    private ImageIcon mediumIcon;
    private ImageIcon smallIcon;
    private ImageIcon turnIcon;
    private int face = 0;

    public PlayerAvatar(final BufferedImage image) {
        largeIcon  = new ImageIcon(magic.ui.utility.GraphicsUtilities.scale(
                     image,LARGE_SIZE,LARGE_SIZE));
        mediumIcon = new ImageIcon(magic.ui.utility.GraphicsUtilities.scale(
                     image,MEDIUM_SIZE,MEDIUM_SIZE));
        smallIcon  = new ImageIcon(magic.ui.utility.GraphicsUtilities.scale(
                     image,SMALL_SIZE,SMALL_SIZE));
        turnIcon   = new ImageIcon(magic.ui.utility.GraphicsUtilities.scale(
                     image,CUSTOM_SIZE,CUSTOM_SIZE));
    }

    public PlayerAvatar(final int face) {
        this.face = face;
    }

    public ImageIcon getIcon(final int size) {
        if (face > 0) {
            return ThemeFactory.getInstance().getCurrentTheme().getAvatarIcon(face, size);
        } else {
            switch (size) {
            case 1: return smallIcon;
            case 2: return mediumIcon;
            case 3: return largeIcon;
            case 4: return turnIcon;
            default: throw new RuntimeException("PlayerAvatar.getIcon: invalid size " + size);
            }
        }
    }
}
