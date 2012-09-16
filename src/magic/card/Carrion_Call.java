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

public class Carrion_Call {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    this,
                    "PN puts two 1/1 green Insect creature tokens with infect onto the battlefield");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicPlayer player=event.getPlayer();
            game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.get("Insect1")));
            game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.get("Insect1")));
        }
    };
}
