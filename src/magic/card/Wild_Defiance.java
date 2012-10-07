package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicItemOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenTargetedTrigger;

import java.util.Collection;

public class Wild_Defiance {
    public static final MagicWhenTargetedTrigger T = new MagicWhenTargetedTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicItemOnStack itemOnStack) {
            if (!itemOnStack.getCardDefinition().isInstant() &&
                !itemOnStack.getCardDefinition().isSorcery()) {
                return MagicEvent.NONE;
            }
                    
            final Collection<MagicPermanent> targets = game.filterPermanents(
                    permanent.getController(),
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            MagicPermanent targetedPerm = MagicPermanent.NONE;
            for (final MagicPermanent perm : targets) {
                if (itemOnStack.containsInChoiceResults(perm) &&
                    perm.isCreature() &&
                    perm.getController() == permanent.getController()) {
                    targetedPerm = perm;
                    break;
                }
            }
            return (targetedPerm != MagicPermanent.NONE) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    targetedPerm,
                    this,
                    targetedPerm + " gets +3/+3 until end of turn."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicChangeTurnPTAction(event.getRefPermanent(),3,3));
        }
    };
}
