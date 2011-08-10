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

public class Kavu_Titan {

	public static final MagicSpellCardEvent V6313 =new MagicSpellCardEvent("Kavu Titan") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new MagicKickerChoice(null,MagicManaCost.TWO_GREEN,false),
				new Object[]{cardOnStack,player},this,
				"$Play Kavu Titan. If Kavu Titan was kicked$, it enters the battlefield with three +1/+1 counters on it and has trample.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final boolean kicked=((Integer)choiceResults[1])>0;
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			final MagicPlayCardFromStackAction action=new MagicPlayCardFromStackAction(cardOnStack,null);
			game.doAction(action);
			final MagicPermanent permanent=action.getPermanent();
			if (kicked) {
				permanent.changeCounters(MagicCounterType.PlusOne,3);
				permanent.addLocalVariable(
	new MagicDummyLocalVariable() {
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return flags|MagicAbility.Trample.getMask();
		}
	}
                );
			}
		}
	};
	
	
}
