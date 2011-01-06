package magic.ui.theme;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import magic.data.IconImages;

public abstract class AbstractTheme implements Theme {

	private final String name;
	private final Map<String,Object> themeMap;
	
	public AbstractTheme(final String name) {
		
		this.name=name;
		themeMap=new HashMap<String,Object>();
	}
	
	protected void addToTheme(final String name,final Object value) {
		
		themeMap.put(name,value);
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
}