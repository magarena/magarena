package magic.ui.theme;

import java.awt.Color;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import magic.data.GeneralConfig;
import magic.ui.MagicImages;
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
