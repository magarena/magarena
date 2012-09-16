package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerState;
import magic.model.MagicType;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicChangePlayerStateAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Blunt_the_Assault {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    this,
                    cardOnStack.getController() + " gains 1 life for each creature on the battlefield. " +
                    "Prevent all combat damage that would be dealt this turn.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicPlayer player = event.getPlayer();
            final int amount = game.getNrOfPermanents(MagicType.Creature);
            if (amount > 0) {
                game.doAction(new MagicChangeLifeAction(player,amount));
            }
            game.doAction(new MagicChangePlayerStateAction(player,MagicPlayerState.PreventAllCombatDamage,true));
            game.doAction(new MagicChangePlayerStateAction(player.getOpponent(),MagicPlayerState.PreventAllCombatDamage,true));
        }
    };
}
