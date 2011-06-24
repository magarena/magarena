package magic.model.event;

import java.util.List;

import magic.model.MagicManaType;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.condition.MagicCondition;

public class MagicSacrificeTapManaActivation extends MagicManaActivation {

	private static final MagicCondition CONDITIONS[]=new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION};
			
	public MagicSacrificeTapManaActivation(final List<MagicManaType> manaTypes) {
		super(manaTypes,CONDITIONS,3);
	}

	@Override
	public MagicEvent[] getCostEvent(final MagicSource source) {
		final MagicPermanent permanent=(MagicPermanent)source;
		return new MagicEvent[]{new MagicTapEvent(permanent),new MagicSacrificeEvent(permanent)};
	}	
}
