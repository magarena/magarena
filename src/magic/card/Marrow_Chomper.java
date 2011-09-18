package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.MagicType;
import magic.model.action.MagicAddEventAction;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicSacrificeTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Marrow_Chomper {
	private static void gainLife(final MagicGame game,final MagicPermanent permanent) {
		if (permanent.hasCounters()) {
			game.doAction(
					new MagicChangeLifeAction(
						permanent.getController(),
						permanent.getCounters(
						MagicCounterType.PlusOne)));
		}
	}
	
	public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
			final MagicTargetFilter targetFilter=new MagicTargetFilter.MagicOtherPermanentTargetFilter(
					MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,permanent);
			final MagicTargetChoice targetChoice=new MagicTargetChoice(
					targetFilter,false,MagicTargetHint.None,"a creature other than "+permanent+" to sacrifice");
			final MagicChoice devourChoice=new MagicMayChoice("You may sacrifice a creature to "+permanent+".",targetChoice);
			if (player.getNrOfPermanentsWithType(MagicType.Creature,game)>1) {
					return new MagicEvent(
							permanent,
							player,
							devourChoice,
							MagicSacrificeTargetPicker.getInstance(),
							new Object[]{permanent},
							this,
							"You may$ sacrifice a creature$ to "+permanent+".");
			}
			gainLife(game,permanent);
			return MagicEvent.NONE;
		}

		@Override
		public boolean usesStack() {
			return false;
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent permanent=(MagicPermanent)data[0];
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
					public void doAction(final MagicPermanent creature) {
						game.doAction(new MagicSacrificeAction(creature));
						game.doAction(new MagicChangeCountersAction(permanent,MagicCounterType.PlusOne,2,true));
						final MagicEvent newEvent=executeTrigger(game,permanent,permanent.getController());
						if (newEvent.isValid()) {
							game.doAction(new MagicAddEventAction(newEvent));
						}
					}
				});
			} else {
				gainLife(game,permanent);
			}
		}
	};
}
