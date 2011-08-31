package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Vorosh__the_Hunter {

    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenDamageIsDealt) {
		@Override
		public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final Object data) {
			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(
                        permanent,
                        player,
                        new MagicMayChoice(
                            "You may pay {2}{G}.",
                            new MagicPayManaCostChoice(MagicManaCost.TWO_GREEN)),
                        new Object[]{permanent},
                        this,
                        "You may$ pay {2}{G}$. If you do, put six +1/+1 counters on " + permanent + ".");
			}
			return null;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				game.doAction(new MagicChangeCountersAction(
                            (MagicPermanent)data[0],
                            MagicCounterType.PlusOne,
                            6,
                            true));
			}
		}
    };
}
