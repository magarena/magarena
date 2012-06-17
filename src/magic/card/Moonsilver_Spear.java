package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenAttacksTrigger;


public class Moonsilver_Spear {
    public static final MagicWhenAttacksTrigger T = new MagicWhenAttacksTrigger(1) {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent creature) {
            final MagicPermanent equippedCreature = permanent.getEquippedCreature();
            return (equippedCreature.isValid() &&
                    equippedCreature == creature) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(), 
                        MagicEvent.NO_DATA,
                        this,
                        permanent.getController() + " puts a 4/4 white Angel " +
                        "creature token with flying onto the battlefield."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicPlayTokenAction(
                    event.getPlayer(),
                    TokenCardDefinitions.get("Angel4")));
        }
    };
}
