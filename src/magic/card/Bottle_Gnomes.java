package magic.card;

import magic.model.*;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.*;
import magic.model.condition.MagicCondition;

public class Bottle_Gnomes {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
            MagicCondition.NONE,
            new MagicActivationHints(MagicTiming.Removal),
            "Life+3") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}
		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(
                    source,
                    player,
                    new Object[]{player},
                    this,
                    player + " gains 3 life.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],3));
		}
	};
}
