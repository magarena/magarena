package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.action.MagicSacrificeAction;
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
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer eotPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Return SN to its owner's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicRemoveFromPlayAction(
                event.getPermanent(),
                MagicLocationType.OwnersHand
            ));
        }
    };
    
    public static final MagicAtEndOfTurnTrigger SacrificeAtEnd = new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicSacrificeAction(event.getPermanent()));
        }
    };
}
