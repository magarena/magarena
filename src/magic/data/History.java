package magic.data;

import magic.MagicMain;
import magic.model.MagicDuel;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class History {
	
	private static final String HISTORY_EXTENSION = ".hist";
	
	private static final String GAMES_PLAYED = "gamesPlayed";
	private static final String GAMES_WON = "gamesWon";
	private static final String DUELS_PLAYED = "duelsPlayed";
	private static final String DUELS_WON = "duelsWon";
	
	private static int gamesPlayed;
	private static int gamesWon;
	private static int duelsPlayed;
	private static int duelsWon;
	
	private static String name = "";
	private final MagicDuel duel;
	
	public History(MagicDuel duel) {
		this.duel = duel;
	}
	
	public static String getHistoryFolder() {
		return MagicMain.getGamePath() + File.separator + "history";
	}
	
	public static void createHistoryFolder() {
		final File HistoryFolderFile = new File(getHistoryFolder());
		if (!HistoryFolderFile.exists() && !HistoryFolderFile.mkdir()) {
            System.err.println("WARNING. Unable to create " + getHistoryFolder());
		}
	}
	
	public void update(final String name,boolean won) {
		gamesPlayed++;
		if (won) {
			gamesWon++;
		}
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
		duelsPlayed = Integer.parseInt(properties.getProperty(DUELS_PLAYED,"0"));
		duelsWon = Integer.parseInt(properties.getProperty(DUELS_WON,"0"));
	}
	
	public void loadHistory(final String name) {
		History.name = name;
        load(FileIO.toProp(new File(getHistoryFolder() +
        		File.separator + name + HISTORY_EXTENSION)));
	}
	
	private void save(final Properties properties) {
		properties.setProperty(GAMES_PLAYED,String.valueOf(gamesPlayed));
		properties.setProperty(GAMES_WON,String.valueOf(gamesWon));
		properties.setProperty(DUELS_PLAYED,String.valueOf(duelsPlayed));
		properties.setProperty(DUELS_WON,String.valueOf(duelsWon));
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

	public static int getDuelsPlayed() {
		return duelsPlayed;
	}
	
	public static int getDuelsWon() {
		return duelsWon;
	}
}
