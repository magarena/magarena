package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Wolfbriar_Elemental {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPlayer player) {   
            return (permanent.isKicked()) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    permanent.getKicker(),
                    this,
                    "Put " + permanent.getKicker() + " 2/2 green Wolf creature tokens onto the battlefield."):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] choiceResults) {
            final MagicPlayer player=event.getPlayer();
            int count=event.getRefInt();
            for (;count>0;count--) {
                game.doAction(new MagicPlayTokenAction(
                        player,
                        TokenCardDefinitions.
                        get("Wolf")));
            }
        }
    };
}
