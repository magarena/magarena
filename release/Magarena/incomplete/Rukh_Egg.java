package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenDiesTrigger;

public class Rukh_Egg {
    public static final Object T = new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return new MagicEvent(
                    permanent,
                    this,
                    "PN puts a 4/4 red Bird creature creature token with flying onto the battlefield at the beginning of the next end step.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            //insert trigger to act at the beginning of the next end step
            game.doAction(new MagicPlayTokenAction(event.getPlayer(), TokenCardDefinitions.get("Rukh")));
        }
    };
}
