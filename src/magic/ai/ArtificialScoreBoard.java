package magic.ai;

import magic.data.LRUCache;

public class ArtificialScoreBoard {

	private final LRUCache<Long,ArtificialScore> gameScoresMap;
	
	public ArtificialScoreBoard() {
		gameScoresMap=new LRUCache<Long,ArtificialScore>(100000);
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
