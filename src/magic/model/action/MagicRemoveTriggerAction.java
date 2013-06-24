package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.trigger.MagicPermanentTrigger;
import magic.model.trigger.MagicTrigger;

public class MagicRemoveTriggerAction extends MagicAction {

    private final MagicPermanent permanent;
    private final MagicTrigger<?> trigger;
    private MagicPermanentTrigger permanentTrigger;

    public MagicRemoveTriggerAction(final MagicPermanent permanent,final MagicTrigger<?> trigger) {
        this.permanent=permanent;
        this.trigger=trigger;
    }

    public MagicRemoveTriggerAction(final MagicTrigger<?> trigger) {
        this(MagicPermanent.NONE, trigger);
    }

    @Override
    public void doAction(final MagicGame game) {
        permanentTrigger=game.removeTrigger(permanent,trigger);
    }

    @Override
    public void undoAction(final MagicGame game) {
        game.addTrigger(permanentTrigger);
    }
}
