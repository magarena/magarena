package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenDiscardedTrigger;

public class Liliana_s_Caress {
    public static final MagicWhenDiscardedTrigger T = new MagicWhenDiscardedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCard card) {
            final MagicPlayer otherController = card.getOwner();
            final MagicPlayer player = permanent.getController();
            return (otherController != player) ?
                new MagicEvent(
                        permanent,
                        otherController,
                        this,
                        otherController + " loses 2 life."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),-2));
        }
    };
}
