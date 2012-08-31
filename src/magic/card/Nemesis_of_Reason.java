package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicMillLibraryAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenAttacksTrigger;


public class Nemesis_of_Reason {
    public static final MagicWhenAttacksTrigger T = new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPlayer player = permanent.getController();
            return (permanent == creature) ?
                new MagicEvent(
                        permanent,
                        player,
                        this,
                        player.getOpponent() + " puts the top ten cards " +
                        "of his or her library into his or her graveyard."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicMillLibraryAction(event.getPlayer().getOpponent(),10));
        }
    };
}
