package magic.ai;

import java.util.HashMap;
import java.util.Map;

public class ArtificialScoreBoard {

	private final Map<Long,ArtificialScore> gameScoresMap;
	
	public ArtificialScoreBoard() {

		gameScoresMap=new HashMap<Long,ArtificialScore>();
	}

	public synchronized void setGameScore(final long gameId,final ArtificialScore aiScore) {
		
		gameScoresMap.put(gameId,aiScore);
	}
	
	public synchronized ArtificialScore getGameScore(final long gameId) {

		return gameScoresMap.get(gameId);
	}	
	
	public synchronized void clear() {
		
		gameScoresMap.clear();
	}
}