package magic.ui.theme;

import java.util.ArrayList;
import java.util.List;

import magic.data.GeneralConfig;
import magic.data.IconImages;

public class ThemeFactory {

	private static final ThemeFactory INSTANCE=new ThemeFactory();
	
	private final List<Theme> themes;
	private Theme currentTheme;
	
	private ThemeFactory() {
		
		themes=loadThemes();
		currentTheme=themes.get(0);
		setCurrentTheme(GeneralConfig.getInstance().getTheme());
	}
	
	private static List<Theme> loadThemes() {

		final List<Theme> themes=new ArrayList<Theme>();
		themes.add(new DefaultTheme("wood",IconImages.WOOD,IconImages.MARBLE));
		themes.add(new DefaultTheme("light wood",IconImages.WOOD2,IconImages.MARBLE));
		themes.add(new DefaultTheme("granite",IconImages.GRANITE,IconImages.GRANITE2));
		themes.add(new DefaultTheme("opal",IconImages.OPAL,IconImages.OPAL2));
		return themes;
	}
	
	public List<Theme> getThemes() {
		
		return themes;
	}
	
	public String[] getThemeNames() {
		
		final String names[]=new String[themes.size()];
		for (int index=0;index<names.length;index++) {
			
			names[index]=themes.get(index).getName();
		}
		return names;
	}
	
	public void setCurrentTheme(final String name) {
		
		for (final Theme theme : themes) {
			
			if (theme.getName().equals(name)) {
				currentTheme=theme;
				break;
			}
		}
	}
	
	public Theme getCurrentTheme() {
		
		return currentTheme;
	}
		
	public static ThemeFactory getInstance() {
		
		return INSTANCE;
	}
}