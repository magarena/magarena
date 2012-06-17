package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.action.MagicChangeExtraTurnsAction;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.trigger.MagicAtEndOfTurnTrigger;

public class Lighthouse_Chronologist {
	public static final MagicStatic S = new MagicStatic(MagicLayer.SetPT) {
		@Override
		public void modPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			final int charges = permanent.getCounters(MagicCounterType.Charge);
			if (charges >= 7) {
				pt.set(3,5);
			} else if (charges >= 4) {
				pt.set(2,4);
			}
		}		
	};
	
	public static final MagicAtEndOfTurnTrigger T = new MagicAtEndOfTurnTrigger() {
    	@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
    		final MagicPlayer player = permanent.getController();
    		return (permanent.getCounters(MagicCounterType.Charge) >= 7 &&
    				player != data) ?
    				new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player},
                        this,
                        player + " takes an extra turn after this one.") :
                    MagicEvent.NONE;
		}	
    	@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
    		game.doAction(new MagicChangeExtraTurnsAction((MagicPlayer)data[0],1));
		}
	};
}
