package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicUntapAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Battered_Golem {
	public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
			return (permanent != otherPermanent &&
					otherPermanent.isArtifact(game) &&
					permanent.isTapped()) ?
				new MagicEvent(
					permanent,
					permanent.getController(),
					new Object[]{permanent},
					this,
					"Untap " + permanent + ".") :
				MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
				final MagicGame game,
				final MagicEvent event,
				final Object data[],
				final Object[] choiceResults) {
			game.doAction(new MagicUntapAction((MagicPermanent)data[0]));
		}
    };
}
