package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicFromGraveyardToLibraryTrigger;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Vigor {

    public static final MagicTrigger T1 = new MagicTrigger(MagicTriggerType.IfDamageWouldBeDealt,4) {
		@Override
		public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicDamage damage) {
			final MagicPlayer player=permanent.getController();
			final MagicTarget target=damage.getTarget();
			final int amount=damage.getAmount();
			if (!damage.isUnpreventable() && 
                amount > 0 && 
                target != permanent && 
                target.isPermanent() && 
                target.getController()==player) {
				final MagicPermanent creature=(MagicPermanent)target;
				if (creature.isCreature()) {
					// Prevention effect.
					damage.setAmount(0);
					return new MagicEvent(
                            permanent,
                            player,
                            new Object[]{creature,amount},
                            this,
                            "Put "+amount+" +1/+1 counters on "+creature+".");
				}
			}
			return null;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangeCountersAction(
                        (MagicPermanent)data[0],
                        MagicCounterType.PlusOne,
                        (Integer)data[1],
                        true));
		}
    };
    
    public static final MagicTrigger T2 = new MagicFromGraveyardToLibraryTrigger();
}
