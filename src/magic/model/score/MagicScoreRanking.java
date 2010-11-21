package magic.model.score;

import java.util.Collection;

public interface MagicScoreRanking {

	public boolean addScore(final int score);
	
	public void addScoreResult(final MagicScoreResult result);
	
	public Collection<Object> getResults();
}