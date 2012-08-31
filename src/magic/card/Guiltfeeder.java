package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenAttacksUnblockedTrigger;

public class Guiltfeeder {
    public static final MagicWhenAttacksUnblockedTrigger T = new MagicWhenAttacksUnblockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            if (creature == permanent) {
                final MagicPlayer player = permanent.getController();
                final MagicPlayer opponent = player.getOpponent();
                return new MagicEvent(
                        permanent,
                        player,
                        this,
                        opponent + " loses 1 life for each card in his or her graveyard.");
            }
            return MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPlayer player = event.getPlayer().getOpponent();
            game.doAction(new MagicChangeLifeAction(
                    player,
                    -player.getGraveyard().size()));
        }
    };
}
