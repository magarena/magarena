package magic.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import magic.MagicMain;
import magic.ai.MagicAI;
import magic.ai.MagicAIImpl;

public class GeneralConfig {

	private static GeneralConfig INSTANCE=new GeneralConfig();
	
	private static final String CONFIG_FILENAME="general.cfg";
	private static final String LEFT="left";
	private static final String TOP="top";
	private static final String WIDTH="width";
	private static final String HEIGHT="height";
	private static final String MAXIMIZED="maximized";
	private static final String THEME="theme";
	private static final String AVATAR="avatar";
	private static final String AI="ai";
	private static final String TEXT_VIEW="text";
	private static final String SKIP_SINGLE="single";
	private static final String ALWAYS_PASS="pass";
	private static final String SMART_TARGET="target";
	private static final String DIFFICULTY="difficulty";
	private static final String EXTRA_LIFE="extra";
	private static final String POPUP_DELAY="popup";
	private static final String STRENGTH_DIFFICULTY="strengthDifficulty";
	private static final String STRENGTH_GAMES="strengthGames";
	private static final String HIGH_QUALITY="hq";

	private static final int DEFAULT_LEFT=-1;
	private static final int DEFAULT_TOP=-1;
	private static final int DEFAULT_WIDTH=1000;
	private static final int DEFAULT_HEIGHT=700;
	private static final boolean DEFAULT_MAXIMIZED=false;
	private static final String DEFAULT_THEME="wood";
	private static final String DEFAULT_AVATAR="default";
	private static final String DEFAULT_AI="default";
	private static final boolean DEFAULT_TEXT_VIEW=false;
	private static final boolean DEFAULT_SINGLE=true;
	private static final boolean DEFAULT_PASS=true;
	private static final boolean DEFAULT_TARGET=true;
	private static final int DEFAULT_DIFFICULTY=6;
	private static final int DEFAULT_EXTRA_LIFE=0;
	private static final int DEFAULT_POPUP_DELAY=300;
	private static final int DEFAULT_STRENGTH_DIFFICULTY=2;
	private static final int DEFAULT_STRENGTH_GAMES=123;
	private static final boolean DEFAULT_HIGH_QUALITY=false;

	private int left=DEFAULT_LEFT;
	private int top=DEFAULT_TOP;
	private int width=DEFAULT_WIDTH;
	private int height=DEFAULT_HEIGHT;
	private boolean maximized=DEFAULT_MAXIMIZED;
	private String theme=DEFAULT_THEME;
	private String avatar=DEFAULT_AVATAR;
	private String ai=DEFAULT_AI;
	private boolean textView=DEFAULT_TEXT_VIEW;
	private boolean skipSingle=DEFAULT_SINGLE;
	private boolean alwaysPass=DEFAULT_PASS;
	private boolean smartTarget=DEFAULT_TARGET;
	private int difficulty=DEFAULT_DIFFICULTY;
	private int extraLife=DEFAULT_EXTRA_LIFE;
	private int popupDelay=DEFAULT_POPUP_DELAY;
	private int strengthDifficulty=DEFAULT_STRENGTH_DIFFICULTY;
	private int strengthGames=DEFAULT_STRENGTH_GAMES;
	private boolean highQuality=DEFAULT_HIGH_QUALITY;
	
