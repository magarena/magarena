package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicMillLibraryAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

public class Shriekgeist {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicDamage damage) {
            final MagicTarget target = damage.getTarget();
            final MagicPlayer player = permanent.getController();
            return (permanent == damage.getSource() && 
                    target.isPlayer() && 
                    damage.isCombat()) ?
                new MagicEvent(
                        permanent,
                        (MagicPlayer)target,
                        this,
                        target + " puts the top two cards of " +
                        "his or her library into his or her graveyard."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicMillLibraryAction(event.getPlayer(),2));
        }
    };
}
