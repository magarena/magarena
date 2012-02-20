package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public class MagicLeavesGainLifeTrigger extends MagicWhenLeavesPlayTrigger {
	
    private final int life;

    public MagicLeavesGainLifeTrigger(final int amount) {
        life = amount;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent data) {
        final MagicPlayer player = permanent.getController();
        return (permanent == data) ?
            new MagicEvent(
                    permanent,
                    player,
                    new Object[]{player},
                    this,
                    player + 
                    (life > 0 ? " gains " + life : " loses " + -life) + 
                     " life."):
            MagicEvent.NONE;
    }
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object data[],
            final Object[] choiceResults) {
        game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],life));
    }
};
