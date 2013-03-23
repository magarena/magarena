package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicMillLibraryAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;


public class Sword_of_Body_and_Mind {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicTarget targetPlayer=damage.getTarget();
            return (damage.getSource() == permanent.getEquippedCreature() && 
                    damage.getTarget().isPlayer() && 
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    targetPlayer,
                    this,
                    "PN puts a 2/2 green Wolf creature token onto the battlefield and " + 
                    targetPlayer + " puts the top ten cards of his or her library into his or her graveyard."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicPlayTokenAction(event.getPlayer(),TokenCardDefinitions.get("Wolf")));
            game.doAction(new MagicMillLibraryAction(event.getRefPlayer(),10));
        }
    };
}
