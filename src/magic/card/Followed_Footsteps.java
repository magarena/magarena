package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtUpkeepTrigger;


public class Followed_Footsteps {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final MagicPermanent enchanted = permanent.getEnchantedCreature();
            return permanent.isController(upkeepPlayer) && enchanted.isValid() ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a token that's a copy of enchanted creature onto the battlefield."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicPermanent permanent=event.getPermanent();
            final MagicPermanent enchanted=permanent.getEnchantedCreature();
            if (enchanted.isValid()) {
                game.doAction(new MagicPlayTokenAction(event.getPlayer(),enchanted.getCardDefinition()));
            }
        }        
    };
}
