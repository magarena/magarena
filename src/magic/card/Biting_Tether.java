package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Biting_Tether {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    permanent.getEnchantedCreature(),
                    this,
                    "PN puts a -1/-1 counter on " +
                    permanent.getEnchantedCreature() + "."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(
                    event.getRefPermanent(),
                    MagicCounterType.MinusOne,1,true));            
        }
    };
}
