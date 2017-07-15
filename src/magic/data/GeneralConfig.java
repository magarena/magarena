package magic.data;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import magic.data.settings.BooleanSetting;
import magic.data.settings.IntegerSetting;
import magic.data.settings.StringSetting;
import magic.ui.CardTextLanguage;
import magic.ui.dialog.prefs.ImageSizePresets;
import magic.ui.screen.HandZoneLayout;
import magic.ui.screen.card.explorer.ExplorerScreenLayout;
import magic.ui.screen.images.download.CardImageDisplayMode;
import magic.ui.widget.cards.table.CardsTableStyle;
import magic.ui.widget.duel.animation.AnimationFx;
import magic.ui.widget.message.MessageStyle;
import magic.utility.FileIO;
import magic.utility.MagicFileSystem;
import magic.utility.MagicSystem;

public class GeneralConfig {

    private static final GeneralConfig INSTANCE = new GeneralConfig();

    public static final String CONFIG_FILENAME = "general.cfg";
    public static final String CONFIG_HEADER = "Magarena " + MagicSystem.VERSION;

    public static final String DEFAULT_TRANSLATION = "";

    // settings that can only be updated by manually editing the config file.
    private static final String OVERLAY_PERMANENT_MIN_HEIGHT = "overlayPermanentMinHeight";

    // settings that should normally be updated via the UI preferences dialog.
    private static final String ALWAYS_PASS = "pass";
    private static final String ANIMATE_GAMEPLAY = "animateGameplay";
    private static final String ANIMATION_FLAGS = "animationFlags";
    private static final String CARDFLOW_SCREEN_SETTINGS = "cardflow.screen";
    private static final String CARDS_TABLE_STYLE = "explorer.table.style";
    private static final String CARD_DISPLAY_MODE = "cardImageDisplayMode";
    private static final String CARD_IMAGES_PATH = "cardImagesPath";
    private static final String CARD_TEXT_LANG = "cardTextLanguage";
    private static final String DUEL_SIDEBAR_LAYOUT ="duelSidebarLayout";
    private static final String EXPLORER_LAYOUT = "explorer.layout";
    private static final String FIREMIND_ACCESS_TOKEN = "firemindAccessToken";
    private static final String GAME_STATS = "gameStats";
    private static final String HAND_ZONE_LAYOUT = "hand.zone.layout";
    private static final String HIDE_AI_ACTION_PROMPT ="hideAiActionPrompt";
    private static final String HIGHLIGHT = "highlight";
    private static final String IGNORED_VERSION_ALERT = "ignoredVersionAlert";
    private static final String IMAGES_ON_DEMAND = "imagesOnDemand";
    private static final String LAND_PREVIEW_DURATION = "landPreviewDuration";
    private static final String LOG_MESSAGE_STYLE = "logMessageStyle";
    private static final String MESSAGE_DELAY = "message";
    private static final String MISSING_DOWNLOAD_DATE = "missingImagesDownloadDate";
    private static final String MOUSEWHEEL_POPUP = "mousewheel";
    private static final String MULLIGAN_SCREEN = "mulliganScreen";
    private static final String NEWTURN_ALERT_DURATION = "newTurnAlertDuration";
    private static final String NONLAND_PREVIEW_DURATION = "nonLandPreviewDuration";
    private static final String PAUSE_GAME_POPUP = "pauseGamePopup";
    private static final String PLAYABLE_DOWNLOAD_DATE = "imageDownloaderRunDate";
    private static final String POPUP_DELAY="popup";
    private static final String PREF_IMAGE_SIZE = "prefImageSize";
    private static final String PREVIEW_CARD_ON_SELECT = "previewCardOnSelect";
    private static final String PROXY_SETTINGS = "proxySettings";
    private static final String RECENT_DECK = "MostRecentDeckFilename";
    private static final String SHOW_LOG_MESSAGES = "showLogMessages";
    private static final String SKIP_SINGLE = "single";
    private static final String SMART_TARGET = "target";
    private static final String SPLITVIEW_DECKEDITOR = "splitViewDeckEditor";
    private static final String TOUCHSCREEN = "touchscreen";
    private static final String TRANSLATION = "translation";

