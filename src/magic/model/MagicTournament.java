package magic.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import magic.MagicMain;
import magic.data.BoosterPackGenerator;
import magic.data.GeneralConfig;
import magic.data.PlayerImages;
import magic.data.TournamentConfig;
import magic.model.phase.MagicDefaultGameplay;

public class MagicTournament {
	
	public static final int SPELL_BOOSTER_PACK_SIZE=60;
	public static final int LAND_BOOSTER_PACK_SIZE=30;
	
	private static final String OPPONENT="opponent";
	private static final String GAME="game";
	private static final String PLAYED="played";
	private static final String WON="won";
	private static final String START="start";
	private static final String COMPUTER="Computer";
	
	private final TournamentConfig configuration;
	private MagicPlayerDefinition playerDefinitions[];
	private int opponentIndex;
	private int gameNr;
	private int gamesPlayed;
	private int gamesWon;
	private int startPlayer;
	private int difficulty;
	
	public MagicTournament(final TournamentConfig configuration) {

		this.configuration=configuration;
		restart();
	}
	
	public MagicTournament() {

		this(new TournamentConfig());
	}
	
	public MagicTournament(final TournamentConfig configuration,final MagicTournament tournament) {
		
		this(configuration);
		playerDefinitions=tournament.playerDefinitions;
	}
	
	public TournamentConfig getConfiguration() {
		
		return configuration;
	}
	
	public MagicPlayerDefinition getOpponent() {
		
		return playerDefinitions[opponentIndex];
	}

	public int getGameNr() {
		
		return gameNr;
	}

	public int getGamesPlayed() {
		
		return gamesPlayed;
	}
	
	public int getGamesTotal() {
		
		return (playerDefinitions.length-1)*configuration.getNrOfGames();
	}

	public int getGamesWon() {
		
		return gamesWon;
	}

	public void determineStartPlayer() {
		
		startPlayer=MagicRandom.nextInt(2);
	}
	
	public void setStartPlayer(final int startPlayer) {
		
		this.startPlayer=startPlayer;
	}
	
	public int getStartPlayer() {
		
		return startPlayer;
	}
	
	public void setDifficulty(final int difficulty) {
		
		this.difficulty=difficulty;
	}
	
	public int getDifficulty() {
		
		return difficulty;
	}
	
	public void updateDifficulty() {
		
		difficulty=GeneralConfig.getInstance().getDifficulty();
	}
	
	public boolean isFinished() {
		
		return getGamesPlayed()==getGamesTotal();
	}
	
	public void advance(final boolean won) {

		gamesPlayed++;
		if (won) {
			gamesWon++;
			startPlayer=1;
		} else {
			startPlayer=0;
		}
		gameNr++;
		if (gameNr>configuration.getNrOfGames()) {
			gameNr=1;
			opponentIndex++;
			determineStartPlayer();
		}
	}

	private MagicPlayerDefinition[] createPlayers() {

		final List<Integer> faces=PlayerImages.getInstance().getImageIndices();

		final MagicPlayerDefinition players[]=new MagicPlayerDefinition[2];

		final MagicPlayerDefinition player=new MagicPlayerDefinition(configuration.getName(),false,configuration.getPlayerProfile(),configuration.getAvatar());
		players[0]=player;
		faces.remove(player.getFace());
		
		final int findex=MagicRandom.nextInt(faces.size());
		final Integer face=faces.get(findex);
		players[1]=new MagicPlayerDefinition(COMPUTER,true,configuration.getOpponentProfile(),face);

		return players;
	}
		
	public MagicGame nextGame() {

		final MagicPlayer player=new MagicPlayer(configuration,playerDefinitions[0],0);
		final MagicPlayer opponent=new MagicPlayer(configuration,playerDefinitions[opponentIndex],1);
		final MagicPlayer start=startPlayer==0?player:opponent;
		return new MagicGame(this,MagicDefaultGameplay.getInstance(),new MagicPlayer[]{player,opponent},start);
	}
	
	public int getNrOfPlayers() {
		
		return playerDefinitions.length;
	}
	
	public MagicPlayerDefinition getPlayer(final int index) {
		
		return playerDefinitions[index];
	}
		
	public MagicPlayerDefinition[] getPlayers() {
		
		return playerDefinitions;
	}
	
	public void setPlayers(final MagicPlayerDefinition playerDefinitions[]) {
		
		this.playerDefinitions=playerDefinitions;
	}
	
	private void buildDecks() {
		
		final BoosterPackGenerator generator=new BoosterPackGenerator();
		for (final MagicPlayerDefinition player : playerDefinitions) {
			
			player.setBoosterPack(generator.createSpellBoosterPack(SPELL_BOOSTER_PACK_SIZE));
			player.setLandBoosterPack(generator.createLandBoosterPack(LAND_BOOSTER_PACK_SIZE));			
			player.buildDeck();
		}
	}
					
	public void initialize() {

		playerDefinitions=createPlayers();
		buildDecks();
	}
	
	public static final File getTournamentFile() {

		return new File(MagicMain.getGamePath(),"tournament.txt");		
	}
	
	private String getPlayerPrefix(final int index) {
		
		return "p"+(index+1)+".";
	}
	
	public void save(final Properties properties) {
		
		configuration.save(properties);
		
		properties.setProperty(OPPONENT,""+opponentIndex);
		properties.setProperty(GAME,""+gameNr);
		properties.setProperty(PLAYED,""+gamesPlayed);
		properties.setProperty(WON,""+gamesWon);
		properties.setProperty(START,""+startPlayer);
		
		for (int index=0;index<playerDefinitions.length;index++) {
			
			playerDefinitions[index].save(properties,getPlayerPrefix(index));
		}
	}
	
	public void save(final File file) {
		
		try {
			final Properties properties=new Properties();
			save(properties);
			properties.store(new FileOutputStream(file),"Tournament");
		} catch (final IOException ex) {}
	}
	
	public void load(final Properties properties) {

		configuration.load(properties);
		
		opponentIndex=Integer.parseInt(properties.getProperty(OPPONENT,"1"));
		gameNr=Integer.parseInt(properties.getProperty(GAME,"1"));
		gamesPlayed=Integer.parseInt(properties.getProperty(PLAYED,"0"));
		gamesWon=Integer.parseInt(properties.getProperty(WON,"0"));
		startPlayer=Integer.parseInt(properties.getProperty(START,"0"));
		
		playerDefinitions=new MagicPlayerDefinition[2];
		for (int index=0;index<playerDefinitions.length;index++) {

			playerDefinitions[index]=new MagicPlayerDefinition();
			playerDefinitions[index].load(properties,getPlayerPrefix(index));
		}
	}
	
	public void load(final File file) {

		try {
			final Properties properties=new Properties();
			properties.load(new FileInputStream(file));
			load(properties);
		} catch (final IOException ex) {}
	}
	
	public void restart() {
		
		opponentIndex=1;
		gameNr=1;
		gamesPlayed=0;
		gamesWon=0;
		determineStartPlayer();
	}
}