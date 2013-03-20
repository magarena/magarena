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
import magic.model.condition.MagicCondition;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Concussive_Bolt {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.NEG_TARGET_PLAYER,
                    new MagicDamageTargetPicker(4),
                    this,
                    "SN deals 4 damage to target player$.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetPlayer(game,choiceResults,new MagicPlayerAction() {
                public void doAction(final MagicPlayer targetPlayer) {
                    final MagicDamage damage = new MagicDamage(event.getSource(),targetPlayer,4);
                    game.doAction(new MagicDealDamageAction(damage));
                    if (MagicCondition.METALCRAFT_CONDITION.accept(event.getSource())) {
                        final Collection<MagicPermanent> targets =
                                game.filterPermanents(targetPlayer,MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
                        for (final MagicPermanent target : targets) {
                            game.doAction(new MagicSetAbilityAction(
                                    target,
                                    MagicAbility.CannotBlock));
                        }
                    }
                }
            });
        }
    };
}
