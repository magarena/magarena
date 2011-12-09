package magic.data;

import magic.MagicMain;
import magic.model.MagicDuel;
import magic.model.MagicGame;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class History {
	
	public static final String HISTORY_FOLDER = "history";
	private static final String HISTORY_EXTENSION = ".hist";
	
	private static final String GAMES_PLAYED = "gamesPlayed";
	private static final String GAMES_WON = "gamesWon";
	private static final String DUELS_PLAYED = "duelsPlayed";
	private static final String DUELS_WON = "duelsWon";
	private static final String TURNS_PLAYED = "turnsPlayed";
	private static final String LIFE_LEFT_PLAYER = "lifeLeftPlayer";
	private static final String LIFE_LEFT_AI = "lifeLeftAI";
	
	private static int gamesPlayed;
	private static int gamesWon;
	private static int turnsPlayed;
	private static int duelsPlayed;
	private static int duelsWon;
	private static int lifeLeftPlayer;
	private static int lifeLeftAI;
	
	private static String name = "";
	private final MagicDuel duel;
	
	public History(MagicDuel duel) {
		this.duel = duel;
	}
	
	public static String getHistoryFolder() {
		return MagicMain.getGamePath() + File.separator + HISTORY_FOLDER;
	}
	
	public static void createHistoryFolder() {
		final File HistoryFolderFile = new File(getHistoryFolder());
		if (!HistoryFolderFile.exists() && !HistoryFolderFile.mkdir()) {
            System.err.println("WARNING. Unable to create " + getHistoryFolder());
		}
	}
	
	public void update(final String name,final boolean won,final MagicGame game) {
		gamesPlayed++;
		if (won) {
			gamesWon++;
			lifeLeftPlayer += game.getPlayer(0).getLife();
		} else {
			lifeLeftAI += game.getPlayer(1).getLife();
		}
		turnsPlayed += game.getTurn();
		if (duel.isFinished()) {
			duelsPlayed++;
			final int duelGamesPlayed = duel.getGamesPlayed();
			final int duelGamesWon = duel.getGamesWon();
			if ((duelGamesWon*100) / duelGamesPlayed >= 50) {
				duelsWon++;
			}
		}
		saveHistory(name + HISTORY_EXTENSION);
	}
	
	private void load(final Properties properties) {
		gamesPlayed = Integer.parseInt(properties.getProperty(GAMES_PLAYED,"0"));
		gamesWon = Integer.parseInt(properties.getProperty(GAMES_WON,"0"));
		turnsPlayed = Integer.parseInt(properties.getProperty(TURNS_PLAYED,"0"));
		duelsPlayed = Integer.parseInt(properties.getProperty(DUELS_PLAYED,"0"));
		duelsWon = Integer.parseInt(properties.getProperty(DUELS_WON,"0"));
		lifeLeftPlayer = Integer.parseInt(properties.getProperty(LIFE_LEFT_PLAYER,"0"));
		lifeLeftAI = Integer.parseInt(properties.getProperty(LIFE_LEFT_AI,"0"));
	}
	
	public void loadHistory(final String name) {
		History.name = name;
        load(FileIO.toProp(new File(getHistoryFolder() +
        		File.separator + name + HISTORY_EXTENSION)));
	}
	
	private void save(final Properties properties) {
		properties.setProperty(GAMES_PLAYED,String.valueOf(gamesPlayed));
		properties.setProperty(GAMES_WON,String.valueOf(gamesWon));
		properties.setProperty(TURNS_PLAYED,String.valueOf(turnsPlayed));
		properties.setProperty(DUELS_PLAYED,String.valueOf(duelsPlayed));
		properties.setProperty(DUELS_WON,String.valueOf(duelsWon));
		properties.setProperty(LIFE_LEFT_PLAYER,String.valueOf(lifeLeftPlayer));
		properties.setProperty(LIFE_LEFT_AI,String.valueOf(lifeLeftAI));
	}
	
	public void saveHistory(final String filename) {
        final Properties properties = new Properties();
        save(properties);
        try { //save history
            FileIO.toFile(new File(getHistoryFolder() + File.separator + filename),
            		properties,
            		"History");
            System.err.println("Saved " + filename);
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to save " + filename + ex.getMessage());
        }
	}

	public static String getName() {
		return name;
	}
	
	public static int getGamesPlayed() {
		return gamesPlayed;
	}

	public static int getGamesWon() {
		return gamesWon;
	}

	public static int getTurnsPlayed() {
		return turnsPlayed;
	}
	
	public static int getDuelsPlayed() {
		return duelsPlayed;
	}
	
	public static int getDuelsWon() {
		return duelsWon;
	}
	
	public static int getLifeLeftPlayer() {
		return lifeLeftPlayer;
	}
	
	public static int getLifeLeftAI() {
		return lifeLeftAI;
	}
}
