package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicWhenDiesTrigger;

public class Kokusho__the_Evening_Star {
    public static final MagicWhenDiesTrigger T = new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return new MagicEvent(
                permanent,
                permanent.getController(),
                this,
                "Your opponent loses 5 life. You gain 5 life."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPlayer player=event.getPlayer();
            game.doAction(new MagicChangeLifeAction(player.getOpponent(),-5));
            game.doAction(new MagicChangeLifeAction(player,5));
        }
    };
}
