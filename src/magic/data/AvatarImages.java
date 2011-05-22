package magic.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import magic.MagicMain;
import magic.ui.theme.PlayerAvatar;

public class AvatarImages {

	private static final AvatarImages INSTANCE = new AvatarImages();
	
	private final File avatarPath;
	private final Vector<String> names;
	private String current = null;
	private PlayerAvatar avatars[];
	
	private AvatarImages() {
		
		avatarPath=new File(MagicMain.getGamePath(),"avatars");
		
		final File[] files=avatarPath.listFiles();
		names=new Vector<String>();
		if (files != null) {
            for (final File file : files) {
            
                if (file.isDirectory()) {
                    names.add(file.getName());
                }
            }
        }
	}
	
	public Vector<String> getNames() {
		
		return names;
	}
	
	private PlayerAvatar loadAvatar(final File file) {
		
		BufferedImage image;
		try {
			final InputStream stream=new FileInputStream(file);
			image=ImageIO.read(stream);	
			stream.close();
		} catch (final Exception ex) {
			image=IconImages.MISSING;
		}		
		return new PlayerAvatar(image);
	}
	
	private synchronized void loadAvatars() {
		
		final String avatar=GeneralConfig.getInstance().getAvatar();
		if (avatar!=current) {
			current=avatar;
			final File files[]=new File(avatarPath,current).listFiles();
			if (files!=null&&files.length>=2) {
				Arrays.sort(files);
				avatars=new PlayerAvatar[files.length];
				for (int index=0;index<files.length;index++) {
					
					avatars[index]=loadAvatar(files[index]);
				}		
			} else {
				avatars=new PlayerAvatar[2];
				avatars[0]=loadAvatar(new File(""));
				avatars[1]=loadAvatar(new File(""));
			}
		}
	}
	
	public ImageIcon getAvatarIcon(int index,final int size) {

		loadAvatars();
		if (index<0) {
			index=0;
		} else if (index>=avatars.length) {
			index%=avatars.length;
		}
		return avatars[index].getIcon(size);
	}
	
	public int getNumberOfAvatars() {

		loadAvatars();
		return avatars.length;
	}

	public static AvatarImages getInstance() {

		return INSTANCE;
	}
}
