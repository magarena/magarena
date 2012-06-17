package magic.model.action;

import magic.model.MagicGame;
import magic.model.target.MagicTarget;

public class MagicPreventDamageAction extends MagicAction {
    
    private final MagicTarget target;
    private final int amount;
    private int oldAmount;
    
    public MagicPreventDamageAction(final MagicTarget target,final int amount) {
        
        this.target=target;
        this.amount=amount;
    }
    
    @Override
    public void doAction(final MagicGame game) {

        oldAmount=target.getPreventDamage();
        target.setPreventDamage(oldAmount+amount);
    }

    @Override
    public void undoAction(final MagicGame game) {

        target.setPreventDamage(oldAmount);
    }
}