package magic.model;

import magic.MagicMain;
import magic.ai.MagicAI;
import magic.data.CubeDefinitions;
import magic.data.DeckGenerator;
import magic.data.DeckUtils;
import magic.data.GeneralConfig;
import magic.data.TournamentConfig;
import magic.model.phase.MagicDefaultGameplay;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MagicTournament {
			
	private static final String OPPONENT="opponent";
	private static final String GAME="game";
	private static final String PLAYED="played";
	private static final String WON="won";
	private static final String START="start";
	private static final String COMPUTER="Computer";
	
	private final TournamentConfig configuration;
	private MagicPlayerDefinition playerDefinitions[];
	private MagicAI ais[];
	private int opponentIndex;
	private int gameNr;
	private int gamesPlayed;
	private int gamesWon;
	private int startPlayer;
	private final int[] difficulty = new int[2];
	
	public MagicTournament(final TournamentConfig configuration) {
		this.configuration=configuration;
		ais=configuration.getPlayerAIs();
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
	
	private MagicPlayerDefinition getOpponent() {
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

	private void determineStartPlayer() {
		startPlayer=MagicRandom.nextInt(2);
	}
	
	public void setStartPlayer(final int startPlayer) {
		this.startPlayer=startPlayer;
	}
	
	private int getStartPlayer() {
		return startPlayer;
	}

    public void setAIs(final MagicAI[] aAIs) {
    	this.ais = aAIs;
    }

    public MagicAI[] getAIs() {
        return ais;
    }
	
	public void setDifficulty(final int diff) {
        setDifficulty(0,diff);
        setDifficulty(1,diff);
	}
	
    public void setDifficulty(final int idx, final int diff) {
        difficulty[idx] = diff;
	}
	
    int getDifficulty() {
		return getDifficulty(0);
	}
	
	int getDifficulty(final int idx) {
        return difficulty[idx];
	}
	
	public void updateDifficulty() {
		difficulty[0] = GeneralConfig.getInstance().getDifficulty();
		difficulty[1] = GeneralConfig.getInstance().getDifficulty();
	}
	
	public boolean isEditable() {
		return gameNr==1;
	}
	
	public boolean isFinished() {
		return getGamesPlayed()==getGamesTotal();
	}
	
	void advance(final boolean won) {
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
	
	private static List<Integer> getAvatarIndices(final int avatars) {
		final List<Integer> indices=new ArrayList<Integer>();
		for (int index=0;index<avatars;index++) {
			
			indices.add(index);
		}
		return indices;
	}

	private MagicPlayerDefinition[] createPlayers() {
		final Theme theme=ThemeFactory.getInstance().getCurrentTheme();
		final List<Integer> avatars=getAvatarIndices(theme.getNumberOfAvatars());

		final MagicPlayerDefinition players[]=new MagicPlayerDefinition[2];

		final int playerFace=configuration.getAvatar()%theme.getNumberOfAvatars();
		final MagicPlayerDefinition player=new MagicPlayerDefinition(configuration.getName(),false,configuration.getPlayerProfile(),playerFace);
		players[0]=player;
		avatars.remove(playerFace);
		
		final int findex=MagicRandom.nextInt(avatars.size());
		final Integer computerFace=avatars.get(findex);
		players[1]=new MagicPlayerDefinition(COMPUTER,true,configuration.getOpponentProfile(),computerFace);

		return players;
	}
		
	public MagicGame nextGame(final boolean sound) {
        //create players
        final MagicPlayer player   = new MagicPlayer(configuration.getStartLife(),playerDefinitions[0],0);
        final MagicPlayer opponent = new MagicPlayer(configuration.getStartLife(),playerDefinitions[opponentIndex],1);
        
        //give the AI player extra life
        opponent.setLife(opponent.getLife() + GeneralConfig.getInstance().getExtraLife());

        //determine who starts first
        final MagicPlayer start    = startPlayer == 0 ? player : opponent;

        //create game
        final MagicGame game = MagicGame.create(
                this,
                MagicDefaultGameplay.getInstance(),
                new MagicPlayer[]{player,opponent},
                start,
                sound);

        //create hand and library
        player.createHandAndLibrary(configuration.getHandSize());
        opponent.createHandAndLibrary(configuration.getHandSize());
        return game;
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
	
	public void setPlayers(final MagicPlayerDefinition aPlayerDefinitions[]) {
		this.playerDefinitions=aPlayerDefinitions;
	}
	
	private void buildDecks() {
		final MagicCubeDefinition cubeDefinition=CubeDefinitions.getInstance().getCubeDefinition(configuration.getCube());
        final DeckGenerator generator=new DeckGenerator(cubeDefinition);
		for (final MagicPlayerDefinition player : playerDefinitions) {
			if (player.getProfile().getNrOfColors()==0) {
				DeckUtils.loadRandomDeck(player);
			} else {
				player.generateDeck(generator);
			}
		}
	}
					
	public void initialize() {
		playerDefinitions=createPlayers();
		buildDecks();
	}
	
	public static final File getTournamentFile() {
		return new File(MagicMain.getGamePath(),"tournament.txt");		
	}
	
	private static String getPlayerPrefix(final int index) {
		return "p"+(index+1)+".";
	}
	
	private void save(final Properties properties) {
		configuration.save(properties);
		
		properties.setProperty(OPPONENT,Integer.toString(opponentIndex));
		properties.setProperty(GAME,Integer.toString(gameNr));
		properties.setProperty(PLAYED,Integer.toString(gamesPlayed));
		properties.setProperty(WON,Integer.toString(gamesWon));
		properties.setProperty(START,Integer.toString(startPlayer));
		
		for (int index=0;index<playerDefinitions.length;index++) {
			playerDefinitions[index].save(properties,getPlayerPrefix(index));
		}
	}
	
	public void save(final File file) {
        final Properties properties=new Properties();
        save(properties);
        try { //save to file 
            magic.data.FileIO.toFile(file, properties, "Tournament");
            System.err.println("Saved tournament");
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable save tournament to " + file);
        }
	}
	
	private void load(final Properties properties) {
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
        load(magic.data.FileIO.toProp(file));
	}
	
	public void restart() {
		opponentIndex=1;
		gameNr=1;
		gamesPlayed=0;
		gamesWon=0;
		determineStartPlayer();
	}
}
