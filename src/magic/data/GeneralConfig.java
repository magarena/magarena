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

public class GeneralConfig {

    private static final GeneralConfig INSTANCE = new GeneralConfig();

    public static final String CONFIG_FILENAME="general.cfg";

    private Properties settings;

    private boolean isMissingFiles = false;

    private static final String FRAME_LEFT = "left";
    private int frameLeft = -1;

    private static final String FRAME_TOP = "top";
    private int frameTop = -1;

    private static final String FRAME_WIDTH = "width";
    public static final int DEFAULT_FRAME_WIDTH = 1024;
    private int frameWidth = DEFAULT_FRAME_WIDTH;

    private static final String FRAME_HEIGHT = "height";
    public static final int DEFAULT_FRAME_HEIGHT = 600;
    private int frameHeight = DEFAULT_FRAME_HEIGHT;

    private static final String MAXIMIZED="maximized";
    private boolean maximized=false;

    private static final String THEME="theme";
    private String theme="felt";

    private static final String AVATAR="avatar";
    private String avatar="legend";

    private static final String HIGHLIGHT = "highlight";
    private String highlight = "theme";

    private static final String SKIP_SINGLE="single";
    private boolean skipSingle = true;

    private static final String ALWAYS_PASS="pass";
    private boolean alwaysPass = true;

    private static final String SMART_TARGET="target";
    private boolean smartTarget = false;

    private static final String POPUP_DELAY="popup";
    private int popupDelay = 300;

    private static final String MESSAGE_DELAY = "message";
    private int messageDelay = 2000;

    private static final String TOUCHSCREEN = "touchscreen";
    private boolean touchscreen = false;

    private static final String MOUSEWHEEL_POPUP = "mousewheel";
    private boolean mouseWheelPopup = false;

    private static final String FULLSCREEN = "fullScreen";
    private boolean fullScreen = false;

    private static final String PREVIEW_CARD_ON_SELECT = "previewCardOnSelect";
    private boolean previewCardOnSelect = true;

    private static final String SHOW_LOG_MESSAGES = "showLogMessages";
    private boolean showLogMessages = true;

    private static final String MULLIGAN_SCREEN = "mulliganScreen";
    private boolean isMulliganScreenActive = true;

    private static final String RECENT_DECK = "MostRecentDeckFilename";
    private String mostRecentDeckFilename = "";

    private static final String CUSTOM_BACKGROUND = "customBackground";
    private boolean isCustomBackground = false;

    private static final String CARD_IMAGES_PATH = "cardImagesPath";
    private String cardImagesPath = "";

    private static final String ANIMATE_GAMEPLAY = "animateGameplay";
    private boolean animateGameplay = true;

    private static final String ANIMATION_FLAGS = "animationFlags";

    private static final String DECK_FILE_MAX_LINES = "deckFileMaxLines";
    private int deckFileMaxLines = 500;

    private static final String PROXY_SETTINGS = "proxySettings";
    private String proxySettings = "";

    private static final String FIREMIND_ACCESS_TOKEN = "firemindAccessToken";
    private String firemindAccessToken = "";

    private static final String NEWTURN_ALERT_DURATION = "newTurnAlertDuration";
    private int newTurnAlertDuration = 3000; // msecs

    private static final String LAND_PREVIEW_DURATION = "landPreviewDuration";
    private int landPreviewDuration = 5000; // msecs

    private static final String NONLAND_PREVIEW_DURATION = "nonLandPreviewDuration";
    private int nonLandPreviewDuration = 10000; // msecs

    private static final String SPLITVIEW_DECKEDITOR = "splitViewDeckEditor";
    private boolean isSplitViewDeckEditor = false;

    private static final String OVERLAY_PERMANENT_MIN_HEIGHT = "overlayPermanentMinHeight";
    private int overlayPermanentMinHeight = 30; // pixels

    private static final String IGNORED_VERSION_ALERT = "ignoredVersionAlert";
    private String ignoredVersionAlert = "";

    private static final String PAUSE_GAME_POPUP = "pauseGamePopup";
    private boolean isGamePausedOnPopup = false;

    private static final String MISSING_DOWNLOAD_DATE = "missingImagesDownloadDate";
    private String unimplementedImagesDownloadDate = "1970-01-01";

    private static final String PLAYABLE_DOWNLOAD_DATE = "imageDownloaderRunDate";
    private String playableImagesDownloadDate = "1970-01-01";

