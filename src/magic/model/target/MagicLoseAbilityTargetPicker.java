package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicLoseAbilityTargetPicker extends MagicTargetPicker<MagicPermanent> {
    private static MagicAbility anAbility;
    private static final MagicLoseAbilityTargetPicker INSTANCE = new MagicLoseAbilityTargetPicker(anAbility);

    private MagicLoseAbilityTargetPicker(final MagicAbility ability) {
        anAbility=ability;
    }

    public static MagicLoseAbilityTargetPicker create(final MagicAbility ability) {
        anAbility=ability;
        return INSTANCE;
    }

    @Override
    protected int getTargetScore(final MagicGame game, final MagicPlayer player, final MagicPermanent permanent) {
        if (permanent.hasAbility(MagicAbility.CannotAttackOrBlock)) {
            return 0;
        }
        final int power = permanent.getPower();
        if ((permanent.hasAbility(anAbility) && permanent.isBlocking()) &&
           (anAbility != MagicAbility.Flying || 
            anAbility != MagicAbility.Defender || 
            anAbility != MagicAbility.Trample || 
            anAbility != MagicAbility.Haste)
           ) {
            return 120 + power;
        }
        if ((permanent.hasAbility(anAbility) && permanent.isBlocked()) &&
           (anAbility != MagicAbility.Flying || 
            anAbility != MagicAbility.Defender || 
            anAbility != MagicAbility.Haste)
           ) {
            return 120 + power;
        }
        if (permanent.hasAbility(anAbility) && permanent.isAttacking() && 
           (anAbility != MagicAbility.Defender ||
            anAbility != MagicAbility.Haste)) {
            return 120 + power;
        }
        if (permanent.hasAbility(anAbility)) {
            return 100 + power;
        }
        return power;
    }
}
// Could group abilities together 'Evasion', 'Attack Dependant', 'Blocking Dependant', 'Blocked Dependant'
