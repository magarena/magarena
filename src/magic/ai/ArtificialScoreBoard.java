package magic.ai;

public class ArtificialScoreBoard {

	private final StateCache<Long,ArtificialScore> gameScoresMap;
	
	public ArtificialScoreBoard() {
		gameScoresMap=new StateCache<Long,ArtificialScore>(100000);
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