    // Settings that should not be imported into the current version
    public static final String[] NOT_IMPORTED = new String[]{

        // properties reset each version
        IntegerSetting.FRAME_TOP.getKey(),
        IntegerSetting.FRAME_LEFT.getKey(),
        IntegerSetting.FRAME_WIDTH.getKey(),
        IntegerSetting.FRAME_HEIGHT.getKey(),
        BooleanSetting.FULL_SCREEN.getKey(),
        TRANSLATION,

        // obsolete properties
        "avatar"
    };

    private static boolean isMissingFiles = false;

    private Properties settings;

    private GeneralConfig() { }

    public static GeneralConfig getInstance() {
        return INSTANCE;
    }

    public static void saveToFile() {
        INSTANCE.save();
    }

    private static File getConfigFile() {
        return MagicFileSystem.getDataPath().resolve(CONFIG_FILENAME).toFile();
    }

    public void load() {
        settings = FileIO.toProp(getConfigFile());
        CardsTableStyle.setStyle(getProperty(CARDS_TABLE_STYLE, CardsTableStyle.getStyle().ordinal()));
        ExplorerScreenLayout.setLayout(getProperty(EXPLORER_LAYOUT, ExplorerScreenLayout.getLayout().ordinal()));
        HandZoneLayout.setLayout(getProperty(HAND_ZONE_LAYOUT, HandZoneLayout.getLayout().ordinal()));
        AnimationFx.setFlags(getProperty(ANIMATION_FLAGS, AnimationFx.getFlags()));
    }

    private void setProperties() {
        setProperty(ANIMATION_FLAGS, AnimationFx.getFlags());
        setProperty(EXPLORER_LAYOUT, ExplorerScreenLayout.getLayout().ordinal());
        setProperty(HAND_ZONE_LAYOUT, HandZoneLayout.getLayout().ordinal());
        setProperty(CARDS_TABLE_STYLE, CardsTableStyle.getStyle().ordinal());
    }

    public void save() {
        setProperties();
        try {
            FileIO.toFile(getConfigFile(), settings, CONFIG_HEADER);
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to save general config");
        }
    }

    //---------------------------------------------------------------------
    // get & set values of different types from Properties instance.
    //---------------------------------------------------------------------

    // integer value
    private int getProperty(String key, int defaultValue) {
        return Integer.parseInt(settings.getProperty(key, String.valueOf(defaultValue)));
    }

    private void setProperty(String key, int value) {
        settings.setProperty(key, String.valueOf(value));
    }

    // long value
    private long getProperty(String key, long defaultValue) {
        return Long.parseLong(settings.getProperty(key, String.valueOf(defaultValue)));
    }

    private void setProperty(String key, long value) {
        settings.setProperty(key, String.valueOf(value));
    }

    // boolean value
    private boolean getProperty(String key, boolean defaultValue) {
        return Boolean.parseBoolean(settings.getProperty(key, String.valueOf(defaultValue)));
    }

    private void setProperty(String key, boolean value) {
        settings.setProperty(key, String.valueOf(value));
    }

    // string value
    private String getProperty(String key, String defaultValue) {
        return settings.getProperty(key, defaultValue);
    }

    private void setProperty(String key, String value) {
        settings.setProperty(key, value);
    }

    //---------------------------------------------------------------------
    // new api for setting & getting application settings.
    // TODO: migrate all settings constants to *Setting enum.
    //---------------------------------------------------------------------

    // boolean
    public static boolean get(BooleanSetting setting) {
        return INSTANCE.getProperty(setting.getKey(), setting.getDefault());
    }

    public static void set(BooleanSetting setting, boolean value) {
        INSTANCE.setProperty(setting.getKey(), value);
    }

    // integer
    public static int get(IntegerSetting setting) {
        return INSTANCE.getProperty(setting.getKey(), setting.getDefault());
    }

    public static void set(IntegerSetting setting, int value) {
        INSTANCE.setProperty(setting.getKey(), value);
    }

    // string
    public static String get(StringSetting setting) {
        return INSTANCE.getProperty(setting.getKey(), setting.getDefault());
    }

