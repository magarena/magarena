package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicShroudTargetPicker extends MagicTargetPicker<MagicPermanent> {

    private static final MagicShroudTargetPicker INSTANCE=new MagicShroudTargetPicker();

    private MagicShroudTargetPicker() {}

    public static MagicShroudTargetPicker create() {
        return INSTANCE;
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
        if (permanent.hasAbility(MagicAbility.Shroud) ||
            permanent.hasAbility(MagicAbility.Hexproof)) {
            return 0;
        }
        return permanent.getScore();
    }
}
