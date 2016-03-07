package magic.ui.theme;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import magic.data.GeneralConfig;
import magic.ui.MagicImages;
import magic.utility.MagicFileSystem;

public class ThemeFactory {

    private static final ThemeFactory INSTANCE = new ThemeFactory();

    private final List<Theme> themes = new ArrayList<>();
    private Theme currentTheme;

    private ThemeFactory() {
        loadThemes();
    }

    public void loadThemes() {
        themes.clear();
        loadBasicBuiltInThemes();
        loadCustomExternalThemes();
        setCurrentTheme(GeneralConfig.getInstance().getTheme());
    }

    private void loadCustomExternalThemes() {
        final File[] files = MagicFileSystem.getThemes();
            for (final File file : files) {
                themes.add(new CustomTheme(file));
            }
    }

    private void loadBasicBuiltInThemes() {
        themes.add(new DefaultTheme("wood", MagicImages.WOOD, MagicImages.MARBLE, Color.BLACK));
        themes.add(new DefaultTheme("granite", MagicImages.GRANITE, MagicImages.GRANITE2, Color.BLACK));
        themes.add(new DefaultTheme("opal", MagicImages.OPAL, MagicImages.OPAL2, Color.BLUE));
    }

    public String[] getThemeNames() {
        final String[] names = new String[themes.size()];
        for (int index = 0; index < names.length; index++) {
            names[index] = themes.get(index).getName();
        }
        return names;
    }

    public void setCurrentTheme(final String name) {
        currentTheme = themes.get(0);
        for (final Theme theme : themes) {
            if (theme.getName().equals(name)) {
                theme.load();
                currentTheme = theme;
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