    private static final String DUEL_SIDEBAR_LAYOUT ="duelSidebarLayout";
    private String duelSidebarLayout = "LOGSTACK,PLAYER2,TURNINFO,PLAYER1";

    private static final String HIDE_AI_ACTION_PROMPT ="hideAiActionPrompt";
    private boolean hideAiActionPrompt = false;

    private static final String ROLLOVER_COLOR ="rolloverColor";
    private Color rolloverColor = Color.YELLOW;

    private static final String UI_VOLUME = "uiSoundVolume";
    private int uiVolume = 80;

    private static final String GAME_VOLUME = "gameVolume";
    private int gameVolume = 80;

    private static final String TRANSLATION = "translation";
    public static final String DEFAULT_TRANSLATION = "";
    private String translation = DEFAULT_TRANSLATION;

    private static final String LOG_MESSAGE_STYLE = "logMessageStyle";
    private MessageStyle logMessageStyle = MessageStyle.PLAIN;

    private static final String PREF_IMAGE_SIZE = "prefImageSize";
    private ImageSizePresets preferredImageSize = ImageSizePresets.SIZE_ORIGINAL;

    private static final String CARD_TEXT_LANG = "cardTextLanguage";
    private CardTextLanguage cardTextLanguage = CardTextLanguage.ENGLISH;

    private static final String IMAGES_ON_DEMAND = "imagesOnDemand";
    private boolean imagesOnDemand = false;

    private static final String CUSTOM_SCROLLBAR = "customScrollBar";
    private boolean isCustomScrollBar = true;

    private static final String KEYWORDS_SCREEN = "keywordsScreen";
    private String keywordsScreen;

    private static final String CARD_DISPLAY_MODE = "cardImageDisplayMode";
    private CardImageDisplayMode cardDisplayMode = CardImageDisplayMode.PRINTED;

    private boolean isStatsVisible = true;

    private static final String GAME_STATS = "gameStats";
    private boolean logGameStats = true;

    private static final String CARDFLOW_SCREEN_SETTINGS = "cardflow.screen";
    private String cardFlowScreenSettings;

    private static final String CUSTOM_FONTS = "custom.fonts";
    private boolean useCustomFonts = true;

    private static final String EXPLORER_LAYOUT = "explorer.layout";
    private static final String HAND_ZONE_LAYOUT = "hand.zone.layout";
    private static final String CARDS_TABLE_STYLE = "explorer.table.style";

    private GeneralConfig() { }

    public Proxy getProxy() {
        final String DELIM = "\\|";
        if (!proxySettings.isEmpty() && proxySettings.split(DELIM).length == 3) {
            Proxy.Type proxyType = Proxy.Type.valueOf(proxySettings.split(DELIM)[0]);
            int port = Integer.parseInt(proxySettings.split(DELIM)[1]);
            String urlAddress = proxySettings.split(DELIM)[2];
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
            proxySettings = sb.toString();
        } else {
            proxySettings = "";
        }
    }

    public String getProxySettings() {
        return proxySettings;
    }

    public int getDeckFileMaxLines() {
        return deckFileMaxLines;
    }

    public boolean showGameplayAnimations() {
        return animateGameplay;
    }

    public boolean getAnimateGameplay() {
        return animateGameplay;
    }
    public void setAnimateGameplay(boolean b) {
        animateGameplay = b;
    }

    public Path getCardImagesPath() {
        if (cardImagesPath.isEmpty()) {
            return MagicFileSystem.getDataPath(MagicFileSystem.DataPath.IMAGES);
        } else {
            return Paths.get(cardImagesPath);
        }
    }
    public void setCardImagesPath(final Path p) {
        if (MagicFileSystem.directoryContains(MagicFileSystem.INSTALL_PATH, p)) {
            this.cardImagesPath = "";
        } else {
            this.cardImagesPath = p.toAbsolutePath().toString();
        }
    }

    public boolean isCustomBackground() {
        return isCustomBackground;
    }
    public void setCustomBackground(boolean isCustomBackground) {
        this.isCustomBackground = isCustomBackground;
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
        return !mostRecentDeckFilename.isEmpty() ? Paths.get(mostRecentDeckFilename) : null;
    }
    public void setMostRecentDeckFilename(final String filename) {
        mostRecentDeckFilename = filename.trim();
    }

    public boolean isMaximized() {
        return maximized;
    }

