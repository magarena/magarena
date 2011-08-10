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

public class Sphinx_of_Magosi {

	public static final MagicPermanentActivation V1799 =new MagicPermanentActivation(			"Sphinx of Magosi",
            new MagicCondition[]{MagicManaCost.TWO_BLUE.getCondition()},
            new MagicActivationHints(MagicTiming.Draw),
            "Draw") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.TWO_BLUE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(
                    source,
                    player,
                    new Object[]{player,source},
                    this,
                    "Draw a card, then put a +1/+1 counter on Magosi Sphinx.");
        }
		
        @Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            game.doAction(new MagicDrawAction((MagicPlayer)data[0],1));
            game.doAction(new MagicChangeCountersAction((MagicPermanent)data[1],MagicCounterType.PlusOne,1,true));
        }
	};
	
}
