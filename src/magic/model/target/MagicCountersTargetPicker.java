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
        int calculation = 0;
        int value = 0;
        for (final MagicCounterType counterType : MagicCounterType.values()) {
        	if (counterType.getScore() == 0) {
        		value=1;
        	} else {
        		value=counterType.getScore();
        	}
        	calculation += (permanent.getCounters(counterType)*value);
        }
    	
    	final int score=calculation;
        return permanent.getController()==player?-score:score;
    }

    public static MagicCountersTargetPicker create() {
        return INSTANCE;
    }
}
