package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;


public class Dissipation_Field {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicSource dmgSource=damage.getSource();
            return (permanent.isController(damage.getTarget()) && dmgSource.isPermanent()) ?
                new MagicEvent(
                    permanent,
                    dmgSource,
                    this,
                    "Return "+dmgSource+" to its owner's hand."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicRemoveFromPlayAction(
                event.getRefPermanent(),
                MagicLocationType.OwnersHand
            ));
        }
    };
}
