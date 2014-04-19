package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;

public class MagicPumpTargetPicker extends MagicTargetPicker<MagicPermanent> {

    private static final MagicPumpTargetPicker INSTANCE=new MagicPumpTargetPicker();

    private static final int ATTACKING_UNBLOCKED=5<<12;
    private static final int ATTACKING_TRAMPLE=4<<12;
    private static final int ATTACKING=3<<12;
    private static final int BLOCKING=2<<12;
    private static final int CAN_TAP=1<<12;

    private static final int DOUBLE_STRIKE=2<<8;
    private static final int LIFELINK=1<<8;

    private MagicPumpTargetPicker() {}

    public static MagicPumpTargetPicker create() {
        return INSTANCE;
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
        final MagicPowerToughness pt=permanent.getPowerToughness();
        int score=0;

        // First level.
        if (permanent.isAttacking()) {
            if (permanent.isBlocked()) {
                if (permanent.hasAbility(MagicAbility.Trample)) {
                    score=ATTACKING_TRAMPLE;
                } else {
                    score=ATTACKING;
                }
            } else {
                score=ATTACKING_UNBLOCKED;
            }
        } else if (permanent.isBlocking()) {
            score=BLOCKING;
        } else if (permanent.canTap()) {
            score=CAN_TAP;
        }

        // Second level.
        if (permanent.hasAbility(MagicAbility.DoubleStrike)) {
            score+=DOUBLE_STRIKE;
        } else if (permanent.hasAbility(MagicAbility.Lifelink)) {
            score+=LIFELINK;
        }

        // Third level.
        final int power=15-pt.getPositivePower();
        if (power>0) {
            score+=power<<4;
        }

        // Fourth level.
        final int toughness=15-pt.getPositiveToughness()+permanent.getDamage();
        if (toughness>0) {
            score+=toughness;
        }

        return permanent.getController()==player?score:-score;
    }
}
