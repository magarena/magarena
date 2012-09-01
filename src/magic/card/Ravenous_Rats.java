package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;


public class Ravenous_Rats {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            final MagicPlayer opponent = player.getOpponent();
            return new MagicEvent(
                    permanent,
                    player,
                    this,
                    opponent + " discards a card.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.addEvent(
                    new MagicDiscardEvent(
                            event.getSource(),
                            event.getPlayer().getOpponent(),
                            1,
                            false));
        }        
    };
}
