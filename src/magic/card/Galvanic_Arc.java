package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicFirstStrikeTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Galvanic_Arc {

	public static final MagicSpellCardEvent V6502 =new MagicPlayAuraEvent("Galvanic Arc",
			MagicTargetChoice.TARGET_CREATURE,MagicFirstStrikeTargetPicker.getInstance());
    public static final MagicTrigger V10571 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Galvanic Arc") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(3),
				new Object[]{permanent},this,"Galvanic Arc deals 3 damage to target creature or player$.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],target,3,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };
    
}
