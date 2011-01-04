package magic.ui.theme;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

import magic.MagicMain;
import magic.data.IconImages;

public class CustomTheme extends AbstractTheme {

	public CustomTheme() {
		
		super("custom");
	}
	
	static BufferedImage loadUserImage(final String name) {
		
		try {
			final FileInputStream inputStream=new FileInputStream(MagicMain.getGamePath()+File.separator+name);
			final BufferedImage image=ImageIO.read(inputStream);
			inputStream.close();
			return image;
		} catch (final Exception ex) {
			return IconImages.MISSING;
		}
	}
}