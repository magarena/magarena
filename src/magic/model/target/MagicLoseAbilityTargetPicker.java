package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicLoseAbilityTargetPicker extends MagicTargetPicker<MagicPermanent> {
    private MagicAbility ability;

    public MagicLoseAbilityTargetPicker(final MagicAbility aAbility) {
        ability = aAbility;
    }

    @Override
    protected int getTargetScore(final MagicGame game, final MagicPlayer player, final MagicPermanent permanent) {
        if (permanent.hasAbility(MagicAbility.CannotAttackOrBlock)) {
            return 0;
        }
        final int power = permanent.getPower();
        if ((permanent.hasAbility(ability) && permanent.isBlocking()) &&
           (ability != MagicAbility.Flying ||
            ability != MagicAbility.Defender ||
            ability != MagicAbility.Trample ||
            ability != MagicAbility.Haste)
           ) {
            return 120 + power;
        }
        if ((permanent.hasAbility(ability) && permanent.isBlocked()) &&
           (ability != MagicAbility.Flying ||
            ability != MagicAbility.Defender ||
            ability != MagicAbility.Haste)
           ) {
            return 120 + power;
        }
        if (permanent.hasAbility(ability) && permanent.isAttacking() &&
           (ability != MagicAbility.Defender ||
            ability != MagicAbility.Haste)) {
            return 120 + power;
        }
        if (permanent.hasAbility(ability)) {
            return 100 + power;
        }
        return power;
    }
}
// Could group abilities together 'Evasion', 'Attack Dependant', 'Blocking Dependant', 'Blocked Dependant'
