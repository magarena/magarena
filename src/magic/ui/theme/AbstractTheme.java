package magic.ui.theme;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import magic.MagicMain;
import magic.data.IconImages;
import magic.ui.widget.FontsAndBorders;

public abstract class AbstractTheme implements Theme {

	private final String name;
	private final Map<String,Object> themeMap;
	private final List<File> avatarFiles;
	private final PlayerAvatar avatars[];
	
	public AbstractTheme(final String name) {
		
		this.name=name;
		themeMap=new HashMap<String,Object>();
		avatarFiles=new ArrayList<File>();
		avatars=loadAvatars(avatarFiles);
		
		addToTheme(TEXTURE_LOGO,null);
		
		addToTheme(ICON_LIFE,IconImages.LIFE);
		addToTheme(ICON_PREVENT,IconImages.PREVENT2);
		addToTheme(ICON_LAND,IconImages.LAND2);
		addToTheme(ICON_HAND,IconImages.HAND2);
		addToTheme(ICON_LIBRARY,IconImages.LIBRARY2);
		addToTheme(ICON_GRAVEYARD,IconImages.GRAVEYARD2);
		addToTheme(ICON_MESSAGE,IconImages.LOG);
		addToTheme(ICON_SMALL_BATTLEFIELD,IconImages.ALL);
		addToTheme(ICON_SMALL_COMBAT,IconImages.COMBAT);
		addToTheme(ICON_SMALL_STACK,IconImages.SPELL);
		addToTheme(ICON_SMALL_HAND,IconImages.HAND);
		addToTheme(ICON_SMALL_GRAVEYARD,IconImages.GRAVEYARD);
		addToTheme(ICON_SMALL_EXILE,IconImages.EXILE);
		
		addToTheme(COLOR_TITLE_FOREGROUND,Color.WHITE);
		addToTheme(COLOR_TITLE_BACKGROUND,new Color(0x23,0x6B,0x8E));
		addToTheme(COLOR_ICON_BACKGROUND,FontsAndBorders.GRAY3);
		addToTheme(COLOR_CHOICE_FOREGROUND,Color.BLUE);
		addToTheme(COLOR_COMMON_FOREGROUND,Color.BLACK);
		addToTheme(COLOR_UNCOMMON_FOREGROUND,new Color(0x8C,0x78,0x53));
		addToTheme(COLOR_RARE_FOREGROUND,new Color(0xCD,0x7F,0x32));
		addToTheme(COLOR_CHOICE,new Color(0,250,0,70));
		addToTheme(COLOR_COMBAT_CHOICE,new Color(250,130,0,125));
		addToTheme(COLOR_SELECTED_PLAYER,Color.RED);
		addToTheme(COLOR_GAME_BORDER,Color.BLACK);
		
		addToTheme(VALUE_SPACING,0);
		addToTheme(VALUE_BACKGROUND_STRETCH,0);
		addToTheme(VALUE_GAME_LAYOUT,1);
		addToTheme(VALUE_GAME_STRETCH,0);
		addToTheme(VALUE_GAME_OFFSET,0);
		addToTheme(VALUE_GAME_BORDER,0);
		addToTheme(VALUE_POPUP_OPACITY,80);
	}
	
	protected void addToTheme(final String name,final Object value) {
		
		themeMap.put(name,value);
	}
	
	@Override
	public void load() {
		
	}

	@Override
	public String getName() {

		return name;
	}

	@Override
	public BufferedImage getTexture(final String name) {

		final Object value=themeMap.get(name);
		return value==null?IconImages.MISSING:(BufferedImage)value;
	}

	@Override
	public BufferedImage getLogoTexture() {

		return (BufferedImage)themeMap.get(Theme.TEXTURE_LOGO);
	}

	@Override
	public ImageIcon getIcon(final String name) {

		final Object value=themeMap.get(name);
		return value==null?IconImages.MISSING2:(ImageIcon)value;
	}

	@Override
	public Color getColor(final String name) {

		final Object value=themeMap.get(name);
		return value==null?Color.BLACK:(Color)value;
	}

	@Override
	public Color getTextColor() {

		return getColor(COLOR_TEXT_FOREGROUND);
	}

	@Override
	public Color getNameColor() {

		return getColor(COLOR_NAME_FOREGROUND);
	}

	@Override
	public Color getChoiceColor() {

		return getColor(COLOR_CHOICE);
	}

	@Override
	public int getValue(final String name) {
		
		final Object value=themeMap.get(name);
		return value==null?0:(Integer)value;
	}

	private PlayerAvatar[] loadAvatars(final List<File> avatarFiles) {

		final File files[]=new File(MagicMain.getGamePath()+File.separator+"avatars").listFiles();
		if (files==null||files.length<2) {
			avatarFiles.add(new File("unknown"));
			avatarFiles.add(new File("unknown"));
		} else {
			avatarFiles.addAll(Arrays.asList(files));
		}
		Collections.sort(avatarFiles);
		return new PlayerAvatar[avatarFiles.size()];
	}
	
	@Override
	public int getNumberOfAvatars() {

		return avatars.length;
	}

	@Override
	public synchronized ImageIcon getAvatarIcon(int index,final int size) {

		if (index<0) {
			index=0;
		} else if (index>=avatars.length) {
			index%=avatars.length;
		}
		PlayerAvatar avatar=avatars[index];
		if (avatar==null) {
			BufferedImage image;
			try {
				final InputStream stream=new FileInputStream(avatarFiles.get(index));
				image=ImageIO.read(stream);	
				stream.close();
			} catch (final Exception ex) {
				image=IconImages.MISSING;
			}		
			avatar=new PlayerAvatar(image);
			avatars[index]=avatar;
		}
		return avatar.getIcon(index,size);
	}
}