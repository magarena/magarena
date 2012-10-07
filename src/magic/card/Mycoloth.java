package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Mycoloth {
    public static final Object T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return (permanent.isController(upkeepPlayer) && 
                    permanent.getCounters(MagicCounterType.PlusOne) > 0) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Put a 1/1 green Saproling creature token onto the battlefield for each +1/+1 counter on SN."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final int amt = event.getPermanent().getCounters(MagicCounterType.PlusOne); 
            game.doAction(amt, new MagicPlayTokenAction(
                event.getPlayer(),
                TokenCardDefinitions.get("Saproling")
            ));
        }
    };
}
