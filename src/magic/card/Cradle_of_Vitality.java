package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicPumpTargetPicker;
import magic.model.trigger.MagicWhenLifeIsGainedTrigger;

public class Cradle_of_Vitality {
    public static final MagicWhenLifeIsGainedTrigger T = new MagicWhenLifeIsGainedTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object[] data) {
			final MagicPlayer player = permanent.getController();
			final int amount = (Integer)data[1];
			return (player == (MagicPlayer)data[0]) ?
				new MagicEvent(
                    permanent,
                    player,
                    new MagicMayChoice(
                            "You may pay {1}{W}.",
                            new MagicPayManaCostChoice(MagicManaCost.ONE_WHITE),
                            MagicTargetChoice.POS_TARGET_CREATURE),
                            MagicPumpTargetPicker.create(),
                    new Object[]{amount},
                    this,
                    "You may$ pay {1}{W}$. If you do, put a +1/+1 counter " +
                        "on target creature$ for each 1 life you gained."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				event.processTargetPermanent(game,choiceResults,2,new MagicPermanentAction() {
	                public void doAction(final MagicPermanent creature) {
	                	game.doAction(new MagicChangeCountersAction(
	        					creature,
	        					MagicCounterType.PlusOne,
	        					(Integer)data[0],
	        					true));
                    }
                });
            }
		}
    };
}
