package magic.ai;

public interface ArtificialPruneScore {

    int getScore();

    boolean pruneScore(final int score,final boolean best);

    ArtificialPruneScore getPruneScore(final int score,final boolean best);
}
