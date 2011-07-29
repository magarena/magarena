package magic.ui.theme;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class PlayerAvatar {

	private static final int LARGE_SIZE=120;
	private static final int MEDIUM_SIZE=LARGE_SIZE/2;
	private static final int SMALL_SIZE=LARGE_SIZE/4;

	private ImageIcon largeIcon = null;
	private ImageIcon mediumIcon = null;
	private ImageIcon smallIcon = null;
	
	public PlayerAvatar(final BufferedImage image) {
        try {
            final BufferedImage largeImage=magic.GraphicsUtilities.createCompatibleImage(image,LARGE_SIZE,LARGE_SIZE);
            largeIcon=new ImageIcon(largeImage);
            final BufferedImage mediumImage=magic.GraphicsUtilities.createCompatibleImage(image,MEDIUM_SIZE,MEDIUM_SIZE);
            mediumIcon=new ImageIcon(mediumImage);
            final BufferedImage smallImage=magic.GraphicsUtilities.createCompatibleImage(image,SMALL_SIZE,SMALL_SIZE);
            smallIcon=new ImageIcon(smallImage);
        } catch (final Throwable th) {
            System.err.println("WARNING. Unable to load player avatar");
        }
	}
	
	public ImageIcon getIcon(final int size) {
		switch (size) {
			case 2: return mediumIcon;
			case 3: return largeIcon;
			default: return smallIcon;
		}
	}
}
