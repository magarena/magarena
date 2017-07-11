package magic.ui.screen.keywords;

import magic.data.GeneralConfig;
import magic.data.settings.StringSetting;

enum ScreenLayout {

    /**
     * Original two column text only on light background.
     */
    Layout_A,

    /**
     * Multi-column list of keywords on dark translucent background.
     * Can show sample card image for selected keyword.
     */
    Layout_B;

    private static ScreenLayout layout;
    static {
        try {
            final String setting = GeneralConfig.getInstance().get(StringSetting.KEYWORDS_SCREEN);
            layout = setting.isEmpty() ? Layout_A : valueOf(setting);
        } catch (Exception ex) {
            System.err.println(ex);
            layout = Layout_A;
        }
    }

    private ScreenLayout next() {
        return values()[(this.ordinal()+1) % values().length];
    }

    static void setNextLayout() {
        layout = layout.next();
    }

    static ScreenLayout getLayout() {
        return layout;
    }
}
