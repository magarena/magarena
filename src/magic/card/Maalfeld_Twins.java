package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicWhenPutIntoGraveyardTrigger;

public class Maalfeld_Twins {
    public static final MagicWhenPutIntoGraveyardTrigger T = new MagicWhenPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicGraveyardTriggerData triggerData) {
            final MagicPlayer player = permanent.getController();
            return (MagicLocationType.Play == triggerData.fromLocation) ?
                new MagicEvent(
                    permanent,
                    player,
                    MagicEvent.NO_DATA,
                    this,
                    player + " puts two 2/2 black Zombie " +
                    "creature tokens onto the battlefield.") :
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicPlayTokenAction(
                    player,
                    TokenCardDefinitions.get("Zombie")));
            game.doAction(new MagicPlayTokenAction(
                    player,
                    TokenCardDefinitions.get("Zombie")));
        }
    };
}
