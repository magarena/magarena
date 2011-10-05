package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;

public class MagicComesIntoPlayWithCounterTrigger extends MagicWhenComesIntoPlayTrigger {

	private final MagicCounterType counterType;
	private final String description;
	private final int amount;
	
    public MagicComesIntoPlayWithCounterTrigger(final MagicCounterType counterType,final String description,final int amount) {
    	this.counterType = counterType;
    	this.description = description;
		this.amount = amount;
	}

    @Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
		return new MagicEvent(
                permanent,
                player,
                new Object[]{permanent},
                this,
                amount > 1 ?
                	permanent + " enters the battlefield with " + amount + " " + description + " counters on it." :
                	permanent + " enters the battlefield with a " + description + " counter on it.");
	}
	@Override
	public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object data[],
            final Object[] choiceResults) {
		game.doAction(new MagicChangeCountersAction(
				(MagicPermanent)data[0],
				counterType,
				amount,
				false));
	}
	@Override
	public boolean usesStack() {
		return false;
	}
}

