package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.event.MagicEvent;

public class MagicLeavesGainLifeTrigger extends MagicWhenLeavesPlayTrigger {

    private final int life;

    public MagicLeavesGainLifeTrigger(final int amount) {
        life = amount;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicRemoveFromPlayAction act) {
        return act.isPermanent(permanent) ?
            new MagicEvent(
                permanent,
                this,
                "PN " +
                (life > 0 ? " gains " + life : " loses " + -life) +
                 " life."
            ):
            MagicEvent.NONE;
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new MagicChangeLifeAction(event.getPlayer(),life));
    }
};
