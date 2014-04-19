package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicFlyingTargetPicker extends MagicTargetPicker<MagicPermanent> {

    private static final MagicFlyingTargetPicker INSTANCE=new MagicFlyingTargetPicker();

    private MagicFlyingTargetPicker() {}

    public static MagicFlyingTargetPicker create() {
        return INSTANCE;
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
        if (permanent.hasAbility(MagicAbility.CannotAttackOrBlock)) {
            return 0;
        }
        final int power=permanent.getPower();
        if (permanent.hasAbility(MagicAbility.Flying)) {
            return power;
        }
        if (permanent.hasAbility(MagicAbility.Defender)) {
            return 20+power;
        }
        if (permanent.hasAbility(MagicAbility.Reach)) {
            return 50+power;
        }
        return 100+power;
    }
}
