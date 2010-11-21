package magic.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import magic.MagicMain;
import magic.model.MagicPlayerProfile;

public class TournamentConfig {

	private static TournamentConfig INSTANCE=new TournamentConfig();
	
	private static final String CONFIG_FILENAME="tournament.cfg";
	private static final String AVATAR="avatar";
	private static final String NAME="name";
	private static final String START_LIFE="life";
	private static final String HAND_SIZE="hand";
	private static final String GAMES="games";
	private static final String PLAYER="player";
	private static final String OPPONENT="opponent";

	private int avatar=0;
	private String name="Player";
	private int startLife=20;
	private int handSize=7;
	private int games=7;
	private MagicPlayerProfile playerProfile=new MagicPlayerProfile("bgr");
	private MagicPlayerProfile opponentProfile=new MagicPlayerProfile("urw");
	
	public TournamentConfig() {
		
	}
	
	public TournamentConfig(final TournamentConfig tournamentConfig) {
		
		avatar=tournamentConfig.avatar;
		startLife=tournamentConfig.startLife;
		handSize=tournamentConfig.handSize;
		games=tournamentConfig.games;
		playerProfile=tournamentConfig.playerProfile;
		opponentProfile=tournamentConfig.opponentProfile;
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
		
	public MagicPlayerProfile getPlayerProfile() {
		
		return playerProfile;
	}

	public void setPlayerProfile(final MagicPlayerProfile profile) {

		this.playerProfile=profile;
	}
	
	public MagicPlayerProfile getOpponentProfile() {
		
		return opponentProfile;
	}
	
	public void setOpponentProfile(final MagicPlayerProfile profile) {
		
		this.opponentProfile=profile;
	}
		
	public void load(final Properties properties) {
		
		avatar=Integer.parseInt(properties.getProperty(AVATAR,""+avatar));
		name=properties.getProperty(NAME,name);
		startLife=Integer.parseInt(properties.getProperty(START_LIFE,""+startLife));
		handSize=Integer.parseInt(properties.getProperty(HAND_SIZE,""+handSize));
		games=Integer.parseInt(properties.getProperty(GAMES,""+games));
		playerProfile=new MagicPlayerProfile(properties.getProperty(PLAYER,playerProfile.getColorText()));
		opponentProfile=new MagicPlayerProfile(properties.getProperty(OPPONENT,opponentProfile.getColorText()));
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
		properties.setProperty(PLAYER,playerProfile.getColorText());
		properties.setProperty(OPPONENT,opponentProfile.getColorText());
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