package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.ChangeCountersAction;
import magic.model.action.DestroyAction;
import magic.model.action.RemoveFromPlayAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.event.MagicEvent;

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
            game.doAction(new RemoveFromPlayAction(
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
            game.doAction(new RemoveFromPlayAction(event.getPermanent(), MagicLocationType.Exile));
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
            game.doAction(new DestroyAction(event.getPermanent()));
        }
    };
    
    public static final MagicAtEndOfCombatTrigger Sacrifice = new MagicAtEndOfCombatTrigger() {
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
    
    public static final MagicAtEndOfCombatTrigger Clockwork = new MagicAtEndOfCombatTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eocPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Remove a +1/+1 counter from SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPermanent().hasCounters(MagicCounterType.PlusOne)) {
                game.doAction(new ChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne,-1));
            }
        }
    };
}
