package magic.model.target;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicCountersTargetPicker extends MagicTargetPicker {

	private static final MagicTargetPicker INSTANCE=new MagicCountersTargetPicker();
	
	private MagicCountersTargetPicker() {
		
	}
	
	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final Object target) {

		final MagicPermanent permanent=(MagicPermanent)target;
		final int score=permanent.getCounters(MagicCounterType.PlusOne)*3+
			permanent.getCounters(MagicCounterType.Charge)*3-
			permanent.getCounters(MagicCounterType.MinusOne)*4-	
			permanent.getCounters(MagicCounterType.Feather);
		return permanent.getController()==player?-score:score;
	}
	
	public static MagicTargetPicker getInstance() {
		
		return INSTANCE;
	}
}