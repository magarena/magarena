package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.mstatic.MagicPermanentStatic;
import magic.model.trigger.MagicPermanentTrigger;

import java.util.Collection;

public class RemoveTriggersStaticsAction extends MagicAction {

    private final MagicPermanent permanent;
    private Collection<MagicPermanentTrigger> removedTriggers;
    private Collection<MagicPermanentStatic> removedStatics;

    public RemoveTriggersStaticsAction(final MagicPermanent permanent) {
        this.permanent=permanent;
    }

    @Override
    public void doAction(final MagicGame game) {
        // Trigger
        removedTriggers = game.removeTriggers(permanent);

        // Static
        removedStatics = game.removeAllStatics(permanent);
    }

    @Override
    public void undoAction(final MagicGame game) {
        // Trigger
        for (final MagicPermanentTrigger permanentTrigger : removedTriggers) {
            game.addTrigger(permanentTrigger);
        }

        // Static
        for (final MagicPermanentStatic permanentStatic : removedStatics) {
            game.addStatic(permanentStatic);
        }
    }
}
