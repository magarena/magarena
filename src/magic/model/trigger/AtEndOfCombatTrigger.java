package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.ChangeCountersAction;
import magic.model.action.DestroyAction;
import magic.model.action.RemoveFromPlayAction;
import magic.model.action.SacrificeAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class AtEndOfCombatTrigger extends MagicTrigger<MagicPlayer> {
    public AtEndOfCombatTrigger(final int priority) {
        super(priority);
    }

    public AtEndOfCombatTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.AtEndOfCombat;
    }

    public static final AtEndOfCombatTrigger create(final MagicSourceEvent sourceEvent) {
        return new AtEndOfCombatTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer turnPlayer) {
                return sourceEvent.getTriggerEvent(permanent);
            }
        };
    }

    public static final AtEndOfCombatTrigger Return = new AtEndOfCombatTrigger() {
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

    public static final AtEndOfCombatTrigger Exile = new AtEndOfCombatTrigger() {
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

    public static final AtEndOfCombatTrigger Destroy = new AtEndOfCombatTrigger() {
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

    public static final AtEndOfCombatTrigger Sacrifice = new AtEndOfCombatTrigger() {
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
            game.doAction(new SacrificeAction(event.getPermanent()));
        }
    };

    public static final AtEndOfCombatTrigger Clockwork = new AtEndOfCombatTrigger() {
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
