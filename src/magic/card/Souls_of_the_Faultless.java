package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

public class Souls_of_the_Faultless {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer attackingPlayer = damage.getSource().getController();
            final int pIndex = attackingPlayer.getIndex();
            final int amount = damage.getDealtAmount();
            return (damage.getTarget() == permanent && 
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    new MagicEventAction() {
                        @Override
                        public void executeEvent(
                                final MagicGame game,
                                final MagicEvent event,
                                final Object[] data,
                                final Object[] choiceResults) {
                            game.doAction(new MagicChangeLifeAction(event.getPlayer(),amount));
                            game.doAction(new MagicChangeLifeAction(game.getPlayer(pIndex),-amount));
                        }
                    },
                    "PN gains " + amount + " life and " +  
                    attackingPlayer + " loses " + amount + " life.") :
                MagicEvent.NONE;
        }
    };
}
