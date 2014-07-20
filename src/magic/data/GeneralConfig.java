package magic.data;

import magic.MagicMain;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class GeneralConfig {

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
    private static final String TEXT_VIEW="text";
    private static final String SKIP_SINGLE="single";
    private static final String ALWAYS_PASS="pass";
    private static final String SMART_TARGET="target";
    private static final String POPUP_DELAY="popup";
    private static final String MESSAGE_DELAY = "message";
    private static final String STRENGTH_DIFFICULTY="strengthDifficulty";
    private static final String STRENGTH_GAMES="strengthGames";
    private static final String HIGH_QUALITY="hq";
    private static final String SOUND="sound";
    private static final String CONFIRM_EXIT = "confirmExit";
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

    // The most common size of card retrieved from http://mtgimage.com.
    public static final Dimension PREFERRED_CARD_SIZE = new Dimension(480, 680);

    private static final int DEFAULT_LEFT=-1;
    private static final int DEFAULT_TOP=0;
    public static final int DEFAULT_WIDTH=1024;
    public static final int DEFAULT_HEIGHT=600;
    private static final boolean DEFAULT_MAXIMIZED=false;
    private static final String DEFAULT_THEME="felt";
    private static final String DEFAULT_AVATAR="legend";
    private static final String DEFAULT_HIGHLIGHT = "theme";
    private static final boolean DEFAULT_TEXT_VIEW=false;
    private static final boolean DEFAULT_SINGLE=true;
    private static final boolean DEFAULT_PASS=true;
    private static final boolean DEFAULT_TARGET=true;
    private static final int DEFAULT_POPUP_DELAY=300;
    private static final int DEFAULT_MESSAGE_DELAY = 2000;
    private static final int DEFAULT_STRENGTH_DIFFICULTY=2;
    private static final int DEFAULT_STRENGTH_GAMES=100;
    private static final boolean DEFAULT_HIGH_QUALITY=false;
    private static final boolean DEFAULT_SOUND=false;
    private static final boolean DEFAULT_CONFIRM_EXIT = true;
    private static final boolean DEFAULT_TOUCHSCREEN = false;
    private static final boolean DEFAULT_MOUSEWHEEL_POPUP = false;
    private static final boolean DEFAULT_LOG_SCROLLBAR = true;
    private static final boolean DEFAULT_LOG_TOPINSERT = false;
    private static final boolean DEFAULT_FULLSCREEN = false;
    private static final boolean DEFAULT_PREVIEW_CARD_ON_SELECT = false;
    private static final boolean DEFAULT_SHOW_LOG_MESSAGES = true;
    private static final boolean DEFAULT_MULLIGAN_SCREEN = true;
    private static final boolean DEFAULT_CUSTOM_BACKGROUND = false;

    private int left=DEFAULT_LEFT;
    private int top=DEFAULT_TOP;
    private int width=DEFAULT_WIDTH;
    private int height=DEFAULT_HEIGHT;
    private boolean maximized=DEFAULT_MAXIMIZED;
    private String theme=DEFAULT_THEME;
    private String avatar=DEFAULT_AVATAR;
    private String highlight = DEFAULT_HIGHLIGHT;
    private boolean textView=DEFAULT_TEXT_VIEW;
    private boolean skipSingle=DEFAULT_SINGLE;
    private boolean alwaysPass=DEFAULT_PASS;
    private boolean smartTarget=DEFAULT_TARGET;
    private int popupDelay=DEFAULT_POPUP_DELAY;
    private int messageDelay = DEFAULT_MESSAGE_DELAY;
    private int strengthDifficulty=DEFAULT_STRENGTH_DIFFICULTY;
    private int strengthGames=DEFAULT_STRENGTH_GAMES;
    private boolean highQuality=DEFAULT_HIGH_QUALITY;
    private boolean sound=DEFAULT_SOUND;
    private boolean confirmExit = DEFAULT_CONFIRM_EXIT;
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
    private boolean animateGameplay = false;

    private GeneralConfig() { }

    public boolean isAnimateGameplay() {
        return animateGameplay;
    }
    public void setAnimateGameplay(boolean b) {
        animateGameplay = b;
    }

    public Path getCardImagesPath() {
        if (cardImagesPath.isEmpty()) {
            return Paths.get(MagicMain.getGamePath());
        } else {
            return Paths.get(cardImagesPath);
        }
    }
    public void setCardImagesPath(final Path path) {
        if (path.equals(Paths.get(MagicMain.getGamePath()))) {
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

    public void setAvatar(final String avatar) {
        this.avatar=avatar;
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

    public boolean getTextView() {
        return textView;
    }

    public void setTextView(final boolean textView) {
        this.textView=textView;
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

    public boolean isConfirmExit() {
        return confirmExit;
    }

    public void setConfirmExit(final boolean confirmExit) {
        this.confirmExit = confirmExit;
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


    private void load(final Properties properties) {
        left=Integer.parseInt(properties.getProperty(LEFT,""+DEFAULT_LEFT));
        top=Integer.parseInt(properties.getProperty(TOP,""+DEFAULT_TOP));
        width=Integer.parseInt(properties.getProperty(WIDTH,""+DEFAULT_WIDTH));
        height=Integer.parseInt(properties.getProperty(HEIGHT,""+DEFAULT_HEIGHT));
        maximized=Boolean.parseBoolean(properties.getProperty(MAXIMIZED,""+DEFAULT_MAXIMIZED));
        theme=properties.getProperty(THEME,DEFAULT_THEME);
        avatar=properties.getProperty(AVATAR,DEFAULT_AVATAR);
        highlight = properties.getProperty(HIGHLIGHT,DEFAULT_HIGHLIGHT);
        textView=Boolean.parseBoolean(properties.getProperty(TEXT_VIEW,""+DEFAULT_TEXT_VIEW));
        skipSingle=Boolean.parseBoolean(properties.getProperty(SKIP_SINGLE,""+DEFAULT_SINGLE));
        alwaysPass=Boolean.parseBoolean(properties.getProperty(ALWAYS_PASS,""+DEFAULT_PASS));
        smartTarget=Boolean.parseBoolean(properties.getProperty(SMART_TARGET,""+DEFAULT_TARGET));
        popupDelay=Integer.parseInt(properties.getProperty(POPUP_DELAY,""+DEFAULT_POPUP_DELAY));
        messageDelay = Integer.parseInt(properties.getProperty(MESSAGE_DELAY,"" + DEFAULT_MESSAGE_DELAY));
        strengthDifficulty=Integer.parseInt(properties.getProperty(STRENGTH_DIFFICULTY,""+DEFAULT_STRENGTH_DIFFICULTY));
        strengthGames=Integer.parseInt(properties.getProperty(STRENGTH_GAMES,""+DEFAULT_STRENGTH_GAMES));
        highQuality=Boolean.parseBoolean(properties.getProperty(HIGH_QUALITY,""+DEFAULT_HIGH_QUALITY));
        sound=Boolean.parseBoolean(properties.getProperty(SOUND,""+DEFAULT_SOUND));
        confirmExit = Boolean.parseBoolean(properties.getProperty(CONFIRM_EXIT,""+DEFAULT_CONFIRM_EXIT));
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
        animateGameplay = Boolean.parseBoolean(properties.getProperty(ANIMATE_GAMEPLAY, "" + false));
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
        properties.setProperty(TEXT_VIEW,String.valueOf(textView));
        properties.setProperty(SKIP_SINGLE,String.valueOf(skipSingle));
        properties.setProperty(ALWAYS_PASS,String.valueOf(alwaysPass));
        properties.setProperty(SMART_TARGET,String.valueOf(smartTarget));
        properties.setProperty(POPUP_DELAY,String.valueOf(popupDelay));
        properties.setProperty(MESSAGE_DELAY,String.valueOf(messageDelay));
        properties.setProperty(STRENGTH_DIFFICULTY,String.valueOf(strengthDifficulty));
        properties.setProperty(STRENGTH_GAMES,String.valueOf(strengthGames));
        properties.setProperty(HIGH_QUALITY,String.valueOf(highQuality));
        properties.setProperty(SOUND,String.valueOf(sound));
        properties.setProperty(CONFIRM_EXIT,String.valueOf(confirmExit));
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
    }

    public void save() {
        final Properties properties=new Properties();
        save(properties);
        try { //save config
            FileIO.toFile(getConfigFile(), properties, "General configuration");
            System.err.println("Saved general config");
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to save general config");
        }
    }

    private static File getConfigFile() {
        return new File(MagicMain.getGamePath(),CONFIG_FILENAME);
    }

    public static GeneralConfig getInstance() {
        return INSTANCE;
    }


}
