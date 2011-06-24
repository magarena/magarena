package magic.model.event;

import java.util.List;

import magic.model.MagicManaType;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.condition.MagicCondition;

public class MagicTapManaActivation extends MagicManaActivation {

	private static final MagicCondition CONDITIONS[]=new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION};

	public MagicTapManaActivation(final List<MagicManaType> manaTypes,final int weight) {
		super(manaTypes,CONDITIONS,weight);
	}
		
	@Override
	public MagicEvent[] getCostEvent(final MagicSource source) {
		return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
	}
}
