package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicWhenPutIntoGraveyardTrigger;

public class Soulcage_Fiend {
    public static final MagicWhenPutIntoGraveyardTrigger T = new MagicWhenPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicGraveyardTriggerData triggerData) {
            return (triggerData.fromLocation == MagicLocationType.Play) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        this,
                        "Each player loses 3 life."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            for (final MagicPlayer player : game.getPlayers()) {
                game.doAction(new MagicChangeLifeAction(player,-3));
            }
        }
    };
}
