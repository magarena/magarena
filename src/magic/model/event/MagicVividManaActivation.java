package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicManaType;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.condition.MagicCondition;

import java.util.List;

public class MagicVividManaActivation extends MagicManaActivation {

	private static final MagicCondition CONDITIONS[]=new MagicCondition[]{
        MagicCondition.CAN_TAP_CONDITION,
        MagicCondition.CHARGE_COUNTER_CONDITION};
			
	public MagicVividManaActivation(final List<MagicManaType> manaTypes) {
		super(manaTypes,CONDITIONS,2);
	}
		
	@Override
	public MagicEvent[] getCostEvent(final MagicSource source) {
		final MagicPermanent permanent=(MagicPermanent)source;
		return new MagicEvent[]{
            new MagicTapEvent(permanent),
            new MagicRemoveCounterEvent(permanent,MagicCounterType.Charge,1)};
	}	
}
