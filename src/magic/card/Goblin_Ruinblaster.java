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

public class Goblin_Ruinblaster {

	public static final MagicSpellCardEvent V6270 =new MagicSpellCardEvent("Goblin Ruinblaster") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new MagicKickerChoice(null,MagicManaCost.RED,false),
				new Object[]{cardOnStack,player},this,
				"$Play Goblin Ruinblaster. When Goblin Ruinblaster enters the battlefield, if it is was kicked$, "+
				"destroy target nonbasic land.");
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
				final MagicEvent triggerEvent=new MagicEvent(permanent,player,
						MagicTargetChoice.NEG_TARGET_NONBASIC_LAND,new MagicDestroyTargetPicker(false),
						MagicEvent.NO_DATA,
	new MagicEventAction() {
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicPermanent land=event.getTarget(game,choiceResults,0);
			if (land!=null) {
				game.doAction(new MagicDestroyAction(land));
			}
		}
	},
    "Destroy target nonbasic land$.");
				game.doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(permanent,triggerEvent)));
			}
		}
	};
	
	
}
