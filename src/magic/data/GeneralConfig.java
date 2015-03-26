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
import magic.utility.MagicFileSystem;
import magic.utility.MagicSystem;

public class GeneralConfig {

    public static final String VERSION = "1.60";
    public static final String SOFTWARE_TITLE =
            "Magarena " + GeneralConfig.VERSION + (MagicSystem.isDevMode() ? " [DEV MODE]" : "");

    private static final GeneralConfig INSTANCE=new GeneralConfig();

    public static final String CONFIG_FILENAME="general.cfg";
    private static final String LEFT="left";
    private static final String TOP="top";
    private static final String WIDTH="width";
    private static final String HEIGHT="height";
    private static final String MAXIMIZED="maximized";
    private static final String THEME="theme";
    private static final String AVATAR="avatar";
    private static final String HIGHLIGHT = "highlight";
    private static final String SKIP_SINGLE="single";
    private static final String ALWAYS_PASS="pass";
    private static final String SMART_TARGET="target";
    private static final String POPUP_DELAY="popup";
    private static final String MESSAGE_DELAY = "message";
    private static final String STRENGTH_DIFFICULTY="strengthDifficulty";
    private static final String STRENGTH_GAMES="strengthGames";
    private static final String HIGH_QUALITY="hq";
    private static final String SOUND="sound";
    private static final String TOUCHSCREEN = "touchscreen";
    private static final String MOUSEWHEEL_POPUP = "mousewheel";
    private static final String LOG_SCROLLBAR = "logScrollbar";
    private static final String LOG_TOPINSERT = "logTopInsert";
    private static final String FULLSCREEN = "fullScreen";
    private static final String PREVIEW_CARD_ON_SELECT = "previewCardOnSelect";
    private static final String SHOW_LOG_MESSAGES = "showLogMessages";
    private static final String MULLIGAN_SCREEN = "mulliganScreen";
    private static final String RECENT_DECK = "MostRecentDeckFilename";
    private static final String CUSTOM_BACKGROUND = "customBackground";
    private static final String SHOW_MISSING_CARD_DATA = "showMissingCardData";
    private static final String CARD_IMAGES_PATH = "cardImagesPath";
    private static final String ANIMATE_GAMEPLAY = "animateGameplay";
    private static final String DECK_FILE_MAX_LINES = "deckFileMaxLines";
    private static final String PROXY_SETTINGS = "proxySettings";
    private static final String FIREMIND_ACCESS_TOKEN = "firemindAccessToken";
    private static final String NEWTURN_ALERT_DURATION = "newTurnAlertDuration";
    private static final String LAND_PREVIEW_DURATION = "landPreviewDuration";
    private static final String NONLAND_PREVIEW_DURATION = "nonLandPreviewDuration";
    private static final String SPLITVIEW_DECKEDITOR = "splitViewDeckEditor";
    private static final String CARD_POPUP_SCALE = "cardPopupScale";
    private static final String SCALE_CARD_POPUP_TO_SCREEN = "scaleCardPopupToScreen";
    private static final String OVERLAY_PERMANENT_MIN_HEIGHT = "overlayPermanentMinHeight";
    private static final String IGNORED_VERSION_ALERT = "ignoredVersionAlert";
    private static final String UI_SOUND = "uiSound";
    private static final String PAUSE_GAME_POPUP = "pauseGamePopup";
    private static final String DOWNLOADER_RUN_DATE = "imageDownloaderRunDate";
    private static final String DUEL_SIDEBAR_LAYOUT ="duelSidebarLayout";

