package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicItemOnStack;
import magic.model.trigger.MagicWhenTargetedTrigger;

public class Ashenmoor_Liege {
    public static final MagicWhenTargetedTrigger T = new MagicWhenTargetedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicItemOnStack target) {
        	MagicPlayer targetPlayer = target.getController();
            return (target.containsInChoiceResults(permanent) &&
            		targetPlayer != permanent.getController()) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new Object[]{targetPlayer},
                    this,
                    targetPlayer + " loses 4 life."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
        	game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],-4));
        }
    };
}
