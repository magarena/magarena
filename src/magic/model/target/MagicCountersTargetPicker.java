package magic.model.target;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicCountersTargetPicker extends MagicTargetPicker<MagicPermanent> {

	private static final MagicCountersTargetPicker INSTANCE = new MagicCountersTargetPicker();
	
	private MagicCountersTargetPicker() {}
	
	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
		final int score=permanent.getCounters(MagicCounterType.PlusOne)*3+
			permanent.getCounters(MagicCounterType.Charge)*3-
			permanent.getCounters(MagicCounterType.MinusOne)*4-	
			permanent.getCounters(MagicCounterType.Feather)-
			permanent.getCounters(MagicCounterType.Gold);
		return permanent.getController()==player?-score:score;
	}
	
	public static MagicCountersTargetPicker getInstance() {
		return INSTANCE;
	}
}
