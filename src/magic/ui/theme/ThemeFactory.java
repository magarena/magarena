package magic.ui.theme;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import magic.data.GeneralConfig;
import magic.ui.MagicImages;
import magic.utility.MagicFileSystem;

public class ThemeFactory {

    private static final ThemeFactory INSTANCE = new ThemeFactory();

    private Theme currentTheme;

    private ThemeFactory() {
        loadThemes();
    }

    public void loadThemes() {
        setCurrentTheme(GeneralConfig.getInstance().getTheme());
    }

    public static String[] getThemeNames() {

        final List<String> themes = new ArrayList<>();

        // built-in themes...
        themes.add("wood");
        themes.add("granite");
        themes.add("opal");

        // custom themes in "themes" folder...
        final File[] files = MagicFileSystem.getThemes();
        for (File f : files) {
            String name = CustomTheme.getThemeName(f);
            if (!themes.contains(name)) {
                themes.add(name);
            }
        }

        // sort alphabetically (ignoring case)...
        Collections.sort(themes, (s1, s2) -> s1.compareToIgnoreCase(s2));

        return themes.toArray(new String[themes.size()]);
    }

    private static Theme getBuiltInTheme(String themeName) {
        switch (themeName.toLowerCase(Locale.ENGLISH)) {
            case "wood":
                return new DefaultTheme("wood", MagicImages.WOOD, MagicImages.MARBLE, Color.BLACK);
            case "granite":
                return new DefaultTheme("granite", MagicImages.GRANITE, MagicImages.GRANITE2, Color.BLACK);
            case "opal":
                return new DefaultTheme("opal", MagicImages.OPAL, MagicImages.OPAL2, Color.BLUE);
        }
        return null;
    }

    public Theme loadTheme(String name) {
        final Theme custom = CustomTheme.loadTheme(name);
        if (custom != null) {
            return custom;
        }
        final Theme builtin = getBuiltInTheme(name);
        return builtin != null ? builtin : getBuiltInTheme("wood");
    }

    public void setCurrentTheme(final String aName) {
        for (String theme : getThemeNames()) {
            if (theme.equalsIgnoreCase(aName)) {
                currentTheme = loadTheme(theme);
                return;
            }
        }
        currentTheme = getBuiltInTheme("wood");
    }

    public Theme getCurrentTheme() {
        return currentTheme;
    }

    public static Theme getTheme() {
        return getInstance().currentTheme;
    }

    public static ThemeFactory getInstance() {
        return INSTANCE;
    }

    public static File getThemeFile(String name) {
        if (getBuiltInTheme(name) != null) {
            return null;
        }
        return CustomTheme.getThemeFile(name);
    }

}
