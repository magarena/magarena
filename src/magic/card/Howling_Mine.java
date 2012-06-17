package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Howling_Mine {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
            return (permanent.isUntapped()) ?
                new MagicEvent(
                    permanent,
                    player,
                    new Object[]{player},
                    this,
                    player + " draws a card."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
            final MagicPlayer player = (MagicPlayer)data[0];
            game.doAction(new MagicDrawAction(player,1));
        }
    };
}
