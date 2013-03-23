package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.action.MagicPutIntoPlayAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Thatcher_Revolt {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(
                final MagicCardOnStack cardOnStack,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN puts three 1/1 red Human creature " +
                "tokens with haste onto the battlefield. Sacrifice " +
                "those tokens at the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCardDefinition token = TokenCardDefinitions.get("Human2");
            for (int x=3;x>0;x--) {
                final MagicPutIntoPlayAction action = new MagicPlayTokenAction(player, token);
                game.doAction(action);
                game.doAction(new MagicChangeStateAction(
                    action.getPermanent(),
                    MagicPermanentState.SacrificeAtEndOfTurn,true
                ));
            }
        }
    };
}
