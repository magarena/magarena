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

public class Backlash {

	public static final MagicSpellCardEvent V3365 =new MagicSpellCardEvent("Backlash") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),
				MagicTargetChoice.NEG_TARGET_UNTAPPED_CREATURE,new MagicTapTargetPicker(true,false),new Object[]{cardOnStack},this,
				"Tap target untapped creature$. That creature deals damage equal to its power to its controller.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicTapAction(creature,true));
				final MagicDamage damage=new MagicDamage(creature,creature.getController(),creature.getPower(game),false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
	
}
