package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenDiesTrigger;

public class Symbiotic_Elf {
    public static final MagicWhenDiesTrigger T = new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts two 1/1 green Insect creature tokens onto the battlefield."
            );
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(2, new MagicPlayTokenAction(
                event.getPlayer(), 
                TokenCardDefinitions.get("Insect4")
            ));
        }
    };
}
