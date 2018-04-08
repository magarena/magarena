package magic.model.score;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class MagicMultipleScoreRanking implements MagicScoreRanking {

    private static final Comparator<MagicScoreResult> BEST_COMPARATOR= (result1, result2) -> {
        final int sdif = result2.getScore() - result1.getScore();
        if (sdif != 0) {
            return sdif;
        }
        return result1.getPosition()-result2.getPosition();
    };

    private static final Comparator<MagicScoreResult> WORST_COMPARATOR= (result1, result2) -> {
        final int sdif = result1.getScore() - result2.getScore();
        if (sdif != 0) {
            return sdif;
        }
        return result1.getPosition()-result2.getPosition();
    };

    private final SortedSet<MagicScoreResult> results;
    private final boolean best;
    private int left;
    private int worstScore;

    public MagicMultipleScoreRanking(final int maxSize,final boolean best) {
        this.best=best;
        results= new TreeSet<>(best ? BEST_COMPARATOR : WORST_COMPARATOR);
        left=maxSize;
    }

    @Override
    public boolean addScore(final int score) {
        return left != 0 || (best ? score > worstScore : score < worstScore);
    }

    @Override
    public void addScoreResult(final MagicScoreResult result) {
        results.add(result);
        if (left==0) {
            results.remove(results.last());
        } else {
            left--;
        }
        worstScore=results.last().getScore();
    }

    @Override
    public Collection<Object> getResults() {
        return new ArrayList<>(results);
    }
}