	private GeneralConfig() {
		
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
	
	public String getAi() {
		
		return ai;
	}
	
	public void setAi(final String ai) {
		
		this.ai=ai;
	}
	
	public MagicAI[] getPlayerAis() {
		
		final MagicAI playerAi = MagicAIImpl.getAI(ai).getAI();
		return new MagicAI[]{playerAi, playerAi};
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

	public int getDifficulty() {
		
		return difficulty;
	}
		
	public void setDifficulty(final int difficulty) {
		
		this.difficulty=difficulty;
	}
	
	public int getExtraLife() {
		
		return extraLife;
	}
	
	public void setExtraLife(final int extraLife) {
		
		this.extraLife=extraLife;
	}
	
	public int getPopupDelay() {
		
		return popupDelay;
	}
	
	public void setPopupDelay(final int popupDelay) {
		
		this.popupDelay=popupDelay;
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
	
	public void load(final Properties properties) {
	
		left=Integer.parseInt(properties.getProperty(LEFT,""+DEFAULT_LEFT));
		top=Integer.parseInt(properties.getProperty(TOP,""+DEFAULT_TOP));
		width=Integer.parseInt(properties.getProperty(WIDTH,""+DEFAULT_WIDTH));
		height=Integer.parseInt(properties.getProperty(HEIGHT,""+DEFAULT_HEIGHT));
		maximized=Boolean.parseBoolean(properties.getProperty(MAXIMIZED,""+DEFAULT_MAXIMIZED));
		theme=properties.getProperty(THEME,DEFAULT_THEME);
		avatar=properties.getProperty(AVATAR,DEFAULT_AVATAR);
		ai=properties.getProperty(AI,DEFAULT_AI);
		textView=Boolean.parseBoolean(properties.getProperty(TEXT_VIEW,""+DEFAULT_TEXT_VIEW));
		skipSingle=Boolean.parseBoolean(properties.getProperty(SKIP_SINGLE,""+DEFAULT_SINGLE));
		alwaysPass=Boolean.parseBoolean(properties.getProperty(ALWAYS_PASS,""+DEFAULT_PASS));
		smartTarget=Boolean.parseBoolean(properties.getProperty(SMART_TARGET,""+DEFAULT_TARGET));
		difficulty=Integer.parseInt(properties.getProperty(DIFFICULTY,""+DEFAULT_DIFFICULTY));
		extraLife=Integer.parseInt(properties.getProperty(EXTRA_LIFE,""+DEFAULT_EXTRA_LIFE));
		popupDelay=Integer.parseInt(properties.getProperty(POPUP_DELAY,""+DEFAULT_POPUP_DELAY));
		strengthDifficulty=Integer.parseInt(properties.getProperty(STRENGTH_DIFFICULTY,""+DEFAULT_STRENGTH_DIFFICULTY));
		strengthGames=Integer.parseInt(properties.getProperty(STRENGTH_GAMES,""+DEFAULT_STRENGTH_GAMES));
		highQuality=Boolean.parseBoolean(properties.getProperty(HIGH_QUALITY,""+DEFAULT_HIGH_QUALITY));
	}
	
	public void load() {

		try {
			final Properties properties=new Properties();
			properties.load(new FileInputStream(getConfigFile()));
			load(properties);
		} catch (final IOException ex) {}
	}
	
	public void save(final Properties properties) {
		
		properties.setProperty(LEFT,String.valueOf(left));
		properties.setProperty(TOP,String.valueOf(top));
		properties.setProperty(WIDTH,String.valueOf(width));
		properties.setProperty(HEIGHT,String.valueOf(height));
		properties.setProperty(MAXIMIZED,String.valueOf(maximized));
		properties.setProperty(THEME,theme);
		properties.setProperty(AVATAR,avatar);
		properties.setProperty(AI,ai);
		properties.setProperty(TEXT_VIEW,String.valueOf(textView));
		properties.setProperty(SKIP_SINGLE,String.valueOf(skipSingle));
		properties.setProperty(ALWAYS_PASS,String.valueOf(alwaysPass));
		properties.setProperty(SMART_TARGET,String.valueOf(smartTarget));
		properties.setProperty(DIFFICULTY,String.valueOf(difficulty));
		properties.setProperty(EXTRA_LIFE,String.valueOf(extraLife));
		properties.setProperty(POPUP_DELAY,String.valueOf(popupDelay));
		properties.setProperty(STRENGTH_DIFFICULTY,String.valueOf(strengthDifficulty));
		properties.setProperty(STRENGTH_GAMES,String.valueOf(strengthGames));
		properties.setProperty(HIGH_QUALITY,String.valueOf(highQuality));
	}
	
	public void save() {
		
		try {
			final Properties properties=new Properties();
			save(properties);
			properties.store(new FileOutputStream(getConfigFile()),"Tournament configuration");
		} catch (final IOException ex) {}		
	}

	private static File getConfigFile() {
		
		return new File(MagicMain.getGamePath(),CONFIG_FILENAME);
	}

	public static GeneralConfig getInstance() {
		
		return INSTANCE;
	}
}