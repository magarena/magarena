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

public class Oracle_of_Nectars {

	public static final MagicPermanentActivation V1456 = new MagicPermanentActivation(            "Oracle of Nectars",
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicManaCost.X.getCondition()},
            new MagicActivationHints(MagicTiming.Draw),
            "Life+X") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.X)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final int amount=payedCost.getX();
			final MagicPlayer player=source.getController();
			return new MagicEvent(source,player,new Object[]{player,amount},this,"You gain "+amount+" life.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicChangeLifeAction(player,(Integer)data[1]));
		}
	};
		
}
