package magic.data.settings;

public enum BooleanSetting {

    CUSTOM_BACKGROUND("customBackground", false),
    CUSTOM_SCROLLBAR("customScrollBar", true),
    FULL_SCREEN("fullScreen", false),
    MAXIMIZE_FRAME("maximized", false);

    private final String propertyName;
    private final boolean defaultValue;

    private BooleanSetting(String propertyName, boolean defaultValue) {
        this.propertyName = propertyName;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return propertyName;
    }

    public boolean getDefault() {
        return defaultValue;
    }

}
