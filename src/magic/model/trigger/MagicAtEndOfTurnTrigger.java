package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.event.MagicEvent;

public abstract class MagicAtEndOfTurnTrigger extends MagicTrigger<MagicPlayer> {
    public MagicAtEndOfTurnTrigger(final int priority) {
        super(priority); 
    }
    
    public MagicAtEndOfTurnTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.AtEndOfTurn;
    }
    
    public static final MagicAtEndOfTurnTrigger ReturnAtEnd = new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer eotPlayer) {
            final MagicPlayer player = permanent.getController();
            return (player == eotPlayer) ?
                new MagicEvent(
                    permanent,
                    player,
                    this,
                    "Return SN to its owner's hand."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicRemoveFromPlayAction(
                event.getPermanent(),
                MagicLocationType.OwnersHand
            ));
        }
    };
}
