package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Sphinx_of_Lost_Truths {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                permanent,
                this,
                permanent.isKicked() ? 
                    "PN draws three cards." :
                    "PN draws three cards. Then discards three cards."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPlayer player=event.getPlayer();
            game.doAction(new MagicDrawAction(player,3));
            if (event.getPermanent().isKicked()) {
                game.addEvent(new MagicDiscardEvent(event.getPermanent(),player,3,false));
            }
        }        
    };
}
