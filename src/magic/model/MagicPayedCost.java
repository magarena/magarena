package magic.model;

import magic.model.choice.MagicPayManaCostResult;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetNone;

public class MagicPayedCost {

    public static final MagicPayedCost NO_COST = new MagicPayedCost();
    
    private MagicTarget target;
    private int x;
    
    public MagicPayedCost() {
        clear();
    }
    
    MagicPayedCost(final MagicCopyMap copyMap,final MagicPayedCost payedCost) {
        target = copyMap.copy(payedCost.target);
        x = payedCost.x;
    }

    private void clear() {
        target = MagicTargetNone.getInstance();
        x = 0;
    }
    
    public void setTarget(final MagicTarget target) {
        this.target = target;
    }
    
    public MagicTarget getTarget() {
        return target;
    }
    
    public void setX(final int x) {
        this.x = x;    
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
}
