package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.action.MagicChangeStateAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBlocksTrigger;

public class AEther_Membrane {
    public static final MagicWhenBlocksTrigger T = new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
            final MagicPermanent blocked = permanent.getBlockedCreature();
            return (permanent == data &&
                    blocked.isValid()) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new Object[]{blocked},
                    this,
                    "Return " + blocked + " to its owner's hand at end of combat."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPermanent creature = (MagicPermanent)data[0];
            game.doAction(new MagicChangeStateAction(creature,MagicPermanentState.ReturnToHandOfOwnerAtEndOfCombat,true));
        }
    };
}
