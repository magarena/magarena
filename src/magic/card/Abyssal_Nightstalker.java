package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenAttacksUnblockedTrigger;

public class Abyssal_Nightstalker {
    public static final MagicWhenAttacksUnblockedTrigger T = new MagicWhenAttacksUnblockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            if (creature == permanent) {
                final MagicPlayer player = permanent.getController();
                final MagicPlayer opponent = player.getOpponent();
                return new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent,opponent},
                        this,
                        opponent + " discards a card.");
            }
            return MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.addEvent(new MagicDiscardEvent(
                    (MagicPermanent)data[0],
                    (MagicPlayer)data[1],
                    1,
                    false));
        }
    };
}
