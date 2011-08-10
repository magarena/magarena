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

public class Colossal_Might {

	public static final MagicSpellCardEvent V3489 =new MagicSpellCardEvent("Colossal Might") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance(),
				new Object[]{cardOnStack},this,"Target creature$ gets +4/+2 and gains trample until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeTurnPTAction(creature,4,2));
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Trample));
			}
		}
	};
	
}
