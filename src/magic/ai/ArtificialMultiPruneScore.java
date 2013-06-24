package magic.ai;

public class ArtificialMultiPruneScore implements ArtificialPruneScore {

    private final int maxBest;
    private final int minWorst;

    private ArtificialMultiPruneScore(final int maxBest,final int minWorst) {
        this.maxBest=maxBest;
        this.minWorst=minWorst;
    }

    ArtificialMultiPruneScore() {
        this(Integer.MIN_VALUE,Integer.MAX_VALUE);
    }

    @Override
    public int getScore() {
        return maxBest; // Does matter for game id.
    }

    @Override
    public boolean pruneScore(final int score,final boolean best) {
        return best?score>minWorst:score<maxBest;
    }

    @Override
    public ArtificialPruneScore getPruneScore(final int score,final boolean best) {
        if (best) {
            return score>maxBest?new ArtificialMultiPruneScore(score,minWorst):this;
        } else {
            return score<minWorst?new ArtificialMultiPruneScore(maxBest,score):this;
        }
    }

    @Override
    public String toString() {
        return maxBest+" / "+minWorst;
    }
}
