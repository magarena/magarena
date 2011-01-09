package magic.ui.theme;

import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class PlayerAvatar {

	private static final int LARGE_SIZE=120;
	private static final int MEDIUM_SIZE=LARGE_SIZE/2;
	private static final int SMALL_SIZE=LARGE_SIZE/4;

	private final ImageIcon largeIcon;
	private final ImageIcon mediumIcon;
	private final ImageIcon smallIcon;
	
	public PlayerAvatar(final BufferedImage image) {
		
		final Image largeImage=image.getScaledInstance(LARGE_SIZE,LARGE_SIZE,Image.SCALE_SMOOTH);
		largeIcon=new ImageIcon(largeImage);
		final Image mediumImage=image.getScaledInstance(MEDIUM_SIZE,MEDIUM_SIZE,Image.SCALE_SMOOTH);
		mediumIcon=new ImageIcon(mediumImage);
		final Image smallImage=image.getScaledInstance(SMALL_SIZE,SMALL_SIZE,Image.SCALE_SMOOTH);
		smallIcon=new ImageIcon(smallImage);
	}
	
	public ImageIcon getIcon(final int index,final int size) {

		switch (size) {
			case 2: return mediumIcon;
			case 3: return largeIcon;
			default: return smallIcon;
		}
	}
}