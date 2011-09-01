package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Liliana_s_Caress {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenDiscarded) {
    	@Override
    	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCard data) {
    		final MagicPlayer otherController = data.getOwner();
    		final MagicPlayer player = permanent.getController();
    		return (otherController != player) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{otherController},
                        this,
                        otherController + " loses 2 life."):
                null;
    	}
    	@Override
    	public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
    		game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],-2));
    	}
    };
}
