package magic.card;

import java.util.Collection;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerState;
import magic.model.action.MagicChangePlayerStateAction;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicTapAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

public class Clinging_Mists {
    public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(
                final MagicCardOnStack cardOnStack,
                final MagicPayedCost payedCost) {
            final MagicPlayer player = cardOnStack.getController();
            return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    new Object[]{cardOnStack},
                    this,
                    "Prevent all combat damage that would be dealt this turn.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
            game.doAction(new MagicMoveCardAction(cardOnStack));
            final MagicPlayer player = cardOnStack.getController();
            game.doAction(new MagicChangePlayerStateAction(
                    player,
                    MagicPlayerState.PreventAllCombatDamage,
                    true));
            game.doAction(new MagicChangePlayerStateAction(
                    game.getOpponent(player),
                    MagicPlayerState.PreventAllCombatDamage,
                    true));
            if (player.getLife() <= 5) {
                final Collection<MagicTarget> targets =
                        game.filterTargets(player,MagicTargetFilter.TARGET_ATTACKING_CREATURE);
                for (final MagicTarget target : targets) {
                    final MagicPermanent perm = (MagicPermanent)target;
                    game.doAction(new MagicTapAction(perm,true));
                    game.doAction(new MagicChangeStateAction(
                            perm,
                            MagicPermanentState.DoesNotUntapDuringNext,
                            true));
                }
            }
        }
    };
}
