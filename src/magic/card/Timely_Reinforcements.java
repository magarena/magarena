package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Timely_Reinforcements {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final MagicPlayer player = cardOnStack.getController();
            final MagicCard card = cardOnStack.getCard();
            return new MagicEvent(
                    card,
                    player,
                    new Object[]{cardOnStack},
                    this,
                    "If you have less life than an opponent, you gain 6 life. " +
                    "If you control fewer creatures than an opponent, " +
                    "put three 1/1 white Soldier creature tokens onto the battlefield.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
            game.doAction(new MagicMoveCardAction(cardOnStack));        
            final MagicPlayer player = event.getPlayer();
            if (player.getLife() < player.getOpponent().getLife()) {
                game.doAction(new MagicChangeLifeAction(player,6));
            }    
            if (player.getNrOfPermanentsWithType(MagicType.Creature) < 
                    player.getOpponent().getNrOfPermanentsWithType(MagicType.Creature)) {
                for (int count = 3; count > 0; count--) {
                    game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.get("Soldier")));
                }
            }
        }
    };
}
