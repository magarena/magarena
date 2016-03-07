package magic.data;

import magic.utility.FileIO;
import java.awt.Color;
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
import magic.ui.duel.animation.AnimationFx;
import magic.ui.message.MessageStyle;
import magic.ui.prefs.ImageSizePresets;
import magic.utility.MagicFileSystem;
import magic.utility.SortedProperties;

public class GeneralConfig {

    private static final GeneralConfig INSTANCE = new GeneralConfig();

    public static final String CONFIG_FILENAME="general.cfg";

    private boolean isMissingFiles = false;

    private static final String LEFT="left";
    private int left   = -1;

    private static final String TOP="top";
    private int top    = 0;

    private static final String WIDTH="width";
    public static final int DEFAULT_WIDTH=1024;
    private int width  = DEFAULT_WIDTH;

    private static final String HEIGHT="height";
    public static final int DEFAULT_HEIGHT=600;
    private int height = DEFAULT_HEIGHT;

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

    private static final String SHOW_MISSING_CARD_DATA = "showMissingCardData";
    private boolean showMissingCardData = true;

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
    private String missingImagesDownloadDate = "1970-01-01";

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

    private boolean isStatsVisible = true;

    private GeneralConfig() { }

    public Proxy getProxy() {
        final String DELIM = "\\|";
        final String proxyString = proxySettings.trim();
        if (proxyString.isEmpty() || proxyString.split(DELIM).length != 3) {
            return Proxy.NO_PROXY;
        } else {
            final Proxy.Type proxyType = Proxy.Type.valueOf(proxyString.split(DELIM)[0]);
            final int port = Integer.parseInt(proxyString.split(DELIM)[1]);
            final String urlAddress = proxyString.split(DELIM)[2];
            return new Proxy(proxyType, new InetSocketAddress(urlAddress, port));
        }
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

    public int getLeft() {
        return left;
    }

    public void setLeft(final int left) {
        this.left=left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(final int top) {
        this.top=top;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(final int width) {
        this.width=width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(final int height) {
        this.height=height;
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

    public boolean getMulliganScreenActive() {
        return isMulliganScreenActive;
    }
    public void setMulliganScreenActive(final boolean b) {
        isMulliganScreenActive = b;
    }

    public boolean showMissingCardData() {
        return showMissingCardData;
    }
    public void setShowMissingCardData(final boolean b) {
        showMissingCardData = b;
        CardDefinitions.resetMissingCardData();
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
     * Gets the last date missing images were downloaded.
     * <p>
     * If missing then date is set to "1970-01-01".
     */
    public Date getMissingImagesDownloadDate() {
        try {
            final SimpleDateFormat df = new SimpleDateFormat(CardProperty.IMAGE_UPDATED_FORMAT);
            return df.parse(missingImagesDownloadDate);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void setMissingImagesDownloadDate(final Date runDate) {
        final SimpleDateFormat df = new SimpleDateFormat(CardProperty.IMAGE_UPDATED_FORMAT);
        missingImagesDownloadDate = df.format(runDate);
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

    private void load(final Properties properties) {
        left=Integer.parseInt(properties.getProperty(LEFT,""+left));
        top=Integer.parseInt(properties.getProperty(TOP,""+top));
        width=Integer.parseInt(properties.getProperty(WIDTH,""+width));
        height=Integer.parseInt(properties.getProperty(HEIGHT,""+height));
        maximized=Boolean.parseBoolean(properties.getProperty(MAXIMIZED,""+maximized));
        theme=properties.getProperty(THEME,theme);
        avatar=properties.getProperty(AVATAR,avatar);
        highlight = properties.getProperty(HIGHLIGHT, highlight);
        skipSingle=Boolean.parseBoolean(properties.getProperty(SKIP_SINGLE,""+skipSingle));
        alwaysPass=Boolean.parseBoolean(properties.getProperty(ALWAYS_PASS,""+alwaysPass));
        smartTarget=Boolean.parseBoolean(properties.getProperty(SMART_TARGET,""+smartTarget));
        popupDelay=Integer.parseInt(properties.getProperty(POPUP_DELAY,""+popupDelay));
        messageDelay = Integer.parseInt(properties.getProperty(MESSAGE_DELAY,"" + messageDelay));
        touchscreen = Boolean.parseBoolean(properties.getProperty(TOUCHSCREEN,""+touchscreen));
        mouseWheelPopup = Boolean.parseBoolean(properties.getProperty(MOUSEWHEEL_POPUP, "" + mouseWheelPopup));
        fullScreen = Boolean.parseBoolean(properties.getProperty(FULLSCREEN, "" + fullScreen));
        previewCardOnSelect = Boolean.parseBoolean(properties.getProperty(PREVIEW_CARD_ON_SELECT, "" + previewCardOnSelect));
        showLogMessages = Boolean.parseBoolean(properties.getProperty(SHOW_LOG_MESSAGES, "" + showLogMessages));
        isMulliganScreenActive = Boolean.parseBoolean(properties.getProperty(MULLIGAN_SCREEN, "" + isMulliganScreenActive));
        mostRecentDeckFilename = properties.getProperty(RECENT_DECK, mostRecentDeckFilename).trim();
        isCustomBackground = Boolean.parseBoolean(properties.getProperty(CUSTOM_BACKGROUND, "" + isCustomBackground));
        showMissingCardData = Boolean.parseBoolean(properties.getProperty(SHOW_MISSING_CARD_DATA, "" + showMissingCardData));
        cardImagesPath = properties.getProperty(CARD_IMAGES_PATH, cardImagesPath);
        animateGameplay = Boolean.parseBoolean(properties.getProperty(ANIMATE_GAMEPLAY, "" + animateGameplay));
        deckFileMaxLines = Integer.parseInt(properties.getProperty(DECK_FILE_MAX_LINES, ""+ deckFileMaxLines));
        proxySettings = properties.getProperty(PROXY_SETTINGS, proxySettings);
        firemindAccessToken = properties.getProperty(FIREMIND_ACCESS_TOKEN, firemindAccessToken);
        newTurnAlertDuration = Integer.parseInt(properties.getProperty(NEWTURN_ALERT_DURATION,"" + newTurnAlertDuration));
        landPreviewDuration = Integer.parseInt(properties.getProperty(LAND_PREVIEW_DURATION,"" + landPreviewDuration));
        nonLandPreviewDuration = Integer.parseInt(properties.getProperty(NONLAND_PREVIEW_DURATION,"" + nonLandPreviewDuration));
        isSplitViewDeckEditor = Boolean.parseBoolean(properties.getProperty(SPLITVIEW_DECKEDITOR, "" + isSplitViewDeckEditor));
        overlayPermanentMinHeight = Integer.parseInt(properties.getProperty(OVERLAY_PERMANENT_MIN_HEIGHT, "" + overlayPermanentMinHeight));
        ignoredVersionAlert = properties.getProperty(IGNORED_VERSION_ALERT, ignoredVersionAlert);
        isGamePausedOnPopup = Boolean.parseBoolean(properties.getProperty(PAUSE_GAME_POPUP, "" + isGamePausedOnPopup));
        missingImagesDownloadDate = properties.getProperty(MISSING_DOWNLOAD_DATE, missingImagesDownloadDate);
        playableImagesDownloadDate = properties.getProperty(PLAYABLE_DOWNLOAD_DATE, playableImagesDownloadDate);
        duelSidebarLayout = properties.getProperty(DUEL_SIDEBAR_LAYOUT, duelSidebarLayout);
        hideAiActionPrompt = Boolean.parseBoolean(properties.getProperty(HIDE_AI_ACTION_PROMPT, "" + hideAiActionPrompt));
        rolloverColor = new Color(Integer.parseInt(properties.getProperty(ROLLOVER_COLOR, "" + rolloverColor.getRGB())));
        uiVolume = Integer.parseInt(properties.getProperty(UI_VOLUME, "" + uiVolume));
        translation = properties.getProperty(TRANSLATION, translation);
        logMessageStyle = MessageStyle.valueOf(properties.getProperty(LOG_MESSAGE_STYLE, logMessageStyle.name()));
        AnimationFx.setFlags(Integer.parseInt(properties.getProperty(ANIMATION_FLAGS, "" + AnimationFx.getFlags())));
        preferredImageSize = ImageSizePresets.valueOf(properties.getProperty(PREF_IMAGE_SIZE, preferredImageSize.name()));
        cardTextLanguage = CardTextLanguage.valueOf(properties.getProperty(CARD_TEXT_LANG, cardTextLanguage.name()));
        gameVolume = Integer.parseInt(properties.getProperty(GAME_VOLUME, "" + gameVolume));
    }

    public void load() {
        load(FileIO.toProp(getConfigFile()));
    }

    private void save(final Properties properties) {
        properties.setProperty(LEFT,String.valueOf(left));
        properties.setProperty(TOP,String.valueOf(top));
        properties.setProperty(WIDTH,String.valueOf(width));
        properties.setProperty(HEIGHT,String.valueOf(height));
        properties.setProperty(MAXIMIZED,String.valueOf(maximized));
        properties.setProperty(THEME,theme);
        properties.setProperty(AVATAR,avatar);
        properties.setProperty(HIGHLIGHT,highlight);
        properties.setProperty(SKIP_SINGLE,String.valueOf(skipSingle));
        properties.setProperty(ALWAYS_PASS,String.valueOf(alwaysPass));
        properties.setProperty(SMART_TARGET,String.valueOf(smartTarget));
        properties.setProperty(POPUP_DELAY,String.valueOf(popupDelay));
        properties.setProperty(MESSAGE_DELAY,String.valueOf(messageDelay));
        properties.setProperty(TOUCHSCREEN,String.valueOf(touchscreen));
        properties.setProperty(MOUSEWHEEL_POPUP, String.valueOf(mouseWheelPopup));
        properties.setProperty(FULLSCREEN, String.valueOf(fullScreen));
        properties.setProperty(PREVIEW_CARD_ON_SELECT, String.valueOf(previewCardOnSelect));
        properties.setProperty(SHOW_LOG_MESSAGES, String.valueOf(showLogMessages));
        properties.setProperty(MULLIGAN_SCREEN, String.valueOf(isMulliganScreenActive));
        properties.setProperty(RECENT_DECK, mostRecentDeckFilename);
        properties.setProperty(CUSTOM_BACKGROUND, String.valueOf(isCustomBackground));
        properties.setProperty(SHOW_MISSING_CARD_DATA, String.valueOf(showMissingCardData));
        properties.setProperty(CARD_IMAGES_PATH, cardImagesPath);
        properties.setProperty(ANIMATE_GAMEPLAY, String.valueOf(animateGameplay));
        properties.setProperty(PROXY_SETTINGS, proxySettings);
        properties.setProperty(FIREMIND_ACCESS_TOKEN, firemindAccessToken);
        properties.setProperty(NEWTURN_ALERT_DURATION, String.valueOf(newTurnAlertDuration));
        properties.setProperty(LAND_PREVIEW_DURATION, String.valueOf(landPreviewDuration));
        properties.setProperty(NONLAND_PREVIEW_DURATION, String.valueOf(nonLandPreviewDuration));
        properties.setProperty(SPLITVIEW_DECKEDITOR, String.valueOf(isSplitViewDeckEditor));
        properties.setProperty(IGNORED_VERSION_ALERT, ignoredVersionAlert);
        properties.setProperty(PAUSE_GAME_POPUP, String.valueOf(isGamePausedOnPopup));
        properties.setProperty(MISSING_DOWNLOAD_DATE, missingImagesDownloadDate);
        properties.setProperty(PLAYABLE_DOWNLOAD_DATE, playableImagesDownloadDate);
        properties.setProperty(DUEL_SIDEBAR_LAYOUT, duelSidebarLayout);
        properties.setProperty(HIDE_AI_ACTION_PROMPT, String.valueOf(hideAiActionPrompt));
        properties.setProperty(ROLLOVER_COLOR, String.valueOf(rolloverColor.getRGB()));
        properties.setProperty(UI_VOLUME, String.valueOf(uiVolume));
        properties.setProperty(TRANSLATION, translation);
        properties.setProperty(LOG_MESSAGE_STYLE, logMessageStyle.name());
        properties.setProperty(ANIMATION_FLAGS, String.valueOf(AnimationFx.getFlags()));
        properties.setProperty(PREF_IMAGE_SIZE, preferredImageSize.name());
        properties.setProperty(CARD_TEXT_LANG, cardTextLanguage.name());
        properties.setProperty(GAME_VOLUME, String.valueOf(gameVolume));
    }

    public void save() {
        final Properties properties=new SortedProperties();
        save(properties);
        try { //save config
            FileIO.toFile(getConfigFile(), properties, "General configuration");
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
}
