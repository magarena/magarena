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

public class Glen_Elendra_Archmage {

	public static final MagicPermanentActivation V1015 =new MagicPermanentActivation(			"Glen Elendra Archmage",
            new MagicCondition[]{MagicManaCost.BLUE.getCondition()},
            new MagicActivationHints(MagicTiming.Counter),
            "Counter") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostSacrificeEvent(source,source.getController(),MagicManaCost.BLUE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(source,source.getController(),MagicTargetChoice.NEG_TARGET_NONCREATURE_SPELL,
				MagicEvent.NO_DATA,this,"Counter target noncreature spell$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicCardOnStack targetSpell=event.getTarget(game,choiceResults,0);
			if (targetSpell!=null) {
				game.doAction(new MagicCounterItemOnStackAction(targetSpell));
			}
		}
	};
	
}
