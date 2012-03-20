package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicWhenOtherSpellIsCastTrigger;


public class Taurean_Mauler {
    public static final MagicWhenOtherSpellIsCastTrigger T = new MagicWhenOtherSpellIsCastTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
			final MagicPlayer player = permanent.getController();
			return (cardOnStack.getController() != player) ?
				new MagicEvent(
                    permanent,
                    player,
                    new MagicSimpleMayChoice(
                            player + " may put a +1/+1 counter on " + permanent + ".",
                            MagicSimpleMayChoice.ADD_PLUSONE_COUNTER,
                            1,
                            MagicSimpleMayChoice.DEFAULT_YES),
                    new Object[]{permanent},
                    this,
                    player + " may put a +1/+1 counter on " + permanent + "."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.PlusOne,1,true));
			}
		}
    };
}
