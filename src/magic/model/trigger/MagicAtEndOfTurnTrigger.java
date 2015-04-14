package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.RemoveFromPlayAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class MagicAtEndOfTurnTrigger extends MagicTrigger<MagicPlayer> {
    public MagicAtEndOfTurnTrigger(final int priority) {
        super(priority);
    }

    public MagicAtEndOfTurnTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.AtEndOfTurn;
    }
    
    public static final MagicAtEndOfTurnTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicAtEndOfTurnTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
    
    public static final MagicAtEndOfTurnTrigger createYour(final MagicSourceEvent sourceEvent) {
        return new MagicAtEndOfTurnTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
                return sourceEvent.getEvent(permanent);
            }
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPlayer eotPlayer) {
                return permanent.isController(eotPlayer);
            }
        };
    }

    public static final MagicAtEndOfTurnTrigger Sacrifice = new MagicAtEndOfTurnTrigger() {
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
    
    public static final MagicAtEndOfTurnTrigger ExileAtEnd = new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
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
    
    public static final MagicAtEndOfTurnTrigger ExileAtYourEnd(final MagicPlayer your) {
        return new MagicAtEndOfTurnTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
                return your.getId() == eotPlayer.getId() ?
                    new MagicEvent(
                        permanent,
                        this,
                        "Exile SN."
                    ):
                    MagicEvent.NONE;
            }
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new RemoveFromPlayAction(event.getPermanent(), MagicLocationType.Exile));
            }
        };
    };
    
    public static final MagicAtEndOfTurnTrigger Return = new MagicAtEndOfTurnTrigger() {
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
}
