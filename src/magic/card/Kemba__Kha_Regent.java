package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Kemba__Kha_Regent {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a 2/2 white Cat creature token onto the " +
                    "battlefield for each Equipment attached to SN."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            int amount = permanent.getEquipmentPermanents().size();
            for (;amount>0;amount--) {
                game.doAction(new MagicPlayTokenAction(event.getPlayer(),TokenCardDefinitions.get("Cat2")));
            }
        }        
    };
}
