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

public class Sphinx_of_Lost_Truths {

	public static final MagicSpellCardEvent V6385 =new MagicSpellCardEvent("Sphinx of Lost Truths") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new MagicKickerChoice(null,MagicManaCost.ONE_BLUE,false),
				new Object[]{cardOnStack,player},this,
				"$Play Sphinx of Lost Truths. When Sphinx of Lost Truths enters the battlefield, "+
				"draw three cards. Then if it wasn't kicked$, discard three cards.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicPlayCardFromStackAction action=new MagicPlayCardFromStackAction((MagicCardOnStack)data[0],null);
			action.setKicked(((Integer)choiceResults[1])>0);
			game.doAction(action);
		}
	};
	
    public static final MagicTrigger V9003 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Sphinx of Lost Truths") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPlayer player=permanent.getController();
			final boolean kicked=permanent.hasState(MagicPermanentState.Kicked);
			return new MagicEvent(permanent,player,new Object[]{player,permanent,kicked},this,
				kicked?"You draw three cards.":"You draw three cards. Then you discard three cards.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicDrawAction(player,3));
			final boolean kicked=(Boolean)data[2];
			if (!kicked) {
				game.addEvent(new MagicDiscardEvent((MagicPermanent)data[1],player,3,false));
			}
		}		
    };
    
}
