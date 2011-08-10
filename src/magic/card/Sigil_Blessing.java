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

public class Sigil_Blessing {

	public static final MagicSpellCardEvent V4525 =new MagicSpellCardEvent("Sigil Blessing") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.TARGET_CREATURE_YOU_CONTROL,MagicPumpTargetPicker.getInstance(),
				new Object[]{cardOnStack,player},this,"Until end of turn, target creature$ you control gets +3/+3 and other creatures you control get +1/+1.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[1],MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				
				final MagicPermanent permanent=(MagicPermanent)target;
				if (permanent==creature) {
					game.doAction(new MagicChangeTurnPTAction(permanent,3,3));
				} else {
					game.doAction(new MagicChangeTurnPTAction(permanent,1,1));
				}
			}
		}
	};
	
}
