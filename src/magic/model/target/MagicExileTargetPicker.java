package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicExileTargetPicker extends MagicTargetPicker<MagicPermanent> {

    private static final MagicExileTargetPicker INSTANCE = new MagicExileTargetPicker();

    private MagicExileTargetPicker() {}

    public static MagicExileTargetPicker create() {
        return INSTANCE;
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
        final int score=permanent.getScore();
        return permanent.getController()==player?-score:score;
    }

}
