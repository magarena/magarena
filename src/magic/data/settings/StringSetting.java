package magic.data.settings;

public enum StringSetting {

    KEYWORDS_SCREEN("keywordsScreen", ""),
    THEME("theme", "felt");

    private final String propertyName;
    private final String defaultValue;

    private StringSetting(String propertyName, String defaultValue) {
        this.propertyName = propertyName;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return propertyName;
    }

    public String getDefault() {
        return defaultValue;
    }

}
