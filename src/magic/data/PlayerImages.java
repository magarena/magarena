package magic.data;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import magic.MagicMain;

public class PlayerImages {

	private static final PlayerImages INSTANCE=new PlayerImages();	

	private final File files[];
	private final int nrOfImages;
	private final boolean[] loaded;
	private final ImageIcon[] largeImages;
	private final ImageIcon[] mediumImages;
	private final ImageIcon[] smallImages;
	
	private PlayerImages() {

		files=new File(MagicMain.getGamePath()+File.separator+"avatars").listFiles();
		nrOfImages=files==null?2:Math.max(2,files.length);
		loaded=new boolean[nrOfImages];
		largeImages=new ImageIcon[nrOfImages];
		mediumImages=new ImageIcon[nrOfImages];
		smallImages=new ImageIcon[nrOfImages];
	}
	
	public int getNrOfImages() {
		
		return nrOfImages;
	}
	
	public List<Integer> getImageIndices() {
		
		final List<Integer> indices=new ArrayList<Integer>();
		for (int index=0;index<nrOfImages;index++) {
			
			indices.add(index);
		}
		return indices;
	}
	
	private void loadIcon(final int index) {
		
		loaded[index]=true;
		BufferedImage image;
		try {
			final InputStream stream=new FileInputStream(files[index]);
			image=ImageIO.read(stream);	
			stream.close();
		} catch (final Exception ex) {
			image=IconImages.MISSING;
		}		
		largeImages[index]=new ImageIcon(image);
		final Image mediumImage=image.getScaledInstance(image.getWidth()/2,image.getHeight()/2,Image.SCALE_SMOOTH);
		mediumImages[index]=new ImageIcon(mediumImage);
		final Image smallImage=image.getScaledInstance(image.getWidth()/4,image.getHeight()/4,Image.SCALE_SMOOTH);
		smallImages[index]=new ImageIcon(smallImage);
	}
	
	public synchronized ImageIcon getIcon(final int index,final int size) {

		if (!loaded[index]) {
			loadIcon(index);
		}
		switch (size) {
			case 2: return mediumImages[index];
			case 3: return largeImages[index];
			default: return smallImages[index];
		}
	}
	
	public static PlayerImages getInstance() {
		
		return INSTANCE;
	}
}