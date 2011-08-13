package magic.card;

import magic.model.*;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Raking_Canopy {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenAttacks) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPermanent creature=(MagicPermanent)data;
			return (creature.getController()!=permanent.getController()&&creature.hasAbility(game,MagicAbility.Flying)) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new Object[]{permanent,creature},
                        this,
                        "Raking Canopy deals 4 damage to "+creature.getName()+".") :
                null;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicDamage damage=new MagicDamage((MagicSource)data[0],(MagicTarget)data[1],4,false);
			game.doAction(new MagicDealDamageAction(damage));
		}
    };
}
