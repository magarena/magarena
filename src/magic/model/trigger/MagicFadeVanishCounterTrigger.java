package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.event.MagicEvent;

public class MagicFadeVanishCounterTrigger extends MagicAtUpkeepTrigger {

	private final String counterType;
	
    public MagicFadeVanishCounterTrigger(final String counterType) {
    	this.counterType = counterType;
	}

    @Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer data) {
    	final MagicPlayer player = permanent.getController();
    	if (player == data) {
    		boolean sacrifice = false;
    		final int amount = permanent.getCounters(MagicCounterType.Charge);
    		if (counterType == "fade") {
    			sacrifice = amount == 0;
    		} else if (amount == 1){
    			sacrifice = true;
    			game.doAction(new MagicChangeCountersAction(permanent,MagicCounterType.Charge,-1,true));
    		}
    		return new MagicEvent(
    				permanent,
    				player,
    				new Object[]{permanent,sacrifice},
    				this,
    				sacrifice ?
    				player + " sacrifices " + permanent + "." :
    				player + " removes a " + counterType + " counter from " + permanent + ".");
    	}
    	return MagicEvent.NONE;
	}
	@Override
	public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object data[],
            final Object[] choiceResults) {
		final boolean sacrifice = (Boolean)data[1];
		if (sacrifice) {
			game.doAction(new MagicSacrificeAction((MagicPermanent)data[0]));
		} else {
			game.doAction(new MagicChangeCountersAction(
					(MagicPermanent)data[0],
					MagicCounterType.Charge,
					-1,
					true));
		}
	}
}

