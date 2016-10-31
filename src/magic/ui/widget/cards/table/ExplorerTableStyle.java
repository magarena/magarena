package magic.ui.widget.cards.table;

import java.util.logging.Level;
import java.util.logging.Logger;
import magic.data.GeneralConfig;

public enum ExplorerTableStyle {

    LIGHT,
    DARK,
    THEME;
    
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

    private ExplorerTableStyle next() {
        return values()[(this.ordinal()+1) % values().length];
    }

    static void setNextStyle() {
        style = style.next();
    }

    public static ExplorerTableStyle getStyle() {
        return style;
    }
}
