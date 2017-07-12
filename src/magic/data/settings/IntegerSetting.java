package magic.data.settings;

public enum IntegerSetting {

    UI_VOLUME("uiSoundVolume", 80),
    DECK_MAX_LINES("deckFileMaxLines", 500);

    private final String propertyName;
    private final int defaultValue;

    private IntegerSetting(String propertyName, int defaultValue) {
        this.propertyName = propertyName;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return propertyName;
    }

    public int getDefault() {
        return defaultValue;
    }

}
