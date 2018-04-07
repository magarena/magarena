package magic.ai;

public class ArtificialScore {

    static final ArtificialScore INVALID_SCORE=new ArtificialScore(0,0);
    static final int MAX = 99900000;
    static final int MIN = -MAX;

    private final int score;
    private final int depth;

    ArtificialScore(final int aScore,final int aDepth) {
        int boundedScore = Math.min(MAX,aScore);
        score = Math.max(MIN,boundedScore);
        depth = aDepth;
    }

    ArtificialScore getScore(final int depthIncr) {
        if (this==INVALID_SCORE) {
            return INVALID_SCORE;
        }
        return new ArtificialScore(score,depth+depthIncr);
    }

    int getScore() {
        return score;
    }

    boolean isBetter(final ArtificialScore other,final boolean max) {
        if (other==INVALID_SCORE) {
            return false;
        } else if (this==INVALID_SCORE) {
            return true;
        } else if (score==other.score) {
            //in my favor, prefer lower depth
            if ((max && score > 0) || (!max && score < 0)) {
                return other.depth < depth;
            } else {
                return other.depth > depth;
            }
        } else if (max) {
            return other.score > score;
        } else {
            return other.score < score;
        }
    }

    @Override
    public String toString() {
        if (this==INVALID_SCORE) {
            return "none";
        }
        return String.valueOf(score) + " at " + depth;
    }

    @Override
    public int hashCode() {
        return 31*score+depth;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this==obj) {
            return true;
        }
        if (obj==null||getClass()!=obj.getClass()) {
            return false;
        }
        final ArtificialScore other=(ArtificialScore)obj;
        return score==other.score&&depth==other.depth;
    }
}
