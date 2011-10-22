package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.trigger.MagicCumulativeUpkeepTrigger;

public class Mwonvuli_Ooze {
	public static final MagicStatic S = new MagicStatic(MagicLayer.SetPT) {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			final int amount = permanent.getCounters(MagicCounterType.Charge) * 2 + 1;
			pt.set(amount,amount);
		}
    };
    
	public static final MagicCumulativeUpkeepTrigger T1 = new MagicCumulativeUpkeepTrigger("{2}");
}
