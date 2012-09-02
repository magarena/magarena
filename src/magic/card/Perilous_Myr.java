package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicWhenDiesTrigger;

public class Perilous_Myr {
    public static final MagicWhenDiesTrigger T = new MagicWhenDiesTrigger() {

        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return new MagicEvent(
                permanent,
                permanent.getController(),
                MagicTargetChoice.TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(2),
                this,
                permanent + " deals 2 damage to target creature or player$."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
            event.processTarget(game,choiceResults,0,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage=new MagicDamage(event.getSource(),target,2,false);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    };
}
