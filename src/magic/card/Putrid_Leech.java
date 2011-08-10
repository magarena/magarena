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

public class Putrid_Leech {

	public static final MagicPermanentActivation V1480 =new MagicPermanentActivation(            "Putrid Leech",
			new MagicCondition[]{MagicCondition.ABILITY_ONCE_CONDITION,MagicCondition.TWO_LIFE_CONDITION},
            new MagicActivationHints(MagicTiming.Pump),
            "Pump") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayLifeEvent(source,source.getController(),2),new MagicPlayAbilityEvent(source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Putrid Leech gets +2/+2 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent permanent=(MagicPermanent)data[0];
			game.doAction(new MagicChangeTurnPTAction(permanent,2,2));
		}
	};
	
}
