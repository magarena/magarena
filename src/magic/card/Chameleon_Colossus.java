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

public class Chameleon_Colossus {

	public static final MagicPermanentActivation V430 =new MagicPermanentActivation(			"Chameleon Colossus",
            new MagicCondition[]{MagicManaCost.TWO_GREEN_GREEN.getCondition()},
            new MagicActivationHints(MagicTiming.Pump),
            "Pump"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {

			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.TWO_GREEN_GREEN)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {

			return new MagicEvent(source,source.getController(),new Object[]{source},this,
				"Chameleon Colossus gets +X/+X until end of turn, where X is its power.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
			final int power=permanent.getPower(game);
			game.doAction(new MagicChangeTurnPTAction(permanent,power,power));
		}
	};

}
