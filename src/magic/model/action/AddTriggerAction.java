package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.trigger.MagicPermanentTrigger;
import magic.model.trigger.MagicTrigger;

public class AddTriggerAction extends MagicAction {

    private final MagicPermanent permanent;
    private final MagicTrigger<?> trigger;
    private MagicPermanentTrigger permanentTrigger;

    public AddTriggerAction(final MagicPermanent aPermanent,final MagicTrigger<?> aTrigger) {
        permanent = aPermanent;
        trigger = aTrigger;
    }

    public AddTriggerAction(final MagicTrigger<?> trigger) {
        this(MagicPermanent.NONE, trigger);
    }

    @Override
    public void doAction(final MagicGame game) {
        if (permanent == MagicPermanent.NONE || permanent.isValid()) {
            permanentTrigger=game.addTrigger(permanent,trigger);
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        if (permanentTrigger != null) {
            game.removeTrigger(permanentTrigger);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+" ("+permanent+','+trigger+')';
    }
}
