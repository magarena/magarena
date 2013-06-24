package magic.model.score;

import java.util.Collection;

public interface MagicScoreRanking {

    boolean addScore(final int score);

    void addScoreResult(final MagicScoreResult result);

    Collection<Object> getResults();
}
