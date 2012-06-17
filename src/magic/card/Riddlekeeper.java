package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicMillLibraryAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenAttacksTrigger;


//The part of this card that interacts with planeswalkers is ignored
public class Riddlekeeper {
    public static final MagicWhenAttacksTrigger T = new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPlayer controller = creature.getController();
            return (controller != permanent.getController()) ?
                new MagicEvent(
                        permanent,
                        controller,
                        new Object[]{controller},
                        this,
                        controller + " puts the top two cards of his or her library into his or her graveyard."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicMillLibraryAction((MagicPlayer)data[0],2));
        }
    };
}