    private static final int DEFAULT_LEFT=-1;
    private static final int DEFAULT_TOP=0;
    public static final int DEFAULT_WIDTH=1024;
    public static final int DEFAULT_HEIGHT=600;
    private static final boolean DEFAULT_MAXIMIZED=false;
    private static final String DEFAULT_THEME="felt";
    private static final String DEFAULT_AVATAR="legend";
    private static final String DEFAULT_HIGHLIGHT = "theme";
    private static final boolean DEFAULT_TEXT_VIEW = false;
    private static final boolean DEFAULT_SINGLE=true;
    private static final boolean DEFAULT_PASS=true;
    private static final boolean DEFAULT_TARGET=true;
    private static final int DEFAULT_POPUP_DELAY=300;
    private static final int DEFAULT_MESSAGE_DELAY = 2000;
    private static final int DEFAULT_STRENGTH_DIFFICULTY=2;
    private static final int DEFAULT_STRENGTH_GAMES=100;
    private static final boolean DEFAULT_HIGH_QUALITY=false;
    private static final boolean DEFAULT_SOUND=true;
    private static final boolean DEFAULT_TOUCHSCREEN = false;
    private static final boolean DEFAULT_MOUSEWHEEL_POPUP = false;
    private static final boolean DEFAULT_LOG_SCROLLBAR = true;
    private static final boolean DEFAULT_LOG_TOPINSERT = false;
    private static final boolean DEFAULT_FULLSCREEN = false;
    private static final boolean DEFAULT_PREVIEW_CARD_ON_SELECT = true;
    private static final boolean DEFAULT_SHOW_LOG_MESSAGES = true;
    private static final boolean DEFAULT_MULLIGAN_SCREEN = true;
    private static final boolean DEFAULT_CUSTOM_BACKGROUND = false;
    private static final int DEFAULT_DECK_FILE_MAX_LINES = 500;
    private static final String DEFAULT_PROXY_SETTINGS = "";
    private static final int DEFAULT_NEWTURN_ALERT_DURATION = 3000; // msecs
    private static final int DEFAULT_LAND_PREVIEW_DURATION = 5000; // msecs
    private static final int DEFAULT_NONLAND_PREVIEW_DURATION = 10000; // msecs
    private static final double DEFAULT_CARD_POPUP_SCALE = 1.0d;
    private static final int DEFAULT_OVERLAY_PERMANENT_MIN_HEIGHT = 30; // pixels
    private static final boolean DEFAULT_PAUSE_GAME_POPUP = false;
    private static final String DEFAULT_DOWNLOADER_RUN_DATE = "1970-01-01";
    private static final String DEFAULT_DUEL_SIDEBAR_LAYOUT = "LOGSTACK,PLAYER2,TURNINFO,PLAYER1";

    private int left=DEFAULT_LEFT;
    private int top=DEFAULT_TOP;
    private int width=DEFAULT_WIDTH;
    private int height=DEFAULT_HEIGHT;
    private boolean maximized=DEFAULT_MAXIMIZED;
    private String theme=DEFAULT_THEME;
    private String avatar=DEFAULT_AVATAR;
    private String highlight = DEFAULT_HIGHLIGHT;
    private boolean textView = DEFAULT_TEXT_VIEW;
    private boolean skipSingle=DEFAULT_SINGLE;
    private boolean alwaysPass=DEFAULT_PASS;
    private boolean smartTarget=DEFAULT_TARGET;
    private int popupDelay=DEFAULT_POPUP_DELAY;
    private int messageDelay = DEFAULT_MESSAGE_DELAY;
    private int strengthDifficulty=DEFAULT_STRENGTH_DIFFICULTY;
    private int strengthGames=DEFAULT_STRENGTH_GAMES;
    private boolean highQuality=DEFAULT_HIGH_QUALITY;
    private boolean sound=DEFAULT_SOUND;
    private boolean touchscreen = DEFAULT_TOUCHSCREEN;
    private boolean mouseWheelPopup = DEFAULT_MOUSEWHEEL_POPUP;
    private boolean isLogScrollbarVisible = DEFAULT_LOG_SCROLLBAR;
    private boolean isLogMessageAddedToTop = DEFAULT_LOG_TOPINSERT;
    private boolean fullScreen = DEFAULT_FULLSCREEN;
    private boolean previewCardOnSelect = DEFAULT_PREVIEW_CARD_ON_SELECT;
    private boolean showLogMessages = DEFAULT_SHOW_LOG_MESSAGES;
    private boolean isMulliganScreenActive = DEFAULT_MULLIGAN_SCREEN;
    private boolean isLogViewerDisabled = false;
    private String mostRecentDeckFilename = "";
    private boolean isMissingFiles = false;
    private boolean isCustomBackground = DEFAULT_CUSTOM_BACKGROUND;
    private boolean showMissingCardData = true;
    private String cardImagesPath = "";
    private boolean animateGameplay = true;
    private int deckFileMaxLines = DEFAULT_DECK_FILE_MAX_LINES;
    private String proxySettings = DEFAULT_PROXY_SETTINGS;
    private String firemindAccessToken;
    private int newTurnAlertDuration = DEFAULT_NEWTURN_ALERT_DURATION;
    private int landPreviewDuration = DEFAULT_LAND_PREVIEW_DURATION;
    private int nonLandPreviewDuration = DEFAULT_NONLAND_PREVIEW_DURATION;
    private boolean isSplitViewDeckEditor = false;
    private double cardPopupScale = DEFAULT_CARD_POPUP_SCALE;
    private boolean isCardPopupScaledToScreen = true;
    private int overlayPermanentMinHeight = DEFAULT_OVERLAY_PERMANENT_MIN_HEIGHT;
    private String ignoredVersionAlert = "";
    private boolean isUiSound = true;
    private boolean isGamePausedOnPopup = DEFAULT_PAUSE_GAME_POPUP;
    private String imageDownloaderRunDate = DEFAULT_DOWNLOADER_RUN_DATE;
    private String duelSidebarLayout = DEFAULT_DUEL_SIDEBAR_LAYOUT;

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

