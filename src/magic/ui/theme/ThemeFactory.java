package magic.ui.theme;

import java.awt.Color;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import magic.data.GeneralConfig;
import magic.data.IconImages;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;

public class ThemeFactory {

    private static final String THEME_ZIP = "_theme.zip";
    private static final String THEME_FOLDER = "_theme";

    // Must be before instance!
    private static final FileFilter THEME_FILE_FILTER = new FileFilter() {
        @Override
        public boolean accept(final File file) {
            return (file.isFile() && file.getName().endsWith(THEME_ZIP)) ||
                   (file.isDirectory() && file.getName().endsWith(THEME_FOLDER));
        }
    };

    private static final ThemeFactory INSTANCE = new ThemeFactory();

    private final List<Theme> themes;
    private Theme currentTheme;

    private ThemeFactory() {
        themes = loadThemes();
        currentTheme = themes.get(0);
        setCurrentTheme(GeneralConfig.getInstance().getTheme());
    }

    private static List<Theme> loadThemes() {

        final List<Theme> themes = new ArrayList<Theme>();
        themes.add(new DefaultTheme("wood", IconImages.WOOD, IconImages.MARBLE, Color.BLACK));
        themes.add(new DefaultTheme("granite", IconImages.GRANITE, IconImages.GRANITE2, Color.BLACK));
        themes.add(new DefaultTheme("opal", IconImages.OPAL, IconImages.OPAL2, Color.BLUE));

        final File[] files = MagicFileSystem.getDataPath(DataPath.MODS).toFile().listFiles(THEME_FILE_FILTER);
        if (files != null) {
            for (final File file : files) {
                final String name = file.getName();
                int index = name.indexOf(THEME_ZIP);
                if (index < 0) {
                    index = name.indexOf(THEME_FOLDER);
                }
                themes.add(new CustomTheme(file, name.substring(0, index)));
            }
        }
        return themes;
    }

    public String[] getThemeNames() {
        final String[] names = new String[themes.size()];
        for (int index = 0; index < names.length; index++) {
            names[index] = themes.get(index).getName();
        }
        return names;
    }

    public void setCurrentTheme(final String name) {
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
