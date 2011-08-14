package magic.model.event;

import magic.model.*;
import magic.model.action.MagicSetAbilityAction;
import magic.model.condition.MagicCondition;

public class MagicGainActivation extends MagicPermanentActivation {
		
	private final MagicManaCost cost;
	private final MagicAbility ability;
	
	public MagicGainActivation(final MagicManaCost cost,final MagicAbility ability,final MagicActivationHints hints) {
		super(new MagicCondition[]{cost.getCondition()},hints,"Gain");
		this.cost=cost;
		this.ability=ability;
	}
	
	@Override
	public MagicEvent[] getCostEvent(final MagicSource source) {
		return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),cost)};
	}

	@Override
	public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
		return new MagicEvent(
                source,
                source.getController(),
                new Object[]{source},
                this,
                source + " gains "+ability+" until end of turn.");
	}

	@Override
	public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choices) {
		game.doAction(new MagicSetAbilityAction((MagicPermanent)data[0],ability));
	}
}
