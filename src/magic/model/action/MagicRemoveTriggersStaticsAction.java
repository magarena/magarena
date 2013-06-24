package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.mstatic.MagicPermanentStatic;
import magic.model.trigger.MagicPermanentTrigger;
import magic.model.trigger.MagicTriggerType;

import java.util.Collection;

public class MagicRemoveTriggersStaticsAction extends MagicAction {

    private final MagicPermanent permanent;
    private Collection<MagicPermanentTrigger> removedTriggers;
    private Collection<MagicPermanentStatic> removedStatics;

    public MagicRemoveTriggersStaticsAction(final MagicPermanent permanent) {
        this.permanent=permanent;
    }

    @Override
    public void doAction(final MagicGame game) {
        // Trigger
        removedTriggers = game.removeTriggers(permanent);

        // Static
        removedStatics = game.removeStatics(permanent);
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
