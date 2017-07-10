package magic.data;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
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

    public static final int DEFAULT_FRAME_WIDTH = 1024;
    public static final int DEFAULT_FRAME_HEIGHT = 600;
    public static final String DEFAULT_TRANSLATION = "";

    // settings that can only be updated by manually editing the config file.
    private static final String AVATAR = "avatar";
    private static final String CUSTOM_FONTS = "custom.fonts";
    private static final String DECK_FILE_MAX_LINES = "deckFileMaxLines";
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
    private static final String CUSTOM_BACKGROUND = "customBackground";
    private static final String CUSTOM_SCROLLBAR = "customScrollBar";
    private static final String DUEL_SIDEBAR_LAYOUT ="duelSidebarLayout";
    private static final String EXPLORER_LAYOUT = "explorer.layout";
    private static final String FIREMIND_ACCESS_TOKEN = "firemindAccessToken";
    private static final String FRAME_HEIGHT = "height";
    private static final String FRAME_LEFT = "left";
    private static final String FRAME_TOP = "top";
    private static final String FRAME_WIDTH = "width";
    private static final String FULLSCREEN = "fullScreen";
    private static final String GAME_STATS = "gameStats";
    private static final String GAME_VOLUME = "gameVolume";
    private static final String HAND_ZONE_LAYOUT = "hand.zone.layout";
    private static final String HIDE_AI_ACTION_PROMPT ="hideAiActionPrompt";
    private static final String HIGHLIGHT = "highlight";
    private static final String IGNORED_VERSION_ALERT = "ignoredVersionAlert";
    private static final String IMAGES_ON_DEMAND = "imagesOnDemand";
    private static final String KEYWORDS_SCREEN = "keywordsScreen";
    private static final String LAND_PREVIEW_DURATION = "landPreviewDuration";
    private static final String LOG_MESSAGE_STYLE = "logMessageStyle";
    private static final String MAXIMIZED = "maximized";
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
    private static final String ROLLOVER_COLOR ="rolloverColor";
    private static final String SHOW_LOG_MESSAGES = "showLogMessages";
    private static final String SKIP_SINGLE = "single";
    private static final String SMART_TARGET = "target";
    private static final String SPLITVIEW_DECKEDITOR = "splitViewDeckEditor";
    private static final String THEME = "theme";
    private static final String TOUCHSCREEN = "touchscreen";
    private static final String TRANSLATION = "translation";
    private static final String UI_VOLUME = "uiSoundVolume";

    // obsolete settings that should not be imported into the current version
    // or version sensitive settings that should not be overwritten.
    public static final String[] NOT_IMPORTED = new String[]{
        FRAME_TOP, FRAME_LEFT, FRAME_WIDTH, FRAME_HEIGHT,
        FULLSCREEN, TRANSLATION
    };

    private Properties settings;
    private boolean isMissingFiles = false;

    private GeneralConfig() { }

    public String getProxySettings() {
        return getString(PROXY_SETTINGS, "").trim();
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

    public int getDeckFileMaxLines() {
        return getInteger(DECK_FILE_MAX_LINES, 500);
    }

    public boolean showGameplayAnimations() {
        return getBoolean(ANIMATE_GAMEPLAY, true);
    }

    public void setShowGameplayAnimations(boolean b) {
        setProperty(ANIMATE_GAMEPLAY, b);
    }

    public boolean isCustomCardImagesPath() {
        return getString(CARD_IMAGES_PATH, "").isEmpty() == false;
    }

    public Path getCardImagesPath() {
        String setting = getString(CARD_IMAGES_PATH, "");
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

    public boolean isCustomBackground() {
        return getBoolean(CUSTOM_BACKGROUND, false);
    }

    public void setCustomBackground(boolean isCustomBackground) {
        setProperty(CUSTOM_BACKGROUND, isCustomBackground);
    }

    public boolean isMissingFiles() {
        return isMissingFiles;
    }
    public void setIsMissingFiles(final boolean b) {
        isMissingFiles = b;
    }

    /**
     * Gets fully qualified path of last deck file to be opened in the deck editor.
     *
     * @return path object or null if setting is missing.
     */
    public Path getMostRecentDeckFilePath() {
        String setting = getString(RECENT_DECK, "").trim();
        return !setting.isEmpty() ? Paths.get(setting) : null;
    }

    public void setMostRecentDeckFilename(String filename) {
        setProperty(RECENT_DECK, filename.trim());
    }

    public boolean isMaximized() {
        return getBoolean(MAXIMIZED, false);
    }

    public void setMaximized(final boolean maximized) {
        setProperty(MAXIMIZED, maximized);
    }

    public String getTheme() {
        return getString(THEME, "felt");
    }

    public void setTheme(String theme) {
        setProperty(THEME, theme);
    }

    public String getAvatar() {
        return getString(AVATAR, "legend");
    }

    public String getHighlight() {
        return getString(HIGHLIGHT, "theme");
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
        return getString(FIREMIND_ACCESS_TOKEN, "");
    }

    public void setFiremindAccessToken(String firemindAccessToken) {
        setProperty(FIREMIND_ACCESS_TOKEN, firemindAccessToken);
    }

    public boolean getSkipSingle() {
        return getBoolean(SKIP_SINGLE, true);
    }

    public void setSkipSingle(boolean skipSingle) {
        setProperty(SKIP_SINGLE, skipSingle);
    }

    public boolean getAlwaysPass() {
        return getBoolean(ALWAYS_PASS, true);
    }

    public void setAlwaysPass(boolean alwaysPass) {
        setProperty(ALWAYS_PASS, alwaysPass);
    }

    public boolean getSmartTarget() {
        return getBoolean(SMART_TARGET, false);
    }

    public void setSmartTarget(boolean smartTarget) {
        setProperty(SMART_TARGET, smartTarget);
    }

    public int getPopupDelay() {
        return getInteger(POPUP_DELAY, 300);
    }

    public void setPopupDelay(int popupDelay) {
        setProperty(POPUP_DELAY, popupDelay);
    }

    public int getMessageDelay() {
        return getInteger(MESSAGE_DELAY, 2000);
    }

    public void setMessageDelay(int messageDelay) {
        setProperty(MESSAGE_DELAY, messageDelay);
    }

    public boolean isTouchscreen() {
        return getBoolean(TOUCHSCREEN, false);
    }

    public void setTouchscreen(boolean touchscreen) {
        setProperty(TOUCHSCREEN, touchscreen);
    }

    public boolean isMouseWheelPopup() {
        return getBoolean(MOUSEWHEEL_POPUP, false);
    }

    public void setMouseWheelPopup(boolean mouseWheelPopup) {
        setProperty(MOUSEWHEEL_POPUP, mouseWheelPopup);
    }

    public boolean isFullScreen() {
        return getBoolean(FULLSCREEN, false);
    }
    public void setFullScreen(boolean b) {
        setProperty(FULLSCREEN, b);
    }

    public boolean isPreviewCardOnSelect() {
        return getBoolean(PREVIEW_CARD_ON_SELECT, true);
    }

    public void setPreviewCardOnSelect(boolean b) {
        setProperty(PREVIEW_CARD_ON_SELECT, b);
    }

    public boolean isLogMessagesVisible() {
        return getBoolean(SHOW_LOG_MESSAGES, true);
    }

    public void setLogMessagesVisible(boolean b) {
        setProperty(SHOW_LOG_MESSAGES, b);
    }

    public boolean showMulliganScreen() {
        return getBoolean(MULLIGAN_SCREEN, true);
    }

    public void setShowMulliganScreen(boolean b) {
        setProperty(MULLIGAN_SCREEN, b);
    }

    public int getNewTurnAlertDuration() {
        return getInteger(NEWTURN_ALERT_DURATION, 3000);
    }

    public void setNewTurnAlertDuration(int millisecs) {
        setProperty(NEWTURN_ALERT_DURATION, millisecs);
    }

    public int getLandPreviewDuration() {
        return getInteger(LAND_PREVIEW_DURATION, 5000);
    }

    public void setLandPreviewDuration(final int millisecs) {
        setProperty(LAND_PREVIEW_DURATION, millisecs);
    }

    public int getNonLandPreviewDuration() {
        return getInteger(NONLAND_PREVIEW_DURATION, 10000);
    }

    public void setNonLandPreviewDuration(int millisecs) {
        setProperty(NONLAND_PREVIEW_DURATION, millisecs);
    }

    public boolean isSplitViewDeckEditor() {
        return getBoolean(SPLITVIEW_DECKEDITOR, false);
    }

    public void setIsSplitViewDeckEditor(boolean b) {
        setProperty(SPLITVIEW_DECKEDITOR, b);
    }

    public String getIgnoredVersionAlert() {
        return getString(IGNORED_VERSION_ALERT, "");
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
        return getInteger(OVERLAY_PERMANENT_MIN_HEIGHT, 30);
    }

    public boolean isGamePausedOnPopup() {
        return getBoolean(PAUSE_GAME_POPUP, false);
    }

    public void setIsGamePausedOnPopup(boolean b) {
        setProperty(PAUSE_GAME_POPUP, b);
    }

    public String getDuelSidebarLayout() {
        return getString(DUEL_SIDEBAR_LAYOUT, "LOGSTACK,PLAYER2,TURNINFO,PLAYER1");
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
            return df.parse(getString(PLAYABLE_DOWNLOAD_DATE, "1970-01-01"));
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
            return df.parse(getString(MISSING_DOWNLOAD_DATE, "1970-01-01"));
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setUnimplementedImagesDownloadDate(final Date runDate) {
        SimpleDateFormat df = new SimpleDateFormat(CardProperty.IMAGE_UPDATED_FORMAT);
        setProperty(MISSING_DOWNLOAD_DATE, df.format(runDate));
    }

    public boolean getHideAiActionPrompt() {
        return getBoolean(HIDE_AI_ACTION_PROMPT, false);
    }

    public void setHideAiActionPrompt(boolean b) {
        setProperty(HIDE_AI_ACTION_PROMPT, b);
    }

    public Color getRolloverColor() {
        return new Color(getInteger(ROLLOVER_COLOR, Color.YELLOW.getRGB()));
    }

    public void setRolloverColor(Color aColor) {
        setProperty(ROLLOVER_COLOR, aColor.getRGB());
    }

    public int getUiVolume() {
        return getInteger(UI_VOLUME, 80);
    }

    public void setUiVolume(int vol) {
        setProperty(UI_VOLUME, vol);
    }

    public String getTranslation() {
        return getString(TRANSLATION, DEFAULT_TRANSLATION);
    }

    public void setTranslation(String value) {
        setProperty(TRANSLATION, value);
    }

    public MessageStyle getLogMessageStyle() {
        return MessageStyle.valueOf(
            getString(LOG_MESSAGE_STYLE, MessageStyle.PLAIN.name())
        );
    }

    public void setLogMessageStyle(MessageStyle aStyle) {
        setProperty(LOG_MESSAGE_STYLE, aStyle.name());
    }

    private int getInteger(String key, int defaultValue) {
        return Integer.parseInt(settings.getProperty(key, String.valueOf(defaultValue)));
    }

    private long getLong(String key, long defaultValue) {
        return Long.parseLong(settings.getProperty(key, String.valueOf(defaultValue)));
    }

    private boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(settings.getProperty(key, String.valueOf(defaultValue)));
    }

    private String getString(String key, String defaultValue) {
        return settings.getProperty(key, defaultValue);
    }

    public void load() {
        settings = FileIO.toProp(getConfigFile());
        CardsTableStyle.setStyle(getInteger(CARDS_TABLE_STYLE, CardsTableStyle.getStyle().ordinal()));
        ExplorerScreenLayout.setLayout(getInteger(EXPLORER_LAYOUT, ExplorerScreenLayout.getLayout().ordinal()));
        HandZoneLayout.setLayout(getInteger(HAND_ZONE_LAYOUT, HandZoneLayout.getLayout().ordinal()));
        AnimationFx.setFlags(getLong(ANIMATION_FLAGS, AnimationFx.getFlags()));
    }

    private void setProperty(String key, int value) {
        settings.setProperty(key, String.valueOf(value));
    }

    private void setProperty(String key, long value) {
        settings.setProperty(key, String.valueOf(value));
    }

    private void setProperty(String key, boolean value) {
        settings.setProperty(key, String.valueOf(value));
    }

    private void setProperty(String key, String value) {
        settings.setProperty(key, value);
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

    private static File getConfigFile() {
        return MagicFileSystem.getDataPath().resolve(CONFIG_FILENAME).toFile();
    }

    public static GeneralConfig getInstance() {
        return INSTANCE;
    }

    public ImageSizePresets getPreferredImageSize() {
        return ImageSizePresets.valueOf(
            getString(PREF_IMAGE_SIZE, ImageSizePresets.SIZE_ORIGINAL.name())
        );
    }

    public void setPreferredImageSize(ImageSizePresets preset) {
        setProperty(PREF_IMAGE_SIZE, preset.name());
    }

    public CardTextLanguage getCardTextLanguage() {
        return CardTextLanguage.valueOf(
            getString(CARD_TEXT_LANG, CardTextLanguage.ENGLISH.name())
        );
    }

    public void setCardTextLanguage(CardTextLanguage aLang) {
        setProperty(CARD_TEXT_LANG, aLang.name());
    }

    public int getGameVolume() {
        return getInteger(GAME_VOLUME, 80);
    }

    public void setGameVolume(int value) {
        setProperty(GAME_VOLUME, value);
    }

    public boolean getImagesOnDemand() {
        return getBoolean(IMAGES_ON_DEMAND, false);
    }

    public void setImagesOnDemand(boolean b) {
        setProperty(IMAGES_ON_DEMAND, b);
    }

    public Rectangle getSizableFrameBounds() {
        return new Rectangle(
            getInteger(FRAME_LEFT, -1),
            getInteger(FRAME_TOP, -1),
            getInteger(FRAME_WIDTH, DEFAULT_FRAME_WIDTH),
            getInteger(FRAME_HEIGHT, DEFAULT_FRAME_HEIGHT)
        );
    }

    public void setSizableFrameBounds(Rectangle aRect) {
        setProperty(FRAME_LEFT, aRect.x);
        setProperty(FRAME_TOP, aRect.y);
        setProperty(FRAME_WIDTH, aRect.width);
        setProperty(FRAME_HEIGHT, aRect.height);
    }

    public void setSizableFrameBounds(Point aPoint, Dimension aSize) {
        setProperty(FRAME_LEFT, aPoint.x);
        setProperty(FRAME_TOP, aPoint.y);
        setProperty(FRAME_WIDTH, aSize.width);
        setProperty(FRAME_HEIGHT, aSize.height);
    }

    public void setCustomScrollBar(boolean b) {
        setProperty(CUSTOM_SCROLLBAR, b);
    }

    public boolean isCustomScrollBar() {
        return getBoolean(CUSTOM_SCROLLBAR, true);
    }

    public void setKeywordsSettings(String text) {
        setProperty(KEYWORDS_SCREEN, text);
    }

    public String getKeywordsSettings() {
        return getString(KEYWORDS_SCREEN, "");
    }

    public CardImageDisplayMode getCardImageDisplayMode() {
        return CardImageDisplayMode.valueOf(
            getString(CARD_DISPLAY_MODE, CardImageDisplayMode.PRINTED.name())
        );
    }

    public void setCardImageDisplayMode(CardImageDisplayMode newMode) {
        setProperty(CARD_DISPLAY_MODE, newMode.name());
    }

    public void setGameStatsEnabled(boolean b) {
        setProperty(GAME_STATS, b);
    }

    public boolean isGameStatsEnabled() {
        return getBoolean(GAME_STATS, true);
    }

    public static boolean isGameStatsOn() {
        return getInstance().isGameStatsEnabled();
    }

    public String getCardFlowScreenSettings() {
        return getString(CARDFLOW_SCREEN_SETTINGS, "");
    }

    public void setCardFlowScreenSettings(String settings) {
        setProperty(CARDFLOW_SCREEN_SETTINGS, settings);
    }

    public boolean useCustomFonts() {
        return getBoolean(CUSTOM_FONTS, true);
    }

}
