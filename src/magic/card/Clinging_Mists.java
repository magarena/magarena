package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerState;
import magic.model.action.MagicChangePlayerStateAction;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicTapAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Clinging_Mists {
    public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(
                final MagicCardOnStack cardOnStack,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Prevent all combat damage that would be dealt this turn."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicChangePlayerStateAction(
                player,
                MagicPlayerState.PreventAllCombatDamage
            ));
            game.doAction(new MagicChangePlayerStateAction(
                player.getOpponent(),
                MagicPlayerState.PreventAllCombatDamage
            ));
            if (MagicCondition.FATEFUL_HOUR.accept(event.getSource())) {
                final Collection<MagicPermanent> targets =
                    game.filterPermanents(player,MagicTargetFilter.TARGET_ATTACKING_CREATURE);
                for (final MagicPermanent perm : targets) {
                    game.doAction(new MagicTapAction(perm,true));
                    game.doAction(new MagicChangeStateAction(
                        perm,
                        MagicPermanentState.DoesNotUntapDuringNext,
                        true
                    ));
                }
            }
        }
    };
}
