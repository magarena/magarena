package magic.data.settings;

import java.awt.Color;
import magic.ui.MagicStickyFrame;

public enum IntegerSetting {
    /** Maximum duration of AI duel in seconds. */
    AI_DUEL_MATCH_LIMIT("aiDuelMatchLimitSeconds",600),
    CARD_OVERLAY_MIN_HEIGHT("overlayPermanentMinHeight", 30),
    DECK_MAX_LINES("deckFileMaxLines", 500),
    /** Maximum duration of AI game in deck strength calculator in seconds. */
    DECK_STR_MATCH_LIMIT("deckStrengthMatchLimitSeconds", 3600),
    /** Maximum duration of Firemind game in seconds. */
    FIREMIND_MATCH_LIMIT("firemindMatchLimitSeconds", 3600),
    FRAME_HEIGHT("height", MagicStickyFrame.DEFAULT_HEIGHT),
    FRAME_LEFT("left", -1),
    FRAME_TOP("top", -1),
    FRAME_WIDTH("width", MagicStickyFrame.DEFAULT_WIDTH),
    GAME_VOLUME("gameVolume", 80),
    ROLLOVER_RGB("rolloverColor", Color.YELLOW.getRGB()),
    /** Maximum duration of test match in seconds. */
    TEST_MATCH_LIMIT("testMatchLimitSeconds", 60),
    UI_VOLUME("uiSoundVolume", 80),
    ;

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
