package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicMillLibraryAction;
import magic.model.action.MagicPlayerAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicWhenPutIntoGraveyardTrigger;

public class Rotcrown_Ghoul {
    public static final MagicWhenPutIntoGraveyardTrigger T = new MagicWhenPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicGraveyardTriggerData triggerData) {
            return (MagicLocationType.Play == triggerData.fromLocation) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        MagicTargetChoice.NEG_TARGET_PLAYER,
                        MagicEvent.NO_DATA,
                        this,
                        "Target player$ puts the top five cards of " +
                        "his or her library into his or her graveyard."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer targetPlayer) {
                    game.doAction(new MagicMillLibraryAction(targetPlayer,5));
                }
            });    
        }
    };
}
