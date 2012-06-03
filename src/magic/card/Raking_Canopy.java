package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenAttacksTrigger;

public class Raking_Canopy {
    public static final MagicWhenAttacksTrigger T = new MagicWhenAttacksTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
			return (creature.getController()!=permanent.getController()&&creature.hasAbility(MagicAbility.Flying)) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new Object[]{permanent,creature},
                        this,
                        permanent + " deals 4 damage to "+creature+".") :
                MagicEvent.NONE;
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
