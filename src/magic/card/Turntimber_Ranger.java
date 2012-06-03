package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Turntimber_Ranger {
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
			final MagicPlayer player = permanent.getController();
			return (otherPermanent.getController() == player &&
                    otherPermanent.hasSubType(MagicSubType.Ally)) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicSimpleMayChoice(
                                player + " may put a 2/2 green Wolf creature " +
                                "token onto the battlefield. If you do, put a " +
                                "+1/+1 counter on " + permanent + ".",
                                MagicSimpleMayChoice.PLAY_TOKEN,
                                1,
                                MagicSimpleMayChoice.DEFAULT_YES),
    					new MagicDestroyTargetPicker(false),
                        new Object[]{player,permanent},
                        this,
                        player + " may$ put a 2/2 green Wolf creature " +
                        "token onto the battlefield. If you do, put a " +
                        "+1/+1 counter on " + permanent + ".") :
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				game.doAction(new MagicPlayTokenAction(
						(MagicPlayer)data[0],
						TokenCardDefinitions.get("Wolf")));
				game.doAction(new MagicChangeCountersAction(
						(MagicPermanent)data[1],
						MagicCounterType.PlusOne,
						1,
						true));
			}		
		}		
    };
}
