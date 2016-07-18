package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPlayer;

public abstract class MagicAction {

    private MagicPlayer scorePlayer;
    private int score;

    final void setScore(final MagicPlayer aScorePlayer, final int aScore) {
        scorePlayer = aScorePlayer;
        score = aScore;
    }

    public final int getScore(final MagicPlayer player) {
        return (player == scorePlayer) ? score : -score;
    }

    public abstract void doAction(final MagicGame game);

    public abstract void undoAction(final MagicGame game);

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
