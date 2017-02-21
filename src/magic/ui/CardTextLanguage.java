package magic.ui;

public enum CardTextLanguage {

    ENGLISH("English", "en"),
    // Magarena translations
    ITALIAN("Italiano", "it"),
    PORTUGUESE("Português", "pt"),
    RUSSIAN("Русский", "ru"),
    // other languages offered by magiccards.info.
    GERMAN("Deutsch", "de"),
    SPANISH("Español", "es"),
    FRENCH("Français", "fr"),
    CHINESE("简体中文", "cn"),
    JAPANESE("日本語", "jp"),
    TAIWANESE("繁體中文", "tw"),
    KOREAN("한국어","ko")
    ;

    private final String caption;
    private final String mcardsLangCode;    // language code used by magiccards.info in image urls.

    private CardTextLanguage(String aCaption, String aLangCode) {
        this.caption = aCaption;
        this.mcardsLangCode = aLangCode;
    }

    @Override
    public String toString() {
        return caption;
    }

    public String getMagicCardsCode() {
        return mcardsLangCode;
    }

    public boolean isEnglish() {
        return this == ENGLISH;
    }
}
