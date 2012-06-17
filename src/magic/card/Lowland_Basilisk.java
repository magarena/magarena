package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanentState;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeStateAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

public class Lowland_Basilisk {
    public static final MagicWhenDamageIsDealtTrigger T3 = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent &&
                    damage.getTarget().isPermanent() &&
                    ((MagicPermanent)damage.getTarget()).isCreature()) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new Object[]{damage.getTarget()},
                        this,
                        "Destroy " + damage.getTarget() + " at end of combat."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicChangeStateAction((MagicPermanent)data[0],MagicPermanentState.DestroyAtEndOfCombat,true));
        }
    };
}
