package magic.ui.screen;

import magic.translate.MText;

/**
 * Various layouts for the card images displayed in a
 * player's hand. Also used for the mulligan screen.
  */
public enum HandZoneLayout {

    STACKED_DUPLICATES(UIText._S1),     // default, as per pre-1.85 behaviour.
    NO_STACKING(UIText._S2);

    private static HandZoneLayout activeLayout = STACKED_DUPLICATES;

    public static HandZoneLayout getLayout() {
        return activeLayout;
    }

    public static void setLayout(int i) {
        activeLayout = HandZoneLayout.values()[i];
    }

    /**
     * Instance fields and methods.
     */

    private final String displayName;

    private HandZoneLayout(final String displayName) {
        this.displayName = MText.get(displayName);
    }

    public String getDisplayName() {
        return displayName;
    }
}

/**
 * translatable strings
 */
final class UIText {
    private UIText() {}
    static final String _S1 = "Stacked";
    static final String _S2 = "Unstacked";
}
