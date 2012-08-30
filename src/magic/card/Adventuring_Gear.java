package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicLandfallTrigger;

public class Adventuring_Gear {
    public static final MagicLandfallTrigger T = new MagicLandfallTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            final MagicPermanent equippedCreature = permanent.getEquippedCreature();
            return equippedCreature.isValid() ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new Object[]{equippedCreature},
                        this,
                        "Equipped creature gets +2/+2 until end of turn.") :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],2,2));
        }    
    };
}
