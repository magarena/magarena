package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.action.MagicChangeCardDestinationAction;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicBuybackChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;

public class Fanning_the_Flames {
    public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return new MagicEvent(
                    cardOnStack,
                    new MagicBuybackChoice(
                        MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                        MagicManaCost.create("{3}")
                    ),
                    new MagicDamageTargetPicker(amount),
                    this,
                    "SN deals " + amount + " damage to target creature or player$. " + 
                    "If the buyback cost was payed$, return SN to its owner's hand as it resolves.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTarget(game,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage = new MagicDamage(
                        event.getSource(),
                        target,
                        event.getCardOnStack().getX()
                    );
                    game.doAction(new MagicDealDamageAction(damage));
                    if (MagicBuybackChoice.isYesChoice(choiceResults[1])) {
                        game.doAction(new MagicChangeCardDestinationAction(event.getCardOnStack(), MagicLocationType.OwnersHand));
                    } 
                }
            });
        }
    };
}
