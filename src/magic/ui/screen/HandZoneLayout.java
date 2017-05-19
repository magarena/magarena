package magic.ui.screen;

import java.util.logging.Level;
import java.util.logging.Logger;
import magic.data.GeneralConfig;

/**
 * Various layouts for the card images displayed in a
 * player's hand. Also used for the mulligan screen.
  */
public enum HandZoneLayout {

    STACKED_DUPLICATES,     // default, as per pre-1.85 behaviour.
    NO_STACKING;

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();
    private static final String CONFIG_SETTING = "hand.zone.layout";

    private static HandZoneLayout activeLayout = load();

    private static HandZoneLayout load() {
        try {
            int ordinal = CONFIG.getInt(CONFIG_SETTING, STACKED_DUPLICATES.ordinal());
            return HandZoneLayout.values()[ordinal];
        } catch (Exception ex) {
            Logger.getLogger(HandZoneLayout.class.getName()).log(Level.SEVERE, null, ex);
            return STACKED_DUPLICATES;
        }
    }

    public static void save() {
        CONFIG.set(CONFIG_SETTING, activeLayout.ordinal());
    }

    public static HandZoneLayout getLayout() {
        return activeLayout;
    }

    public static void setLayout(int i) {
        activeLayout = HandZoneLayout.values()[i];
    }
}
