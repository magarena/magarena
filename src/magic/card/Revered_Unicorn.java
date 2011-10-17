package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtUpkeepTrigger;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public class Revered_Unicorn {
    public static final MagicAtUpkeepTrigger T1 = new MagicAtUpkeepTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
			if (permanent.getController() == data) {
				game.doAction(new MagicChangeCountersAction(permanent,MagicCounterType.Charge,1,true));
				final int amount = permanent.getCounters(MagicCounterType.Charge);
                return new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new MagicMayChoice(
                                "You may pay {"+amount+"}.",
                                new MagicPayManaCostChoice(MagicManaCost.createCost("{"+amount+"}"))),
                        new Object[]{permanent},
                        this,
                        "You may$ pay {"+amount+"}. If you don't, sacrifice " + permanent + ".");
			}
			return MagicEvent.NONE;
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isNoChoice(choiceResults[0])) {
				game.doAction(new MagicSacrificeAction((MagicPermanent)data[0]));
			}
		}
    };
    
    public static final MagicWhenLeavesPlayTrigger T2 = new MagicWhenLeavesPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
			if (permanent == data) {
				final MagicPlayer player = permanent.getController();
				final int amount = permanent.getCounters(MagicCounterType.Charge);
				return new MagicEvent(
                    permanent,
                    player,
                    new Object[]{player,amount},
                    this,
                    player + " gains " + amount + " life.");
			}
			return MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangeLifeAction(
					(MagicPlayer)data[0],
					(Integer)data[1]));
		}
    };
}
