package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokensAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenAttacksTrigger;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Grave_Titan {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts two 2/2 black Zombie creature tokens onto the battlefield."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicPlayTokensAction(
                event.getPlayer(),
                TokenCardDefinitions.get("Zombie"),
                2
            ));
        }        
    };

    public static final MagicWhenAttacksTrigger T2 = new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent==creature) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts two 2/2 black Zombie creature tokens onto the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicPlayTokensAction(
                event.getPlayer(),
                TokenCardDefinitions.get("Zombie"),
                2
            ));
        }        
    };
}
