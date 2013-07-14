package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.action.MagicDestroyAction;

public abstract class MagicAtEndOfCombatTrigger extends MagicTrigger<MagicPlayer> {
    public MagicAtEndOfCombatTrigger(final int priority) {
        super(priority);
    }

    public MagicAtEndOfCombatTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.AtEndOfCombat;
    }
    
    public static final MagicAtEndOfCombatTrigger Return = new MagicAtEndOfCombatTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer eocPlayer) {
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
    
    public static final MagicAtEndOfCombatTrigger Exile = new MagicAtEndOfCombatTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eocPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Exile SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicRemoveFromPlayAction(event.getPermanent(), MagicLocationType.Exile));
        }
    };
    
    public static final MagicAtEndOfCombatTrigger Destroy = new MagicAtEndOfCombatTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eocPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Destroy SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDestroyAction(event.getPermanent()));
        }
    };
}
