package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicLoseFlyingTargetPicker extends MagicTargetPicker<MagicPermanent> {

    private static final MagicLoseFlyingTargetPicker INSTANCE = new MagicLoseFlyingTargetPicker();

    private MagicLoseFlyingTargetPicker() {}
    
    public static MagicLoseFlyingTargetPicker create() {
        return INSTANCE;
    }

    @Override
    protected int getTargetScore(
            final MagicGame game,
            final MagicPlayer player,
            final MagicPermanent permanent) {
        final long flags = permanent.getAllAbilityFlags();
        if (MagicAbility.CannotAttackOrBlock.hasAbility(flags)) {
            return 0;
        }
        final int power = permanent.getPower();
        if (MagicAbility.Flying.hasAbility(flags)) {
            return 100 + power;
        }
        
        return power;
    }
}
