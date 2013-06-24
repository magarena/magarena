package magic.ai;

class ArtificialPruneScoreRef {

    private ArtificialPruneScore pruneScore;

    public ArtificialPruneScoreRef(final ArtificialPruneScore pScore) {
        pruneScore = pScore;
    }

    public void update(final int score) {
        pruneScore = pruneScore.getPruneScore(score,true);
    }

    public ArtificialPruneScore get() {
        return pruneScore;
    }
}
