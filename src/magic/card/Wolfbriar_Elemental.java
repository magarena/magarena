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

public class Wolfbriar_Elemental {

	public static final MagicSpellCardEvent V6406 =new MagicSpellCardEvent("Wolfbriar Elemental") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new MagicKickerChoice(null,MagicManaCost.GREEN,true),
				new Object[]{cardOnStack,player},this,
				"$Play Wolfbriar Elemental. When Wolfbriar Elemental enters the battlefield, "+
				"put a 2/2 green Wolf creature token onto the battlefield for each time it was kicked$.");
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
				final MagicEvent triggerEvent=new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player,kickerCount},
	            		new MagicEventAction() {
		            		@Override
		                    public void executeEvent(
                                final MagicGame game,
                                final MagicEvent event,
                                final Object[] data,
                                final Object[] choiceResults) {
			                        final MagicPlayer player=(MagicPlayer)data[0];
			                        int count=(Integer)data[1];
			                        for (;count>0;count--) {
				        				game.doAction(new MagicPlayTokenAction(
                                                player,
                                                TokenCardDefinitions.
                                                WOLF_TOKEN_CARD));
			                        }
		                        }
	                    },
                        "Put "+kickerCount+" 2/2 green Wolf creature tokens onto the battlefield.");
				game.doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(permanent,triggerEvent)));
			}
		}
	};

	// ***** ENTERS BATTLEFIELD *****
	
}