    public static void set(StringSetting setting, String value) {
        INSTANCE.setProperty(setting.getKey(), value);
    }

    //---------------------------------------------------------------------
    // Setters & getters for each setting.
    // TODO: replace with new get/set api defined above.
    //---------------------------------------------------------------------

    public String getProxySettings() {
        return getProperty(PROXY_SETTINGS, "").trim();
    }

    public Proxy getProxy() {
        final String DELIM = "\\|";
        String setting = getProxySettings();
        if (!setting.isEmpty() && setting.split(DELIM).length == 3) {
            Proxy.Type proxyType = Proxy.Type.valueOf(setting.split(DELIM)[0]);
            int port = Integer.parseInt(setting.split(DELIM)[1]);
            String urlAddress = setting.split(DELIM)[2];
            return new Proxy(proxyType, new InetSocketAddress(urlAddress, port));
        }
        return Proxy.NO_PROXY;
    }

    public void setProxy(final Proxy proxy) {
        final String DELIM = "|";
        if (proxy != Proxy.NO_PROXY && proxy.type() != Proxy.Type.DIRECT) {
            final StringBuffer sb = new StringBuffer();
            sb.append(proxy.type().toString()).append(DELIM);
            sb.append(Integer.toString(((InetSocketAddress)proxy.address()).getPort())).append(DELIM);
            sb.append(proxy.address().toString());
            setProperty(PROXY_SETTINGS, sb.toString());
        } else {
            setProperty(PROXY_SETTINGS, "");
        }
    }

    public boolean showGameplayAnimations() {
        return getProperty(ANIMATE_GAMEPLAY, true);
    }

    public void setShowGameplayAnimations(boolean b) {
        setProperty(ANIMATE_GAMEPLAY, b);
    }

    public boolean isCustomCardImagesPath() {
        return getProperty(CARD_IMAGES_PATH, "").isEmpty() == false;
    }

    public Path getCardImagesPath() {
        String setting = getProperty(CARD_IMAGES_PATH, "");
        return setting.isEmpty()
            ? MagicFileSystem.getDataPath(MagicFileSystem.DataPath.IMAGES)
            : Paths.get(setting);
    }

    public void setCardImagesPath(final Path p) {
        String setting = MagicFileSystem.directoryContains(MagicFileSystem.INSTALL_PATH, p)
            ? ""
            : p.toAbsolutePath().toString();
        setProperty(CARD_IMAGES_PATH, setting);
    }

    public static boolean isMissingFiles() {
        return isMissingFiles;
    }
    public static void setIsMissingFiles(final boolean b) {
        isMissingFiles = b;
    }

    /**
     * Gets fully qualified path of last deck file to be opened in the deck editor.
     *
     * @return path object or null if setting is missing.
     */
    public Path getMostRecentDeckFilePath() {
        String setting = getProperty(RECENT_DECK, "").trim();
        return !setting.isEmpty() ? Paths.get(setting) : null;
    }

    public void setMostRecentDeckFilename(String filename) {
        setProperty(RECENT_DECK, filename.trim());
    }

    public String getHighlight() {
        return getProperty(HIGHLIGHT, "theme");
    }

    public void setHighlight(String highlight) {
        setProperty(HIGHLIGHT, highlight);
    }

    public boolean isHighlightNone() {
        return "none".equals(getHighlight());
    }

    public boolean isHighlightOverlay() {
        return "overlay".equals(getHighlight());
    }

    public boolean isHighlightTheme() {
        return "theme".equals(getHighlight());
    }

    public String getFiremindAccessToken() {
        return getProperty(FIREMIND_ACCESS_TOKEN, "");
    }

    public void setFiremindAccessToken(String firemindAccessToken) {
        setProperty(FIREMIND_ACCESS_TOKEN, firemindAccessToken);
    }

    public boolean getSkipSingle() {
        return getProperty(SKIP_SINGLE, true);
    }

    public void setSkipSingle(boolean skipSingle) {
        setProperty(SKIP_SINGLE, skipSingle);
    }

    public boolean getAlwaysPass() {
        return getProperty(ALWAYS_PASS, true);
    }

