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

public class Kindled_Fury {

	public static final MagicSpellCardEvent V4034 =new MagicSpellCardEvent("Kindled Fury") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.POS_TARGET_CREATURE,MagicFirstStrikeTargetPicker.getInstance(),
				new Object[]{cardOnStack},this,"Target creature$ gets +1/+0 and gains first strike until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeTurnPTAction(creature,1,0));
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.FirstStrike));
			}
		}
	};

}
