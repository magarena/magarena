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

public class Fallen_Angel {

	public static final MagicPermanentActivation V804 =new MagicPermanentActivation(			"Fallen Angel",
            new MagicCondition[]{MagicCondition.TWO_CREATURES_CONDITION},
            new MagicActivationHints(MagicTiming.Pump),
            "Pump"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificePermanentEvent(source,source.getController(),MagicTargetChoice.SACRIFICE_CREATURE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(source,source.getController(),new Object[]{source},this,"Fallen Angel gets +2/+1 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],2,1));
		}
	};
	
}
