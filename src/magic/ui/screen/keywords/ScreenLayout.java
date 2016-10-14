package magic.ui.screen.keywords;

import magic.data.GeneralConfig;

enum ScreenLayout {

    /**
     * Original two column text only on light background.
     */
    Layout1,

    /**
     * Multi-column list of keywords on dark translucent background.
     * Can show sample card image for selected keyword.
     */
    Layout2A,

    /**
     * Multi-column list of keywords on dark translucent background.
     * Can multiple sample card thumbnail images for selected keyword.
     */
    Layout2B;

    private static ScreenLayout layout;
    static {
        try {
            final String setting = GeneralConfig.getInstance().getKeywordsSettings();
            layout = setting.isEmpty() ? Layout1 : valueOf(setting);
        } catch (Exception ex) {
            System.err.println(ex);
            layout = Layout1;
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
