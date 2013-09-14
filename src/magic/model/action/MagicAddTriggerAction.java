package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.trigger.MagicPermanentTrigger;
import magic.model.trigger.MagicTrigger;

public class MagicAddTriggerAction extends MagicAction {

    private final MagicPermanent permanent;
    private final MagicTrigger<?> trigger;
    private MagicPermanentTrigger permanentTrigger;

    public MagicAddTriggerAction(final MagicPermanent permanent,final MagicTrigger<?> trigger) {
        this.permanent=permanent;
        this.trigger=trigger;
    }

    public MagicAddTriggerAction(final MagicTrigger<?> trigger) {
        this(MagicPermanent.NONE, trigger);
    }

    @Override
    public void doAction(final MagicGame game) {
        if (permanent.isValid()) {
            permanentTrigger=game.addTrigger(permanent,trigger);
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        if (permanent.isValid()) {
            game.removeTrigger(permanentTrigger);
        }
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName()+" ("+permanent+','+trigger+')';
    }
}
