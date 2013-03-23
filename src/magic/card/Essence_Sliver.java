package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

public class Essence_Sliver {
        
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount=damage.getDealtAmount();
            final MagicSource source=damage.getSource();
            return source.hasSubType(MagicSubType.Sliver) ?
                new MagicEvent(
                    permanent,
                    source.getController(),
                    amount,
                    this,
                    "PN gains " + amount + " life."
                ):
                MagicEvent.NONE;
        }
    
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
             game.doAction(new MagicChangeLifeAction(event.getPlayer(),event.getRefInt()));
        }
    };
}
