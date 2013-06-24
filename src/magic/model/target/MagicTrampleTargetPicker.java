package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

import java.util.Set;

public class MagicTrampleTargetPicker extends MagicTargetPicker<MagicPermanent> {

    private static final MagicTrampleTargetPicker INSTANCE=new MagicTrampleTargetPicker();

    private MagicTrampleTargetPicker() {}

    public static MagicTrampleTargetPicker create() {
        return INSTANCE;
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
        if (!permanent.hasAbility(MagicAbility.Trample)&&
            !permanent.hasAbility(MagicAbility.Defender)&&
            !permanent.hasAbility(MagicAbility.CannotAttackOrBlock)) {
            return 1+permanent.getPower();
        }
        return 0;
    }
}