    public void setAlwaysPass(boolean alwaysPass) {
        setProperty(ALWAYS_PASS, alwaysPass);
    }

    public boolean getSmartTarget() {
        return getProperty(SMART_TARGET, false);
    }

    public void setSmartTarget(boolean smartTarget) {
        setProperty(SMART_TARGET, smartTarget);
    }

    public int getPopupDelay() {
        return getProperty(POPUP_DELAY, 300);
    }

    public void setPopupDelay(int popupDelay) {
        setProperty(POPUP_DELAY, popupDelay);
    }

    public int getMessageDelay() {
        return getProperty(MESSAGE_DELAY, 2000);
    }

    public void setMessageDelay(int messageDelay) {
        setProperty(MESSAGE_DELAY, messageDelay);
    }

    public boolean isTouchscreen() {
        return getProperty(TOUCHSCREEN, false);
    }

    public void setTouchscreen(boolean touchscreen) {
        setProperty(TOUCHSCREEN, touchscreen);
    }

    public boolean isMouseWheelPopup() {
        return getProperty(MOUSEWHEEL_POPUP, false);
    }

    public void setMouseWheelPopup(boolean mouseWheelPopup) {
        setProperty(MOUSEWHEEL_POPUP, mouseWheelPopup);
    }

    public boolean isPreviewCardOnSelect() {
        return getProperty(PREVIEW_CARD_ON_SELECT, true);
    }

    public void setPreviewCardOnSelect(boolean b) {
        setProperty(PREVIEW_CARD_ON_SELECT, b);
    }

    public boolean isLogMessagesVisible() {
        return getProperty(SHOW_LOG_MESSAGES, true);
    }

    public void setLogMessagesVisible(boolean b) {
        setProperty(SHOW_LOG_MESSAGES, b);
    }

    public boolean showMulliganScreen() {
        return getProperty(MULLIGAN_SCREEN, true);
    }

    public void setShowMulliganScreen(boolean b) {
        setProperty(MULLIGAN_SCREEN, b);
    }

    public int getNewTurnAlertDuration() {
        return getProperty(NEWTURN_ALERT_DURATION, 3000);
    }

    public void setNewTurnAlertDuration(int millisecs) {
        setProperty(NEWTURN_ALERT_DURATION, millisecs);
    }

    public int getLandPreviewDuration() {
        return getProperty(LAND_PREVIEW_DURATION, 5000);
    }

    public void setLandPreviewDuration(final int millisecs) {
        setProperty(LAND_PREVIEW_DURATION, millisecs);
    }

    public int getNonLandPreviewDuration() {
        return getProperty(NONLAND_PREVIEW_DURATION, 10000);
    }

    public void setNonLandPreviewDuration(int millisecs) {
        setProperty(NONLAND_PREVIEW_DURATION, millisecs);
    }

    public boolean isSplitViewDeckEditor() {
        return getProperty(SPLITVIEW_DECKEDITOR, false);
    }

    public void setIsSplitViewDeckEditor(boolean b) {
        setProperty(SPLITVIEW_DECKEDITOR, b);
    }

    public String getIgnoredVersionAlert() {
        return getProperty(IGNORED_VERSION_ALERT, "");
    }

    public void setIgnoredVersionAlert(String version) {
        setProperty(IGNORED_VERSION_ALERT, version);
    }

    /**
     * Minimum height of card image on which overlays such as P/T,
     * ability icons, etc should be shown.
     * <p>
     * Non-UI: requires manual update of config file to change.
     */
    public int getOverlayMinimumHeight() {
        return getProperty(OVERLAY_PERMANENT_MIN_HEIGHT, 30);
    }

    public boolean isGamePausedOnPopup() {
        return getProperty(PAUSE_GAME_POPUP, false);
    }

    public void setIsGamePausedOnPopup(boolean b) {
        setProperty(PAUSE_GAME_POPUP, b);
    }

    public String getDuelSidebarLayout() {
        return getProperty(DUEL_SIDEBAR_LAYOUT, "LOGSTACK,PLAYER2,TURNINFO,PLAYER1");
    }

    public void setDuelSidebarLayout(String layout) {
        setProperty(DUEL_SIDEBAR_LAYOUT, layout);
    }

