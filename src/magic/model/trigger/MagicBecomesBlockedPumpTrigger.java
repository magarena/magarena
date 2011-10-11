package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;

public class MagicBecomesBlockedPumpTrigger extends MagicWhenBecomesBlockedTrigger {

	private final int amountPower;
	private final int amountToughness;
	private final boolean forEachBlocker;
	
    public MagicBecomesBlockedPumpTrigger(final int amountPower,final int amountToughness,final boolean forEachBlocker) {
    	this.amountPower = amountPower;
    	this.amountToughness = amountToughness;
    	this.forEachBlocker = forEachBlocker;
	}

    @Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
		if (permanent == data ) {
			int size = forEachBlocker ?
					permanent.getBlockingCreatures().size() :
					1;
			final int totalAmountPower = amountPower * size;
			final int totalAmountToughness = amountToughness * size;
            return new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new Object[]{permanent},
                    this,
                    permanent + " gets " + getString(totalAmountPower) + 
                    "/" + getString(totalAmountToughness) + " until end of turn.");
		}
		return MagicEvent.NONE;
	}
	@Override
	public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object data[],
            final Object[] choiceResults) {
		game.doAction(new MagicChangeTurnPTAction(
				(MagicPermanent)data[0],
				amountPower,
				amountToughness));
	}
	
	private String getString(final int pt) {
		return pt >= 0 ?
				"+" + pt :
				Integer.toString(pt);
	}
}

