package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherPutIntoGraveyardFromPlayTrigger;

public class Kresh_the_Bloodbraided {
    public static final MagicWhenOtherPutIntoGraveyardFromPlayTrigger T = new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
			final MagicPlayer player = permanent.getController();
			final int power = otherPermanent.getPower(game);
			return (permanent != otherPermanent &&
					otherPermanent.isCreature()) ?
				new MagicEvent(
                    permanent,
                    player,
                    new MagicSimpleMayChoice(
                            player + " may put " + power + " +1/+1 counters on " + permanent + ".",
                            MagicSimpleMayChoice.ADD_PLUSONE_COUNTER,
                            1,
                            MagicSimpleMayChoice.DEFAULT_YES),
                    new Object[]{permanent,power},
                    this,
                    player + " may put " + power + " +1/+1 counters on " + permanent + ".") :
                MagicEvent.NONE;
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
						(Integer)data[1],
						true));
			}
		}
    };
}
