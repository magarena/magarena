package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.mstatic.MagicLayer;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.action.MagicChangeCountersAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenAttacksTrigger;

public class Beastmaster_Ascension {
	public static final MagicStatic S = new MagicStatic(
	        MagicLayer.ModPT, 
		    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL) {
		@Override
		public void getPowerToughness(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
					pt.add(5,5);
		}
		@Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target && source.getCounters(MagicCounterType.Charge) >= 7;
        }
    };
    
    public static final MagicWhenAttacksTrigger T = new MagicWhenAttacksTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
			final MagicPlayer player = permanent.getController();
			return (creature.getController() == player) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicSimpleMayChoice(
                            	player + " may put a quest counter on " + permanent + ".",
                            	MagicSimpleMayChoice.ADD_CHARGE_COUNTER,
                            	1,
                            	MagicSimpleMayChoice.DEFAULT_YES),
                        new Object[]{permanent},
                        this,
                        player + " may$ put a quest counter on " + permanent + ".") :
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				game.doAction(new MagicChangeCountersAction(
						(MagicPermanent)data[0],
						MagicCounterType.Charge,
						1,
						true));
			}
		}
    };
}
