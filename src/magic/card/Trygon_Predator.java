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

public class Trygon_Predator {

    public static final MagicTrigger V9224 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Trygon Predator") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				return new MagicEvent(permanent,permanent.getController(),
	new MagicMayChoice(
			"You may destroy target artifact or enchantment.",MagicTargetChoice.TARGET_ARTIFACT_OR_ENCHANTMENT_YOUR_OPPONENT_CONTROLS),
    new MagicDestroyTargetPicker(false),
					MagicEvent.NO_DATA,this,"You may$ destroy target artifact or enchantment$ your opponent controls.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicPermanent permanent=event.getTarget(game,choiceResults,1);
				if (permanent!=null) {
					game.doAction(new MagicDestroyAction(permanent));
				}
			}
		}
    };

}
