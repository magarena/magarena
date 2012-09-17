package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenAttacksTrigger;


// The part of this card that interacts with planeswalkers is ignored
public class Circle_of_Flame {
    public static final MagicWhenAttacksTrigger T = new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPlayer controller = creature.getController();
            return (controller != permanent.getController() &&
                    !creature.hasAbility(MagicAbility.Flying)) ?
                new MagicEvent(
                        permanent,
                        controller,
                        new Object[]{creature},
                        this,
                        "SN deals 1 damage to attacking creature without flying."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicDamage damage = new MagicDamage(event.getSource(),(MagicTarget)data[0],1,false);
            game.doAction(new MagicDealDamageAction(damage));
        }
    };
}
