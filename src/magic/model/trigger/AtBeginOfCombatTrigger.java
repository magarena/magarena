package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class AtBeginOfCombatTrigger extends MagicTrigger<MagicPlayer> {
    public AtBeginOfCombatTrigger(final int priority) {
        super(priority);
    }

    public AtBeginOfCombatTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.AtBeginOfCombat;
    }

    public static final AtBeginOfCombatTrigger create(final MagicSourceEvent sourceEvent) {
        return new AtBeginOfCombatTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer turnPlayer) {
                return sourceEvent.getTriggerEvent(permanent);
            }
        };
    }

    public static final AtBeginOfCombatTrigger createYour(final MagicSourceEvent sourceEvent) {
        return new AtBeginOfCombatTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer turnPlayer) {
                return sourceEvent.getTriggerEvent(permanent);
            }
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPlayer turnPlayer) {
                return permanent.isController(turnPlayer);
            }
        };
    }
}
