package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.*;
import magic.model.action.MagicPlayTokenAction;
import magic.model.condition.MagicCondition;
import magic.model.event.*;

public class Zombie_Infestation {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
			new MagicCondition[]{
                MagicCondition.HAS_TWO_CARDS_CONDITION},
            new MagicActivationHints(MagicTiming.Token,true),
            "Token") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
				new MagicDiscardEvent(source,source.getController(),2,false)
			};
		}
		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player = source.getController();
			return new MagicEvent(
                    source,
                    player,
                    new Object[]{player},
                    this,
                    "Put a 2/2 black Zombie creature token onto the battlefield.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPlayer player = (MagicPlayer)data[0];
			game.doAction(new MagicPlayTokenAction(
                    player,
                    TokenCardDefinitions.ZOMBIE_TOKEN_CARD));
		}
	};
}
