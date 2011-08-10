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

public class Inspirit {

	public static final MagicSpellCardEvent V3988 =new MagicSpellCardEvent("Inspirit") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance(),
				new Object[]{cardOnStack},this,"Untap target creature$. It gets +2/+4 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {			
				game.doAction(new MagicUntapAction(creature));
				game.doAction(new MagicChangeTurnPTAction(creature,2,4));
			}
		}
	};

}
