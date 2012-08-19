package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.event.MagicEvent;
import magic.model.MagicCounterType;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Mycoloth {
    public static final Object T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
            final MagicPlayer player=permanent.getController();
            return (player == data && permanent.getCounters(MagicCounterType.PlusOne) > 0) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent,player},
                        this,
                        "Put a 1/1 green Saproling creature token onto the battlefield for each +1/+1 counter on " + permanent + "."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPermanent permanent=(MagicPermanent)data[0];
            final MagicPlayer player=(MagicPlayer)data[1];
            if (player.controlsPermanent(permanent)) {
                final int tokens = permanent.getCounters(MagicCounterType.PlusOne); 
                for (int count=tokens;count>0;count--) {
                    game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.get("Saproling")));
                }
            }
        }
    };
}
