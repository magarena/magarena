package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicItemOnStack;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenTargetedTrigger;

public class Livewire_Lash {
    public static final MagicWhenTargetedTrigger T = new MagicWhenTargetedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicItemOnStack target) {
        	final MagicPermanent equippedCreature = permanent.getEquippedCreature();
            return (target.containsInChoiceResults(equippedCreature) &&
            		target.isSpell()) ?
                new MagicEvent(
                	equippedCreature,
                    permanent.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(2),
                    new Object[]{equippedCreature},
                    this,
                    equippedCreature + " deals 2 damage to target creature or player$."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
        	final MagicPermanent source = (MagicPermanent)data[0];
        	event.processTarget(game,choiceResults,0,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage=new MagicDamage(source,target,2,false);
                    game.doAction(new MagicDealDamageAction(damage));
                }
			});
        }
    };
}