    public void setMaximized(final boolean maximized) {
        this.maximized=maximized;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(final String theme) {
        this.theme=theme;
    }

    public String getAvatar() {
        return avatar;
    }

    public boolean isHighlightNone() {
        return "none".equals(highlight);
    }

    public boolean isHighlightOverlay() {
        return "overlay".equals(highlight);
    }

    public boolean isHighlightTheme() {
        return "theme".equals(highlight);
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(final String highlight) {
        this.highlight = highlight;
    }

    public String getFiremindAccessToken() {
        return firemindAccessToken;
    }

    public void setFiremindAccessToken(final String firemindAccessToken) {
        this.firemindAccessToken = firemindAccessToken;
    }

    public boolean getSkipSingle() {
        return skipSingle;
    }

    public void setSkipSingle(final boolean skipSingle) {
        this.skipSingle=skipSingle;
    }

    public boolean getAlwaysPass() {
        return alwaysPass;
    }

    public void setAlwaysPass(final boolean alwaysPass) {
        this.alwaysPass=alwaysPass;
    }

    public boolean getSmartTarget() {
        return smartTarget;
    }

    public void setSmartTarget(final boolean smartTarget) {
        this.smartTarget=smartTarget;
    }

    public int getPopupDelay() {
        return popupDelay;
    }

    public void setPopupDelay(final int popupDelay) {
        this.popupDelay=popupDelay;
    }

    public int getMessageDelay() {
        return messageDelay;
    }

    public void setMessageDelay(final int messageDelay) {
        this.messageDelay = messageDelay;
    }

    public boolean isTouchscreen() {
        return touchscreen;
    }

    public void setTouchscreen(final boolean touchscreen) {
        this.touchscreen = touchscreen;
    }

    public boolean isMouseWheelPopup() {
        return mouseWheelPopup;
    }

    public void setMouseWheelPopup(final boolean mouseWheelPopup) {
        this.mouseWheelPopup = mouseWheelPopup;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }
    public void setFullScreen(final boolean b) {
        this.fullScreen = b;
    }

    public boolean isPreviewCardOnSelect() {
        return previewCardOnSelect;
    }
    public void setPreviewCardOnSelect(final boolean b) {
        this.previewCardOnSelect = b;
    }

    public boolean isLogMessagesVisible() {
        return showLogMessages;
    }
    public void setLogMessagesVisible(final boolean b) {
        showLogMessages = b;
    }

    public boolean showMulliganScreen() {
        return isMulliganScreenActive;
    }

    public void setShowMulliganScreen(final boolean b) {
        isMulliganScreenActive = b;
    }

    public int getNewTurnAlertDuration() {
        return newTurnAlertDuration;
    }
    public void setNewTurnAlertDuration(final int value) {
        newTurnAlertDuration = value;
    }

    public int getLandPreviewDuration() {
        return landPreviewDuration;
    }
    public void setLandPreviewDuration(final int value) {
        landPreviewDuration = value;
    }

    public int getNonLandPreviewDuration() {
        return nonLandPreviewDuration;
    }
    public void setNonLandPreviewDuration(final int value) {
        nonLandPreviewDuration = value;
    }

    public boolean isSplitViewDeckEditor() {
        return isSplitViewDeckEditor;
    }
    public void setIsSplitViewDeckEditor(boolean b) {
        isSplitViewDeckEditor = b;
    }

    public String getIgnoredVersionAlert() {
        return ignoredVersionAlert;
    }
    public void setIgnoredVersionAlert(final String version) {
        ignoredVersionAlert = version;
    }

    /**
     * Minimum height of card image on which overlays such as P/T,
     * ability icons, etc should be shown.
     * <p>
     * Non-user: requires manual update of config file to change.
     */
    public int getOverlayMinimumHeight() {
        return overlayPermanentMinHeight;
    }

    public boolean isGamePausedOnPopup() {
        return isGamePausedOnPopup;
    }
    public void setIsGamePausedOnPopup(final boolean b) {
        isGamePausedOnPopup = b;
    }

    public String getDuelSidebarLayout() {
        return duelSidebarLayout;
    }
    public void setDuelSidebarLayout(final String layout) {
        duelSidebarLayout = layout;
    }

    /**
     * Gets the last date playable images were downloaded.
     * <p>
     * If missing then date is set to "1970-01-01".
     */
    public Date getPlayableImagesDownloadDate() {
        try {
            final SimpleDateFormat df = new SimpleDateFormat(CardProperty.IMAGE_UPDATED_FORMAT);
            return df.parse(playableImagesDownloadDate);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void setPlayableImagesDownloadDate(final Date runDate) {
        final SimpleDateFormat df = new SimpleDateFormat(CardProperty.IMAGE_UPDATED_FORMAT);
        playableImagesDownloadDate = df.format(runDate);
    }

    /**
     * Gets the last date unimplemented images were downloaded.
     * <p>
     * If missing then date is set to "1970-01-01".
     */
    public Date getUnimplementedImagesDownloadDate() {
        try {
            final SimpleDateFormat df = new SimpleDateFormat(CardProperty.IMAGE_UPDATED_FORMAT);
            return df.parse(unimplementedImagesDownloadDate);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void setUnimplementedImagesDownloadDate(final Date runDate) {
        final SimpleDateFormat df = new SimpleDateFormat(CardProperty.IMAGE_UPDATED_FORMAT);
        unimplementedImagesDownloadDate = df.format(runDate);
    }

    public boolean getHideAiActionPrompt() {
        return hideAiActionPrompt;
    }
    public void setHideAiActionPrompt(final boolean b) {
        hideAiActionPrompt = b;
    }

    public Color getRolloverColor() {
        return rolloverColor;
    }
    public void setRolloverColor(final Color aColor) {
        rolloverColor = aColor;
    }

    public int getUiVolume() {
        return uiVolume;
    }
    public void setUiVolume(final int aInt) {
        uiVolume = aInt;
    }

    public String getTranslation() {
        return translation;
    }
    public void setTranslation(final String aString) {
        translation = aString;
    }

    public MessageStyle getLogMessageStyle() {
        return logMessageStyle;
    }
    public void setLogMessageStyle(MessageStyle aStyle) {
        logMessageStyle = aStyle;
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
        frameLeft = getInteger(FRAME_LEFT, frameLeft);
        frameTop = getInteger(FRAME_TOP, frameTop);
        frameWidth = getInteger(FRAME_WIDTH, frameWidth);
        frameHeight = getInteger(FRAME_HEIGHT, frameHeight);
        maximized = getBoolean(MAXIMIZED, maximized);
        theme = getString(THEME, theme);
        avatar = getString(AVATAR, avatar);
        highlight = getString(HIGHLIGHT, highlight);
        skipSingle = getBoolean(SKIP_SINGLE, skipSingle);
        alwaysPass = getBoolean(ALWAYS_PASS, alwaysPass);
        smartTarget = getBoolean(SMART_TARGET, smartTarget);
        popupDelay = getInteger(POPUP_DELAY, popupDelay);
        messageDelay = getInteger(MESSAGE_DELAY, messageDelay);
        touchscreen = getBoolean(TOUCHSCREEN, touchscreen);
        mouseWheelPopup = getBoolean(MOUSEWHEEL_POPUP, mouseWheelPopup);
        fullScreen = getBoolean(FULLSCREEN, fullScreen);
        previewCardOnSelect = getBoolean(PREVIEW_CARD_ON_SELECT, previewCardOnSelect);
        showLogMessages = getBoolean(SHOW_LOG_MESSAGES, showLogMessages);
        isMulliganScreenActive = getBoolean(MULLIGAN_SCREEN, isMulliganScreenActive);
        mostRecentDeckFilename = getString(RECENT_DECK, mostRecentDeckFilename).trim();
        isCustomBackground = getBoolean(CUSTOM_BACKGROUND, isCustomBackground);
        cardImagesPath = getString(CARD_IMAGES_PATH, cardImagesPath);
        animateGameplay = getBoolean(ANIMATE_GAMEPLAY, animateGameplay);
        deckFileMaxLines = getInteger(DECK_FILE_MAX_LINES, deckFileMaxLines);
        proxySettings = getString(PROXY_SETTINGS, proxySettings).trim();
        firemindAccessToken = getString(FIREMIND_ACCESS_TOKEN, firemindAccessToken);
        newTurnAlertDuration = getInteger(NEWTURN_ALERT_DURATION, newTurnAlertDuration);
        landPreviewDuration = getInteger(LAND_PREVIEW_DURATION, landPreviewDuration);
        nonLandPreviewDuration = getInteger(NONLAND_PREVIEW_DURATION, nonLandPreviewDuration);
        isSplitViewDeckEditor = getBoolean(SPLITVIEW_DECKEDITOR, isSplitViewDeckEditor);
        overlayPermanentMinHeight = getInteger(OVERLAY_PERMANENT_MIN_HEIGHT, overlayPermanentMinHeight);
        ignoredVersionAlert = getString(IGNORED_VERSION_ALERT, ignoredVersionAlert);
        isGamePausedOnPopup = getBoolean(PAUSE_GAME_POPUP, isGamePausedOnPopup);
        unimplementedImagesDownloadDate = getString(MISSING_DOWNLOAD_DATE, unimplementedImagesDownloadDate);
        playableImagesDownloadDate = getString(PLAYABLE_DOWNLOAD_DATE, playableImagesDownloadDate);
        duelSidebarLayout = getString(DUEL_SIDEBAR_LAYOUT, duelSidebarLayout);
        hideAiActionPrompt = getBoolean(HIDE_AI_ACTION_PROMPT, hideAiActionPrompt);
        rolloverColor = new Color(getInteger(ROLLOVER_COLOR, rolloverColor.getRGB()));
        uiVolume = getInteger(UI_VOLUME, uiVolume);
        translation = getString(TRANSLATION, translation);
        logMessageStyle = MessageStyle.valueOf(getString(LOG_MESSAGE_STYLE, logMessageStyle.name()));
        preferredImageSize = ImageSizePresets.valueOf(getString(PREF_IMAGE_SIZE, preferredImageSize.name()));
        cardTextLanguage = CardTextLanguage.valueOf(getString(CARD_TEXT_LANG, cardTextLanguage.name()));
        gameVolume = getInteger(GAME_VOLUME, gameVolume);
        imagesOnDemand = getBoolean(IMAGES_ON_DEMAND, imagesOnDemand);
        isCustomScrollBar = getBoolean(CUSTOM_SCROLLBAR, isCustomScrollBar);
        keywordsScreen =  getString(KEYWORDS_SCREEN, "");
        cardDisplayMode = CardImageDisplayMode.valueOf(getString(CARD_DISPLAY_MODE, cardDisplayMode.name()));
        logGameStats = getBoolean(GAME_STATS, logGameStats);
        cardFlowScreenSettings = getString(CARDFLOW_SCREEN_SETTINGS, "");
        useCustomFonts = getBoolean(CUSTOM_FONTS, useCustomFonts);
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
        setProperty(FRAME_LEFT, frameLeft);
        setProperty(FRAME_TOP, frameTop);
        setProperty(FRAME_WIDTH, frameWidth);
        setProperty(FRAME_HEIGHT, frameHeight);
        setProperty(MAXIMIZED, maximized);
        setProperty(THEME, theme);
        setProperty(AVATAR, avatar);
        setProperty(HIGHLIGHT, highlight);
        setProperty(SKIP_SINGLE, skipSingle);
        setProperty(ALWAYS_PASS, alwaysPass);
        setProperty(SMART_TARGET, smartTarget);
        setProperty(POPUP_DELAY, popupDelay);
        setProperty(MESSAGE_DELAY, messageDelay);
        setProperty(TOUCHSCREEN, touchscreen);
        setProperty(MOUSEWHEEL_POPUP, mouseWheelPopup);
        setProperty(FULLSCREEN, fullScreen);
        setProperty(PREVIEW_CARD_ON_SELECT, previewCardOnSelect);
        setProperty(SHOW_LOG_MESSAGES, showLogMessages);
        setProperty(MULLIGAN_SCREEN, isMulliganScreenActive);
        setProperty(RECENT_DECK, mostRecentDeckFilename);
        setProperty(CUSTOM_BACKGROUND, isCustomBackground);
        setProperty(CARD_IMAGES_PATH, cardImagesPath);
        setProperty(ANIMATE_GAMEPLAY, animateGameplay);
        setProperty(PROXY_SETTINGS, proxySettings);
        setProperty(FIREMIND_ACCESS_TOKEN, firemindAccessToken);
        setProperty(NEWTURN_ALERT_DURATION, newTurnAlertDuration);
        setProperty(LAND_PREVIEW_DURATION, landPreviewDuration);
        setProperty(NONLAND_PREVIEW_DURATION, nonLandPreviewDuration);
        setProperty(SPLITVIEW_DECKEDITOR, isSplitViewDeckEditor);
        setProperty(IGNORED_VERSION_ALERT, ignoredVersionAlert);
        setProperty(PAUSE_GAME_POPUP, isGamePausedOnPopup);
        setProperty(MISSING_DOWNLOAD_DATE, unimplementedImagesDownloadDate);
        setProperty(PLAYABLE_DOWNLOAD_DATE, playableImagesDownloadDate);
        setProperty(DUEL_SIDEBAR_LAYOUT, duelSidebarLayout);
        setProperty(HIDE_AI_ACTION_PROMPT, hideAiActionPrompt);
        setProperty(ROLLOVER_COLOR, rolloverColor.getRGB());
        setProperty(UI_VOLUME, uiVolume);
        setProperty(TRANSLATION, translation);
        setProperty(LOG_MESSAGE_STYLE, logMessageStyle.name());
        setProperty(ANIMATION_FLAGS, AnimationFx.getFlags());
        setProperty(PREF_IMAGE_SIZE, preferredImageSize.name());
        setProperty(CARD_TEXT_LANG, cardTextLanguage.name());
        setProperty(GAME_VOLUME, gameVolume);
        setProperty(IMAGES_ON_DEMAND, imagesOnDemand);
        setProperty(CUSTOM_SCROLLBAR, isCustomScrollBar);
        setProperty(KEYWORDS_SCREEN, keywordsScreen);
        setProperty(CARD_DISPLAY_MODE, cardDisplayMode.name());
        setProperty(GAME_STATS, logGameStats);
        setProperty(CARDFLOW_SCREEN_SETTINGS, cardFlowScreenSettings);
        setProperty(CUSTOM_FONTS, useCustomFonts);
        setProperty(EXPLORER_LAYOUT, ExplorerScreenLayout.getLayout().ordinal());
        setProperty(HAND_ZONE_LAYOUT, HandZoneLayout.getLayout().ordinal());
        setProperty(CARDS_TABLE_STYLE, CardsTableStyle.getStyle().ordinal());
    }

    public void save() {
        setProperties();
        try {
            FileIO.toFile(getConfigFile(), settings, "Magarena settings");
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

    public boolean isCustomCardImagesPath() {
        return cardImagesPath.isEmpty() == false;
    }

    public ImageSizePresets getPreferredImageSize() {
        return preferredImageSize;
    }

    public void setPreferredImageSize(ImageSizePresets preset) {
        this.preferredImageSize = preset;
    }

    public CardTextLanguage getCardTextLanguage() {
        return cardTextLanguage;
    }

    public void setCardTextLanguage(CardTextLanguage aLang) {
        this.cardTextLanguage = aLang;
    }

    public int getGameVolume() {
        return gameVolume;
    }

    public void setGameVolume(int value) {
        gameVolume = value;
    }

    public boolean isStatsVisible() {
        return isStatsVisible;
    }

    public void setStatsVisible(boolean b) {
        isStatsVisible = b;
    }

    public boolean getImagesOnDemand() {
        return imagesOnDemand;
    }

    public void setImagesOnDemand(boolean b) {
        imagesOnDemand = b;
    }

    public Rectangle getSizableFrameBounds() {
        return new Rectangle(frameLeft, frameTop, frameWidth, frameHeight);
    }

    public void setSizableFrameBounds(Rectangle aRect) {
        frameLeft = aRect.x;
        frameTop = aRect.y;
        frameWidth = aRect.width;
        frameHeight = aRect.height;
    }

    public void setSizableFrameBounds(Point aPoint, Dimension aSize) {
        frameLeft = aPoint.x;
        frameTop = aPoint.y;
        frameWidth = aSize.width;
        frameHeight = aSize.height;
    }

    public void setCustomScrollBar(boolean b) {
        isCustomScrollBar = b;
    }

    public boolean isCustomScrollBar() {
        return isCustomScrollBar;
    }

    public void setKeywordsSettings(String text) {
        keywordsScreen = text;
    }

    public String getKeywordsSettings() {
        return keywordsScreen;
    }

    public void set(String name, int value) {
        settings.setProperty(name, Integer.toString(value));
    }

    public int getInt(String name, int value) {
        return Integer.parseInt(settings.getProperty(name, Integer.toString(value)));
    }

    public CardImageDisplayMode getCardImageDisplayMode() {
        return cardDisplayMode;
    }

    public void setCardImageDisplayMode(CardImageDisplayMode newMode) {
        cardDisplayMode = newMode;
    }

    public void setGameStatsEnabled(boolean b) {
        logGameStats = b;
    }

    public boolean isGameStatsEnabled() {
        return logGameStats;
    }

    public static boolean isGameStatsOn() {
        return getInstance().isGameStatsEnabled();
    }

    public String getCardFlowScreenSettings() {
        return cardFlowScreenSettings;
    }

    public void setCardFlowScreenSettings(String settings) {
        cardFlowScreenSettings = settings;
    }

    public boolean useCustomFonts() {
        return useCustomFonts;
    }

    public void setUseCustomFonts(boolean b) {
        useCustomFonts = b;
    }

}
