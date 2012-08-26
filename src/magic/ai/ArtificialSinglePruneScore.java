package magic.ai;

public class ArtificialSinglePruneScore implements ArtificialPruneScore {

    private final int score;
    private final boolean best;

    private ArtificialSinglePruneScore(final int score,final boolean best) {
        this.score=score;
        this.best=best;
    }
    
    private ArtificialSinglePruneScore() {
        this(Integer.MIN_VALUE,true);
    }

    @Override
    public int getScore() {
        return 0; // Does not matter much for game id.
    }
    
    @Override
    public boolean pruneScore(final int otherScore,final boolean otherBest) {
        if (best==otherBest) {
            return false;
        }
        return best?score>otherScore:score<otherScore;
    }    

    @Override
    public ArtificialPruneScore getPruneScore(final int otherScore,final boolean otherBest) {
        if (best!=otherBest) {
            return this;
        }
        if (best) {
            return otherScore>score?new ArtificialSinglePruneScore(otherScore,true):this;
        } else {
            return otherScore<score?new ArtificialSinglePruneScore(otherScore,false):this;
        }
    }
    
    @Override
    public String toString() {
        return score+(best?" >":" <");
    }    
}
