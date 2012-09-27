package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicPlayerAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Bonfire_of_the_Damned {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(
                final MagicCardOnStack cardOnStack,
                final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.NEG_TARGET_PLAYER,
                    new MagicDamageTargetPicker(amount),
                    this,
                    "SN deals " + amount +
                    " damage to target player$ and each creature he or she controls.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    final int amount = event.getCardOnStack().getX();
                    MagicDamage damage = new MagicDamage(
                            event.getSource(),
                            player,
                            amount,
                            false);
                    game.doAction(new MagicDealDamageAction(damage));
                    final Collection<MagicTarget> targets = game.filterTargets(
                            player,
                            MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
                    for (final MagicTarget target : targets) {
                        damage = new MagicDamage(
                                event.getSource(),
                                target,
                                amount,
                                false);
                        game.doAction(new MagicDealDamageAction(damage));
                    }
                }
            });
        }
    };
}
