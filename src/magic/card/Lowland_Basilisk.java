package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.action.MagicChangeStateAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

public class Lowland_Basilisk {
    public static final MagicWhenDamageIsDealtTrigger T3 = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent &&
                    damage.getTarget().isCreature()) ?
                new MagicEvent(
                    permanent,
                    damage.getTarget(),
                    this,
                    "Destroy RN at end of combat."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicChangeStateAction(event.getRefPermanent(),MagicPermanentState.DestroyAtEndOfCombat,true));
        }
    };
}
