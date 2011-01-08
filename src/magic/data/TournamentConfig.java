package magic.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import magic.MagicMain;
import magic.model.MagicColor;
import magic.model.MagicPlayerProfile;

public class TournamentConfig {

	private static TournamentConfig INSTANCE=new TournamentConfig();

	private static final String ANY_THREE="***";
	private static final String ANY_TWO="**";
	
	private static final String CONFIG_FILENAME="tournament.cfg";
	private static final String AVATAR="avatar";
	private static final String NAME="name";
	private static final String START_LIFE="life";
	private static final String HAND_SIZE="hand";
	private static final String GAMES="games";
	private static final String PLAYER="player";
	private static final String OPPONENT="opponent";
	private static final String CUBE="cube";

	private int avatar=0;
	private String name="Player";
	private int startLife=20;
	private int handSize=7;
	private int games=7;
	private String playerColors=ANY_THREE;
	private String opponentColors=ANY_THREE;
	private String cube=CubeDefinitions.DEFAULT_NAME;
	
	public TournamentConfig() {
		
	}
	
	public TournamentConfig(final TournamentConfig tournamentConfig) {
		
		avatar=tournamentConfig.avatar;
		startLife=tournamentConfig.startLife;
		handSize=tournamentConfig.handSize;
		games=tournamentConfig.games;
		playerColors=tournamentConfig.playerColors;
		opponentColors=tournamentConfig.opponentColors;
	}
	
	public int getAvatar() {
		
		return avatar;
	}

	public void setAvatar(final int avatar) {

		this.avatar = avatar;
	}
	
	public String getName() {
		
		return name;
	}
	
	public void setName(final String name) {
		
		this.name=name;
	}

	public int getStartLife() {

		return startLife;
	}

	public void setStartLife(final int startLife) {
		
		this.startLife = startLife;
	}

	public int getHandSize() {
		
		return handSize;
	}

	public void setHandSize(final int handSize) {
		
		this.handSize = handSize;
	}
	
	public void setNrOfGames(final int games) {
		
		this.games = games; 
	}
	
	public int getNrOfGames() {
		
		return games;
	}
	
	private static MagicPlayerProfile getProfile(final String colorText) {
		
		if (ANY_THREE.equals(colorText)) {
			return new MagicPlayerProfile(MagicColor.getRandomColors(3));
		} else if (ANY_TWO.equals(colorText)) {
			return new MagicPlayerProfile(MagicColor.getRandomColors(2));
		}
		return new MagicPlayerProfile(colorText);
	}
	
	public String getPlayerColors() {
		
		return playerColors;
	}
	
	public void setPlayerColors(final String colors) {

		playerColors=colors;
	}
	
	public MagicPlayerProfile getPlayerProfile() {
		
		return getProfile(playerColors);
	}
	
	public String getOpponentColors() {
		
		return opponentColors;
	}
	
	public void setOpponentColors(final String colors) {
		
		opponentColors=colors;
	}
	
	public MagicPlayerProfile getOpponentProfile() {
		
		return getProfile(opponentColors);
	}
	
	public String getCube() {
		
		return cube;
	}
	
	public void setCube(final String cube) {
		
		this.cube=cube;
	}

	public void load(final Properties properties) {
		
		avatar=Integer.parseInt(properties.getProperty(AVATAR,""+avatar));
		name=properties.getProperty(NAME,name);
		startLife=Integer.parseInt(properties.getProperty(START_LIFE,""+startLife));
		handSize=Integer.parseInt(properties.getProperty(HAND_SIZE,""+handSize));
		games=Integer.parseInt(properties.getProperty(GAMES,""+games));
		playerColors=properties.getProperty(PLAYER,playerColors);
		opponentColors=properties.getProperty(OPPONENT,opponentColors);
		cube=properties.getProperty(CUBE,cube);
	}
	
	public void load() {

		try {
			final Properties properties=new Properties();
			properties.load(new FileInputStream(getConfigFile()));
			load(properties);
		} catch (final IOException ex) {}
	}
	
	public void save(final Properties properties) {

		properties.setProperty(AVATAR,""+avatar);
		properties.setProperty(NAME,name);
		properties.setProperty(START_LIFE,""+startLife);
		properties.setProperty(HAND_SIZE,""+handSize);
		properties.setProperty(GAMES, ""+games);
		properties.setProperty(PLAYER,playerColors);
		properties.setProperty(OPPONENT,opponentColors);
		properties.setProperty(CUBE,cube);
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
	
	public static TournamentConfig getInstance() {
		
		return INSTANCE;
	}
}