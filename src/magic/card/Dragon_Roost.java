package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.*;
import magic.model.action.MagicPlayTokenAction;
import magic.model.condition.MagicCondition;
import magic.model.event.*;

public class Dragon_Roost {

	public static final MagicPermanentActivation V2322 =new MagicPermanentActivation(			"Dragon Roost",
            new MagicCondition[]{MagicManaCost.FIVE_RED_RED.getCondition()},
            new MagicActivationHints(MagicTiming.Token),
            "Token") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.FIVE_RED_RED)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(
                    source,
                    player,
                    new Object[]{player},
                    this,
                    "Put a 5/5 red Dragon creature token with flying onto the battlefield.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {

			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.DRAGON5_TOKEN_CARD));
		}
	};
	
}
