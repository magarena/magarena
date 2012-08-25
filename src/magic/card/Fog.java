package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerState;
import magic.model.action.MagicChangePlayerStateAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Fog {
    public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final MagicPlayer player = cardOnStack.getController();
            return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    new Object[]{cardOnStack,player},
                    this,
                    "Prevent all combat damage that would be dealt this turn.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicPlayer player = (MagicPlayer)data[1];
            game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
            game.doAction(new MagicChangePlayerStateAction(player,MagicPlayerState.PreventAllCombatDamage,true));
            game.doAction(new MagicChangePlayerStateAction(player.getOpponent(),MagicPlayerState.PreventAllCombatDamage,true));
        }
    };
}
