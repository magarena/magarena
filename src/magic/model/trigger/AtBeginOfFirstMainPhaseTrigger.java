package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.ChangeCountersAction;
import magic.model.event.MagicEvent;

public abstract class AtBeginOfFirstMainPhaseTrigger extends MagicTrigger<MagicPlayer> {
    public AtBeginOfFirstMainPhaseTrigger(final int priority) {
        super(priority);
    }

    public AtBeginOfFirstMainPhaseTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.AtBeginOfFirstMainPhase;
    }

    public static final AtBeginOfFirstMainPhaseTrigger YourAddCounter(final MagicCounterType counterType) {
        return new AtBeginOfFirstMainPhaseTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPlayer player) {
                return permanent.isController(player);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
                game.doAction(new ChangeCountersAction(player, permanent, counterType, 1));
                return MagicEvent.NONE;
            }
        };
    }

}
