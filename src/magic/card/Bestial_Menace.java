package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Bestial_Menace {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final MagicPlayer player=cardOnStack.getController();
            return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    new Object[]{cardOnStack,player},
                    this,
                    "Put a 1/1 green Snake creature token, " + 
                        "a 2/2 green Wolf creature token and " + 
                        "a 3/3 green Elephant creature token onto the battlefield.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
            final MagicPlayer player=(MagicPlayer)data[1];
            game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.get("Snake")));
            game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.get("Wolf")));
            game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.get("Elephant")));
        }
    };
}
