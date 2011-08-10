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

public class Mind_Stone {

	public static final MagicPermanentActivation V2663 =new MagicPermanentActivation(            "Mind Stone",
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicManaCost.TWO.getCondition()},
            new MagicActivationHints(MagicTiming.Draw),
            "Draw") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return new MagicEvent[]{
				new MagicTapEvent(permanent),
				new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE),
				new MagicSacrificeEvent(permanent)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(source,player,new Object[]{player},this,"Draw a card.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicDrawAction((MagicPlayer)data[0],1));
		}
	};
	
}
