package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicBouncePermanentPicker extends MagicTargetPicker<MagicPermanent> {

    private static final MagicBouncePermanentPicker INSTANCE = new MagicBouncePermanentPicker();

    private MagicBouncePermanentPicker() {}

    public static MagicBouncePermanentPicker create() {
        return INSTANCE;
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
        int score=permanent.getScore();
        if (permanent.getComeIntoPlayTriggers().size()>0) {
            score-=1000;
        }
        return permanent.getController()==player?-score:score;
    }
}
