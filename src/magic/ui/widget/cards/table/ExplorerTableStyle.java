package magic.ui.widget.cards.table;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import magic.data.GeneralConfig;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;

public enum ExplorerTableStyle {

    LIGHT(Color.WHITE),
    DARK(Color.BLACK),
    THEME(MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND));

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();
    private static final String CONFIG_SETTING = "explorer.table.style";

    private static ExplorerTableStyle style = load();

    private static ExplorerTableStyle load() {
        try {
            int ordinal = CONFIG.getInt(CONFIG_SETTING, LIGHT.ordinal());
            return ExplorerTableStyle.values()[ordinal];
        } catch (Exception ex) {
            Logger.getLogger(ExplorerTableStyle.class.getName()).log(Level.SEVERE, null, ex);
            return LIGHT;
        }
    }

    public static void save() {
        CONFIG.set(CONFIG_SETTING, style.ordinal());
    }

    static void setStyle(ExplorerTableStyle newStyle) {
        style = newStyle;
    }

    private ExplorerTableStyle next() {
        return values()[(this.ordinal()+1) % values().length];
    }

    public static void setNextStyle() {
        style = style.next();
    }

    public static ExplorerTableStyle getStyle() {
        return style;
    }

    private final Color emptyBackgroundColor;

    private ExplorerTableStyle(Color emptyBackgroundColor) {
        this.emptyBackgroundColor = MagicStyle.getTranslucentColor(emptyBackgroundColor, 230);
    }

    public Color getEmptyBackgroundColor() {
        return this.emptyBackgroundColor;
    }
}
