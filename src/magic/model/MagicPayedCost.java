package magic.model;

import magic.model.choice.MagicPayManaCostResult;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetNone;

public class MagicPayedCost implements MagicCopyable {

    public static final MagicPayedCost NO_COST = new MagicPayedCost();
    public static final MagicPayedCost NOT_SPELL = new MagicPayedCost();

    private MagicTarget target;
    private int x;
    private int kicker;

    public MagicPayedCost() {
        target = MagicTargetNone.getInstance();
        x = 0;
        kicker = 0;
    }

    public MagicPayedCost(final MagicPayedCost payedCost) {
        target = payedCost.target;
        x = payedCost.x;
        kicker = payedCost.kicker;
    }

    public MagicPayedCost(final MagicCopyMap copyMap,final MagicPayedCost payedCost) {
        target = copyMap.copy(payedCost.target);
        x = payedCost.x;
        kicker = payedCost.kicker;
    }

    @Override
    public MagicCopyable copy(final MagicCopyMap copyMap) {
        return new MagicPayedCost(copyMap, this);
    }

    private void setTarget(final MagicTarget aTarget) {
        target = aTarget;
    }

    public MagicTarget getTarget() {
        return target;
    }

    private void setX(final int newX) {
        if (newX != 0) {
            x = newX;
        }
    }

    public int getX() {
        return x;
    }

    void set(final Object choiceResult) {
        if (choiceResult instanceof MagicTarget) {
            setTarget((MagicTarget)choiceResult);
        } else if (choiceResult instanceof MagicPayManaCostResult) {
            setX(((MagicPayManaCostResult)choiceResult).getX());
        }
    }

    public void setKicker(final int aKicker) {
        kicker = aKicker;
    }

    public int getKicker() {
        return kicker;
    }

    public boolean isKicked() {
        return kicker > 0;
    }

    public long getStateId() {
        return MurmurHash3.hash(new long[] {
            MagicObjectImpl.getStateId(target),
            x,
            kicker
        });
    }
}
