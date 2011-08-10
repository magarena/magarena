package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Murderous_Redcap {

    public static final MagicTrigger V8159 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Murderous Redcap") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final int power=permanent.getPower(game);
			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(power),
				new Object[]{permanent},this,"Murderous Redcap deals damage equal to its power to target creature or player$.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage(permanent,target,permanent.getPower(game),false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };
    
}
