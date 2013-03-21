package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;


public class Mordant_Dragon {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount=damage.getAmount();
            return (damage.getSource() == permanent && 
                    damage.getTarget().isPlayer() && 
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(MagicTargetChoice.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS),
                    new MagicDamageTargetPicker(amount),
                    amount,
                    this,
                    "PN may$ have SN deal " + amount + " damage to target creature$ your opponent controls."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final MagicDamage damage=new MagicDamage(event.getSource(),creature,event.getRefInt());
                    game.doAction(new MagicDealDamageAction(damage));
                }
                });
            }
        }
    };
}
