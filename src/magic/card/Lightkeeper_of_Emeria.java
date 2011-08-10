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

public class Lightkeeper_of_Emeria {

	public static final MagicSpellCardEvent V6347 =new MagicSpellCardEvent("Lightkeeper of Emeria") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new MagicKickerChoice(null,MagicManaCost.WHITE,true),
				new Object[]{cardOnStack,player},this,
				"$Play Lightkeeper of Emeria. When Lightkeeper of Emeria enters the battlefield, you gain 2 life for each time it was kicked$.");
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
				final int life=kickerCount*2;
				final MagicEvent triggerEvent=new MagicEvent(permanent,player,new Object[]{player,life},
	new MagicEventAction() {
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],(Integer)data[1]));
		}
	},
                        
                        "You gain "+life+" life.");
				game.doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(permanent,triggerEvent)));
			}
		}
	};
		
}
