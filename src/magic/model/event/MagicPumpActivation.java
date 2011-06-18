package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.condition.MagicCondition;

public class MagicPumpActivation extends MagicPermanentActivation {

	private static final MagicActivationHints ACTIVATION_HINTS=new MagicActivationHints(MagicTiming.Pump);
	
	private final MagicManaCost cost;
	private final int power;
	private final int toughness;
	
	public MagicPumpActivation(final String name,final MagicManaCost cost,final int power,final int toughness) {
		super(name,new MagicCondition[]{cost.getCondition()},ACTIVATION_HINTS,"Pump");
		this.cost=cost;
		this.power=power;
		this.toughness=toughness;
	}

	@Override
	public MagicEvent[] getCostEvent(final MagicSource source) {

		return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),cost)};
	}
	
	@Override
	public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {

		return new MagicEvent(source,source.getController(),new Object[]{source},this,
			source.getName()+" gets +"+power+"/+"+toughness+" until end of turn.");
	}

	@Override
	public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choices) {
		
		game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],power,toughness));
	}
}
