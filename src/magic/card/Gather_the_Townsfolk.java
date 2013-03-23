package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.action.MagicPlayTokenAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Gather_the_Townsfolk {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(
                final MagicCardOnStack cardOnStack,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    this,
                    "PN puts two 1/1 white Human " +
                    "creature tokens onto the battlefield.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            int amount = MagicCondition.FATEFUL_HOUR.accept(event.getSource()) ? 5 : 2;
            for (;amount>0;amount--) {
                game.doAction(new MagicPlayTokenAction(
                        event.getPlayer(),
                        TokenCardDefinitions.get("Human1")));
            }
        }
    };
}