    public double getCardPopupScale() {
        return cardPopupScale;
    }
    public void setCardPopupScale(final double popupScale) {
        this.cardPopupScale = popupScale;
    }

    public boolean isCardPopupScaledToScreen() {
        return isCardPopupScaledToScreen;
    }
    public void setIsCardPopupScaledToScreen(final boolean b) {
        this.isCardPopupScaledToScreen = b;
    }

    public int getDeckFileMaxLines() {
        return deckFileMaxLines;
    }

    public boolean isAnimateGameplay() {
        return animateGameplay && !getTextView();
    }

    public boolean getAnimateGameplay() {
        return animateGameplay;
    }
    public void setAnimateGameplay(boolean b) {
        animateGameplay = b;
    }

    public Path getCardImagesPath() {
        if (cardImagesPath.isEmpty()) {
            return MagicFileSystem.getDataPath();
        } else {
            return Paths.get(cardImagesPath);
        }
    }
    public void setCardImagesPath(final Path path) {
        if (path.equals(MagicFileSystem.getDataPath())) {
            this.cardImagesPath = "";
        } else {
            this.cardImagesPath = path.toAbsolutePath().toString();
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

    public boolean isLogViewerDisabled() {
        return isLogViewerDisabled;
    }
    public void setLogViewerDisabled(boolean isLogViewerDisabled) {
        this.isLogViewerDisabled = isLogViewerDisabled;
    }

    public boolean isLogMessageAddedToTop() {
        return this.isLogMessageAddedToTop;
    }
    public void setLogMessageAddedToTop(final boolean b) {
        this.isLogMessageAddedToTop = b;
    }

    public boolean isLogScrollbarVisible() {
        return this.isLogScrollbarVisible;
    }
    public void setLogScrollbarVisible(final boolean b) {
        this.isLogScrollbarVisible = b;
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

    public boolean getTextView() {
        return textView;
    }

    public void setTextView(final boolean textView) {
        this.textView = textView;
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

    public int getStrengthDifficulty() {
        return strengthDifficulty;
    }

    public void setStrengthDifficulty(final int strengthDifficulty) {
        this.strengthDifficulty=strengthDifficulty;
    }

    public int getStrengthGames() {
        return strengthGames;
    }

    public void setStrengthGames(final int strengthGames) {
        this.strengthGames=strengthGames;
    }

    public boolean isHighQuality() {
        return highQuality;
    }

    public void setHighQuality(final boolean highQuality) {
        this.highQuality=highQuality;
    }

    public boolean isSound() {
        return sound;
    }

    public void setSound(final boolean sound) {
        this.sound=sound;
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
        return isMulliganScreenActive && !getTextView();
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

    public boolean isUiSound() {
        return isUiSound;
    }
    public void setIsUiSound(final boolean b) {
        isUiSound = b;
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

    public Date getImageDownloaderRunDate() {
        try {
            final SimpleDateFormat df = new SimpleDateFormat(CardProperty.IMAGE_UPDATED_FORMAT);
            return df.parse(imageDownloaderRunDate);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void setImageDownloaderRunDate(final Date runDate) {
        final SimpleDateFormat df = new SimpleDateFormat(CardProperty.IMAGE_UPDATED_FORMAT);
        imageDownloaderRunDate = df.format(runDate);
    }


    private void load(final Properties properties) {
        left=Integer.parseInt(properties.getProperty(LEFT,""+DEFAULT_LEFT));
        top=Integer.parseInt(properties.getProperty(TOP,""+DEFAULT_TOP));
        width=Integer.parseInt(properties.getProperty(WIDTH,""+DEFAULT_WIDTH));
        height=Integer.parseInt(properties.getProperty(HEIGHT,""+DEFAULT_HEIGHT));
        maximized=Boolean.parseBoolean(properties.getProperty(MAXIMIZED,""+DEFAULT_MAXIMIZED));
        theme=properties.getProperty(THEME,DEFAULT_THEME);
        avatar=properties.getProperty(AVATAR,DEFAULT_AVATAR);
        highlight = properties.getProperty(HIGHLIGHT,DEFAULT_HIGHLIGHT);
        skipSingle=Boolean.parseBoolean(properties.getProperty(SKIP_SINGLE,""+DEFAULT_SINGLE));
        alwaysPass=Boolean.parseBoolean(properties.getProperty(ALWAYS_PASS,""+DEFAULT_PASS));
        smartTarget=Boolean.parseBoolean(properties.getProperty(SMART_TARGET,""+DEFAULT_TARGET));
        popupDelay=Integer.parseInt(properties.getProperty(POPUP_DELAY,""+DEFAULT_POPUP_DELAY));
        messageDelay = Integer.parseInt(properties.getProperty(MESSAGE_DELAY,"" + DEFAULT_MESSAGE_DELAY));
        strengthDifficulty=Integer.parseInt(properties.getProperty(STRENGTH_DIFFICULTY,""+DEFAULT_STRENGTH_DIFFICULTY));
        strengthGames=Integer.parseInt(properties.getProperty(STRENGTH_GAMES,""+DEFAULT_STRENGTH_GAMES));
        highQuality=Boolean.parseBoolean(properties.getProperty(HIGH_QUALITY,""+DEFAULT_HIGH_QUALITY));
        sound=Boolean.parseBoolean(properties.getProperty(SOUND,""+DEFAULT_SOUND));
        touchscreen = Boolean.parseBoolean(properties.getProperty(TOUCHSCREEN,""+DEFAULT_TOUCHSCREEN));
        mouseWheelPopup = Boolean.parseBoolean(properties.getProperty(MOUSEWHEEL_POPUP, "" + DEFAULT_MOUSEWHEEL_POPUP));
        isLogScrollbarVisible = Boolean.parseBoolean(properties.getProperty(LOG_SCROLLBAR, "" + DEFAULT_LOG_SCROLLBAR));
        isLogMessageAddedToTop = Boolean.parseBoolean(properties.getProperty(LOG_TOPINSERT, "" + DEFAULT_LOG_TOPINSERT));
        fullScreen = Boolean.parseBoolean(properties.getProperty(FULLSCREEN, "" + DEFAULT_FULLSCREEN));
        previewCardOnSelect = Boolean.parseBoolean(properties.getProperty(PREVIEW_CARD_ON_SELECT, "" + DEFAULT_PREVIEW_CARD_ON_SELECT));
        showLogMessages = Boolean.parseBoolean(properties.getProperty(SHOW_LOG_MESSAGES, "" + DEFAULT_SHOW_LOG_MESSAGES));
        isMulliganScreenActive = Boolean.parseBoolean(properties.getProperty(MULLIGAN_SCREEN, "" + DEFAULT_MULLIGAN_SCREEN));
        mostRecentDeckFilename = properties.getProperty(RECENT_DECK, "").trim();
        isCustomBackground = Boolean.parseBoolean(properties.getProperty(CUSTOM_BACKGROUND, "" + DEFAULT_CUSTOM_BACKGROUND));
        showMissingCardData = Boolean.parseBoolean(properties.getProperty(SHOW_MISSING_CARD_DATA, "" + true));
        cardImagesPath = properties.getProperty(CARD_IMAGES_PATH, "");
        animateGameplay = Boolean.parseBoolean(properties.getProperty(ANIMATE_GAMEPLAY, "" + true));
        deckFileMaxLines = Integer.parseInt(properties.getProperty(DECK_FILE_MAX_LINES, ""+DEFAULT_DECK_FILE_MAX_LINES));
        proxySettings = properties.getProperty(PROXY_SETTINGS, "");
        firemindAccessToken = properties.getProperty(FIREMIND_ACCESS_TOKEN, "");
        newTurnAlertDuration = Integer.parseInt(properties.getProperty(NEWTURN_ALERT_DURATION,"" + DEFAULT_NEWTURN_ALERT_DURATION));
        landPreviewDuration = Integer.parseInt(properties.getProperty(LAND_PREVIEW_DURATION,"" + DEFAULT_LAND_PREVIEW_DURATION));
        nonLandPreviewDuration = Integer.parseInt(properties.getProperty(NONLAND_PREVIEW_DURATION,"" + DEFAULT_NONLAND_PREVIEW_DURATION));
        isSplitViewDeckEditor = Boolean.parseBoolean(properties.getProperty(SPLITVIEW_DECKEDITOR, "" + false));
        cardPopupScale = Double.parseDouble(properties.getProperty(CARD_POPUP_SCALE, "" + DEFAULT_CARD_POPUP_SCALE));
        isCardPopupScaledToScreen = Boolean.parseBoolean(properties.getProperty(SCALE_CARD_POPUP_TO_SCREEN, "" + true));
        overlayPermanentMinHeight = Integer.parseInt(properties.getProperty(OVERLAY_PERMANENT_MIN_HEIGHT, "" + DEFAULT_OVERLAY_PERMANENT_MIN_HEIGHT));
        ignoredVersionAlert = properties.getProperty(IGNORED_VERSION_ALERT, "");
        isUiSound = Boolean.parseBoolean(properties.getProperty(UI_SOUND, "" + true));
        isGamePausedOnPopup = Boolean.parseBoolean(properties.getProperty(PAUSE_GAME_POPUP, "" + DEFAULT_PAUSE_GAME_POPUP));
        imageDownloaderRunDate = properties.getProperty(DOWNLOADER_RUN_DATE, DEFAULT_DOWNLOADER_RUN_DATE);
        duelSidebarLayout = properties.getProperty(DUEL_SIDEBAR_LAYOUT, DEFAULT_DUEL_SIDEBAR_LAYOUT);
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
        properties.setProperty(STRENGTH_DIFFICULTY,String.valueOf(strengthDifficulty));
        properties.setProperty(STRENGTH_GAMES,String.valueOf(strengthGames));
        properties.setProperty(HIGH_QUALITY,String.valueOf(highQuality));
        properties.setProperty(SOUND,String.valueOf(sound));
        properties.setProperty(TOUCHSCREEN,String.valueOf(touchscreen));
        properties.setProperty(MOUSEWHEEL_POPUP, String.valueOf(mouseWheelPopup));
        properties.setProperty(LOG_SCROLLBAR, String.valueOf(isLogScrollbarVisible));
        properties.setProperty(LOG_TOPINSERT, String.valueOf(isLogMessageAddedToTop));
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
        properties.setProperty(CARD_POPUP_SCALE, String.valueOf(cardPopupScale));
        properties.setProperty(SCALE_CARD_POPUP_TO_SCREEN, String.valueOf(isCardPopupScaledToScreen));
        properties.setProperty(IGNORED_VERSION_ALERT, ignoredVersionAlert);
        properties.setProperty(UI_SOUND, String.valueOf(isUiSound));
        properties.setProperty(PAUSE_GAME_POPUP, String.valueOf(isGamePausedOnPopup));
        properties.setProperty(DOWNLOADER_RUN_DATE, imageDownloaderRunDate);
        properties.setProperty(DUEL_SIDEBAR_LAYOUT, duelSidebarLayout);
    }

    public void save() {
        final Properties properties=new Properties();
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

}
