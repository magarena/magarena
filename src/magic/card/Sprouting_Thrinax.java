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

public class Sprouting_Thrinax {
    public static final MagicWhenPutIntoGraveyardTrigger T = new MagicWhenPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicGraveyardTriggerData triggerData) {
            return (triggerData.fromLocation == MagicLocationType.Play) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts three 1/1 green Saproling creature tokens onto the battlefield."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPlayer player=event.getPlayer();
            for (int count=3;count>0;count--) {
                game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.get("Saproling")));
            }
        }
    };
}
