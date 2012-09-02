package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicWhenDiesTrigger;

public class Black_Cat {
    public static final MagicWhenDiesTrigger T = new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            final MagicPlayer opponent = permanent.getController().getOpponent();
            return new MagicEvent(
                    permanent,
                    opponent,
                    this,
                    opponent + " discards a card at random.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.addEvent(new MagicDiscardEvent(
                    event.getSource(),
                    event.getPlayer(),
                    1,
                    true));
        }
    };
}
