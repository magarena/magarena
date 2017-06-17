package magic.model.target;

import magic.model.MagicAmount;
import magic.model.MagicAmountFactory;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.event.MagicEvent;

public class MagicWeakenTargetPicker extends MagicTargetPicker<MagicPermanent> {

    private static final int ATTACKING_BLOCKED=4<<8;
    private static final int ATTACKING=3<<8;
    private static final int BLOCKING=2<<8;
    private static final int CAN_TAP=1<<8;

    private int amountToughness;
    private int amountPower;
    private final MagicAmount count;

    public MagicWeakenTargetPicker(final MagicAmount aCount) {
        amountToughness = -1;
        count = aCount;
    }

    public MagicWeakenTargetPicker(final int aAmountPower,final int aAmountToughness) {
        amountToughness = aAmountToughness;
        amountPower = aAmountPower;
        count = MagicAmountFactory.One;
    }

    @Override
    public MagicWeakenTargetPicker create(final String arg) {
        final String[] args = arg.replace('+','0').split("/");
        final int p = -Integer.parseInt(args[0]);
        final int t = -Integer.parseInt(args[1]);
        return new MagicWeakenTargetPicker(p, t);
    }

    @Override
    protected void setEvent(final MagicEvent event) {
        if (amountToughness < 0 || count.isConstant() == false) {
            amountToughness = -count.getAmount(event);
        }
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
        final MagicPowerToughness pt=permanent.getPowerToughness();

        final int lethalToughness=pt.getPositiveToughness()-permanent.getDamage();
        if (lethalToughness<=amountToughness) {
            return permanent.getScore()<<12;
        }

        int score=0;

        // First level.
        if (permanent.isAttacking()) {
            if (permanent.isBlocked()) {
                score=ATTACKING_BLOCKED;
            } else {
                score=ATTACKING;
            }
        } else if (permanent.isBlocking()) {
            score=BLOCKING;
        } else if (permanent.canTap()) {
            score=CAN_TAP;
        }

        if (amountToughness>0) {
            // Second level.
            score+=Math.max(15,lethalToughness)<<4;
        }

        // Third level.
        score+=Math.max(15,pt.getPositivePower() - amountPower);

        return score;
    }
}
