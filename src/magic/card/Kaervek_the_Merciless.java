package magic.card;

import magic.model.*;
import magic.model.action.MagicDealDamageAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Kaervek_the_Merciless {

    public static final MagicTrigger V7672 =new MagicTrigger(MagicTriggerType.WhenSpellIsPlayed,"Kaervek the Merciless") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data;
			final MagicPlayer player=permanent.getController();
			if (cardOnStack.getController()!=player) {
				final int damage=cardOnStack.getCardDefinition().getConvertedCost();
				return new MagicEvent(permanent,player,MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(damage),
					new Object[]{permanent,damage},this,"Kaervek the Merciless deals "+damage+" damage to target creature or player$.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],target,(Integer)data[1],false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };

}
