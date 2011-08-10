package magic.card;
import java.util.*;
import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.condition.*;
import magic.model.*;
import magic.data.*;
import magic.model.variable.*;

public class Goblin_Bushwhacker {

	public static final MagicSpellCardEvent V6227 =new MagicSpellCardEvent("Goblin Bushwhacker") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),
				new MagicKickerChoice(null,MagicManaCost.RED,false),new Object[]{cardOnStack},this,
				"$Play Goblin Bushwhacker. When Goblin Bushwhacker enters the battlefield, "+
				"if it was kicked$, creatures you control get +1/+0 and gain haste until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final int kickerCount=(Integer)choiceResults[1];
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			final MagicPlayCardFromStackAction action=new MagicPlayCardFromStackAction(cardOnStack,null);
			game.doAction(action);
			if (kickerCount>0) {
				final MagicPermanent permanent=action.getPermanent();
				final MagicPlayer player=permanent.getController();
				final MagicEvent triggerEvent=new MagicEvent(permanent,player,new Object[]{player},
        new MagicEventAction() {
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			
			final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				
				final MagicPermanent creature=(MagicPermanent)target;
				game.doAction(new MagicChangeTurnPTAction(creature,1,0));
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Haste));
			}			
		}
	}, 
					"Creatures you control get +1/+0 and gain haste until end of turn.");
				game.doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(permanent,triggerEvent)));
			}
		}
	};
	
	
}
