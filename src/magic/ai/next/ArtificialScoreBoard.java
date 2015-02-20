package magic.ai.next;

import magic.data.LRUCache;

public class ArtificialScoreBoard {

    private final LRUCache<Long,ArtificialScore> gameScoresMap;

    ArtificialScoreBoard() {
        gameScoresMap=new LRUCache<Long,ArtificialScore>(100000);
    }

    synchronized void setGameScore(final long gameId,final ArtificialScore aiScore) {
        gameScoresMap.put(gameId,aiScore);
    }

    synchronized ArtificialScore getGameScore(final long gameId) {
        return gameScoresMap.get(gameId);
    }

    synchronized void clear() {
        gameScoresMap.clear();
    }
}
