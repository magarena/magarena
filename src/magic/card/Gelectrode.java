package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicUntapAction;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicWhenSpellIsPlayedTrigger;

public class Gelectrode {
    public static final MagicWhenSpellIsPlayedTrigger T = new MagicWhenSpellIsPlayedTrigger() {
		@Override
		public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicCardOnStack cardOnStack) {
			final MagicPlayer player=permanent.getController();
			return (cardOnStack.getController()==player&&cardOnStack.getCardDefinition().isSpell()) ?
                new MagicEvent(
                    permanent,
                    player,
                    new Object[]{permanent},
                    this,
                    "Untap " + permanent + "."):
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
