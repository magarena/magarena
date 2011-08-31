package magic.model.score;

import java.util.Collection;
import java.util.Collections;

public class MagicSingleScoreRanking implements MagicScoreRanking {

	private final boolean best;
	private MagicScoreResult result;
	private int bestScore;
	
	public MagicSingleScoreRanking(final boolean best) {
		this.best=best;
		result=null;
		bestScore=best?Integer.MIN_VALUE:Integer.MAX_VALUE;
	}

	@Override
	public boolean addScore(int score) {
		return best?score>bestScore:score<bestScore;
	}

	@Override
	public void addScoreResult(MagicScoreResult result) {
		this.result=result;
		bestScore=result.getScore();
	}

	@Override
	public Collection<Object> getResults() {
		return Collections.<Object>singletonList(result);
	}
}
