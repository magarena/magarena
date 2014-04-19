package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicDeathtouchTargetPicker extends MagicTargetPicker<MagicPermanent> {

    private static final MagicDeathtouchTargetPicker INSTANCE = new MagicDeathtouchTargetPicker();

    private static final int BLOCKED = 4<<8;
    private static final int BLOCKING = 3<<8;
    private static final int CAN_TAP = 2<<8;
    private static final int FIRST_STRIKE = 1<<8;

    private MagicDeathtouchTargetPicker() {
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
        int score = 0;

        // no score for ability overlap or not being able to deal combat damage
        if (permanent.hasAbility(MagicAbility.Deathtouch) ||
            permanent.hasAbility(MagicAbility.CannotAttackOrBlock)) {
            return 0;
        }

        if (permanent.isBlocked()) {
            score = BLOCKED;
            // possibility to destroy more than one blocker
        } else if (permanent.isBlocking()) {
            // good chance to destroy attacker
            score = BLOCKING;
        } else if (permanent.canTap()) {
            // can be in combat later or possibly use a damage ability
            score = CAN_TAP;
        }

        if (permanent.hasAbility(MagicAbility.FirstStrike) ||
            permanent.hasAbility(MagicAbility.DoubleStrike)) {
            // higher chance to deal combat damage
            score += FIRST_STRIKE;
        }

        return permanent.getPower() + score;
    }

    public static MagicDeathtouchTargetPicker create() {
        return INSTANCE;
    }
}