    /**
     * Gets the last date playable images were downloaded.
     * <p>
     * If missing then date is set to "1970-01-01".
     */
    public Date getPlayableImagesDownloadDate() {
        try {
            SimpleDateFormat df = new SimpleDateFormat(CardProperty.IMAGE_UPDATED_FORMAT);
            return df.parse(getProperty(PLAYABLE_DOWNLOAD_DATE, "1970-01-01"));
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setPlayableImagesDownloadDate(final Date runDate) {
        SimpleDateFormat df = new SimpleDateFormat(CardProperty.IMAGE_UPDATED_FORMAT);
        setProperty(PLAYABLE_DOWNLOAD_DATE, df.format(runDate));
    }

    /**
     * Gets the last date unimplemented images were downloaded.
     * <p>
     * If missing then date is set to "1970-01-01".
     */
    public Date getUnimplementedImagesDownloadDate() {
        try {
            SimpleDateFormat df = new SimpleDateFormat(CardProperty.IMAGE_UPDATED_FORMAT);
            return df.parse(getProperty(MISSING_DOWNLOAD_DATE, "1970-01-01"));
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setUnimplementedImagesDownloadDate(final Date runDate) {
        SimpleDateFormat df = new SimpleDateFormat(CardProperty.IMAGE_UPDATED_FORMAT);
        setProperty(MISSING_DOWNLOAD_DATE, df.format(runDate));
    }

    public boolean getHideAiActionPrompt() {
        return getProperty(HIDE_AI_ACTION_PROMPT, false);
    }

    public void setHideAiActionPrompt(boolean b) {
        setProperty(HIDE_AI_ACTION_PROMPT, b);
    }

    public String getTranslation() {
        return getProperty(TRANSLATION, DEFAULT_TRANSLATION);
    }

    public void setTranslation(String value) {
        setProperty(TRANSLATION, value);
    }

    public MessageStyle getLogMessageStyle() {
        return MessageStyle.valueOf(
            getProperty(LOG_MESSAGE_STYLE, MessageStyle.PLAIN.name())
        );
    }

    public void setLogMessageStyle(MessageStyle aStyle) {
        setProperty(LOG_MESSAGE_STYLE, aStyle.name());
    }

    public ImageSizePresets getPreferredImageSize() {
        return ImageSizePresets.valueOf(
            getProperty(PREF_IMAGE_SIZE, ImageSizePresets.SIZE_ORIGINAL.name())
        );
    }

    public void setPreferredImageSize(ImageSizePresets preset) {
        setProperty(PREF_IMAGE_SIZE, preset.name());
    }

    public CardTextLanguage getCardTextLanguage() {
        return CardTextLanguage.valueOf(
            getProperty(CARD_TEXT_LANG, CardTextLanguage.ENGLISH.name())
        );
    }

    public void setCardTextLanguage(CardTextLanguage aLang) {
        setProperty(CARD_TEXT_LANG, aLang.name());
    }

    public boolean getImagesOnDemand() {
        return getProperty(IMAGES_ON_DEMAND, false);
    }

    public void setImagesOnDemand(boolean b) {
        setProperty(IMAGES_ON_DEMAND, b);
    }

    public CardImageDisplayMode getCardImageDisplayMode() {
        return CardImageDisplayMode.valueOf(
            getProperty(CARD_DISPLAY_MODE, CardImageDisplayMode.PRINTED.name())
        );
    }

    public void setCardImageDisplayMode(CardImageDisplayMode newMode) {
        setProperty(CARD_DISPLAY_MODE, newMode.name());
    }

    public void setGameStatsEnabled(boolean b) {
        setProperty(GAME_STATS, b);
    }

    public boolean isGameStatsEnabled() {
        return getProperty(GAME_STATS, true);
    }

    public static boolean isGameStatsOn() {
        return getInstance().isGameStatsEnabled();
    }

    public String getCardFlowScreenSettings() {
        return getProperty(CARDFLOW_SCREEN_SETTINGS, "");
    }

    public void setCardFlowScreenSettings(String settings) {
        setProperty(CARDFLOW_SCREEN_SETTINGS, settings);
    }

}
