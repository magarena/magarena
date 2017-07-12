package magic.data.settings;

public enum IntegerSetting {

    DECK_MAX_LINES("deckFileMaxLines", 500),
    GAME_VOLUME("gameVolume", 80),
    UI_VOLUME("uiSoundVolume", 80);

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
