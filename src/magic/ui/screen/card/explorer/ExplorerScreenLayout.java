package magic.ui.screen.card.explorer;

import java.util.logging.Level;
import java.util.logging.Logger;
import magic.data.GeneralConfig;

public enum ExplorerScreenLayout {

    DEFAULT,
    NO_SIDEBAR;

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();
    private static final String CONFIG_SETTING = "explorer.layout";

    private static ExplorerScreenLayout activeLayout = load();

    private static ExplorerScreenLayout load() {
        try {
            int ordinal = CONFIG.getInt(CONFIG_SETTING, DEFAULT.ordinal());
            return ExplorerScreenLayout.values()[ordinal];
        } catch (Exception ex) {
            Logger.getLogger(ExplorerScreenLayout.class.getName()).log(Level.SEVERE, null, ex);
            return DEFAULT;
        }
    }

    static void save() {
        CONFIG.set(CONFIG_SETTING, activeLayout.ordinal());
    }

    private ExplorerScreenLayout next() {
        return values()[(this.ordinal()+1) % values().length];
    }

    public static ExplorerScreenLayout getLayout() {
        return activeLayout;
    }

    static void setLayout(int i) {
        activeLayout = ExplorerScreenLayout.values()[i];
    }

    public static void setNextLayout() {
        activeLayout = activeLayout.next();
    }
}
