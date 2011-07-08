package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicRegenerateAction;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicSingleActivationCondition;

public class MagicRegenerationActivation extends MagicPermanentActivation {

	private static final MagicActivationHints ACTIVATION_HINTS = new MagicActivationHints(MagicTiming.Pump);
	private final MagicManaCost cost;
	
	public MagicRegenerationActivation(final String name,final MagicManaCost cost) {
		super(
            name,
            new MagicCondition[]{
                MagicCondition.CAN_REGENERATE_CONDITION,
                cost.getCondition(),
                new MagicSingleActivationCondition(),
            },
            ACTIVATION_HINTS, 
            "Regen");
		this.cost=cost;
	}
	
	@Override
	public MagicEvent[] getCostEvent(final MagicSource source) {
		return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),cost)};
	}

	@Override
	public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
		return new MagicEvent(source,source.getController(),new Object[]{source},this,"Regenerate "+source.getName()+".");
	}

	@Override
	public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choices) {
		game.doAction(new MagicRegenerateAction((MagicPermanent)data[0]));
	}
}
