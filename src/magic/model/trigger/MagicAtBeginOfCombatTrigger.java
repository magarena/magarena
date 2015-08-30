package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class MagicAtBeginOfCombatTrigger extends MagicTrigger<MagicPlayer> {
    public MagicAtBeginOfCombatTrigger(final int priority) {
        super(priority);
    }

    public MagicAtBeginOfCombatTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.AtBeginOfCombat;
    }
    
    public static final MagicAtBeginOfCombatTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicAtBeginOfCombatTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer turnPlayer) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
    
    public static final MagicAtBeginOfCombatTrigger createYour(final MagicSourceEvent sourceEvent) {
        return new MagicAtBeginOfCombatTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer turnPlayer) {
                return sourceEvent.getEvent(permanent);
            }
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPlayer turnPlayer) {
                return permanent.isController(turnPlayer);
            }
        };
    }
}
