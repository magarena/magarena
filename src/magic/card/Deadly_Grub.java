package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicWhenPutIntoGraveyardTrigger;

public class Deadly_Grub {
    public static final MagicWhenPutIntoGraveyardTrigger T = new MagicWhenPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicGraveyardTriggerData triggerData) {
            final MagicPlayer player = permanent.getController();
            return (MagicLocationType.Play == triggerData.fromLocation &&
                    permanent.getCounters(MagicCounterType.Charge) == 0) ?
                new MagicEvent(
                    permanent,
                    player,
                    this,
                    player + " puts a 6/1 green Insect creature token " +
                    "with shroud onto the battlefield.") :
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
                    TokenCardDefinitions.get("Insect2")));
        }
    };
}
