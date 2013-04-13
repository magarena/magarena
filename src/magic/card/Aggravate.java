package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicPlayerAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Aggravate {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.NEG_TARGET_PLAYER,
                    this,
                    "SN deals 1 damage to each creature target player$ " +
                    "controls. Each creature dealt damage this way attacks " +
                    "this turn if able.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer targetPlayer) {
                    final Collection<MagicPermanent> targets = game.filterPermanents(
                            targetPlayer,
                            MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
                    for (final MagicPermanent target : targets) {
                        final MagicDamage damage = new MagicDamage(
                            event.getSource(),
                            target,
                            1
                        );
                        game.doAction(new MagicDealDamageAction(damage));
                        if (damage.getDealtAmount() > 0) {
                            game.doAction(new MagicSetAbilityAction(
                                target,
                                MagicAbility.AttacksEachTurnIfAble
                            ));
                        }
                    }
                }
            });
        }
    };
}
