package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicLifelinkTargetPicker extends MagicTargetPicker<MagicPermanent> {

    private static final MagicLifelinkTargetPicker INSTANCE = new MagicLifelinkTargetPicker();

    private static final int ATTACKING_UNBLOCKED = 5<<8;
    private static final int BLOCKED_OR_BLOCKING = 4<<8;
    private static final int CAN_TAP = 3<<8;
    private static final int DOUBLE_STRIKE = 2<<8;
    private static final int FIRST_STRIKE = 1<<8;

    private MagicLifelinkTargetPicker() {}

    public static MagicLifelinkTargetPicker create() {
        return INSTANCE;
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
        int score = 0;

        // no score for ability overlap or not being able to deal combat damage
        if (permanent.hasAbility(MagicAbility.Lifelink) ||
            permanent.hasAbility(MagicAbility.CannotAttackOrBlock)) {
            return 0;
        }

        if (permanent.isAttacking()) {
            if (!permanent.isBlocked()) {
                // unblocked attacker has the highest chance of gaining life
                score = ATTACKING_UNBLOCKED;
            } else {
                // possible to not gain life when blocker has first strike
                score = BLOCKED_OR_BLOCKING;
            }
        } else if (permanent.isBlocking()) {
            // possible to not gain life when attacker has first strike
            score = BLOCKED_OR_BLOCKING;
        } else if (permanent.canTap()) {
            // can be in combat later or possibly use a damage ability
            score = CAN_TAP;
        }

        if (permanent.hasAbility(MagicAbility.DoubleStrike)) {
            // chance to deal combat damage twice
            score += DOUBLE_STRIKE;
        }
        if (permanent.hasAbility(MagicAbility.FirstStrike)) {
            // higher chance to deal combat damage
            score += FIRST_STRIKE;
        }

        return permanent.getPower() + score;
    }
}
