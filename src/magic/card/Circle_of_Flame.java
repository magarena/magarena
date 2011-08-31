package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

// The part of this card that interacts with planeswalkers is ignored
public class Circle_of_Flame {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenAttacks) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPermanent creature = (MagicPermanent)data;
			final MagicPlayer controller = creature.getController();
			return (controller != permanent.getController() &&
					!creature.hasAbility(game,MagicAbility.Flying)) ?
                new MagicEvent(
                        permanent,
                        controller,
                        new Object[]{permanent,creature},
                        this,
                        permanent + " deals 1 damage to attacking creature without flying."):
                null;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicTarget target = (MagicTarget)data[1];
			final MagicDamage damage = new MagicDamage((MagicPermanent)data[0],target,1,false);
            game.doAction(new MagicDealDamageAction(damage));
		}
    };
}
