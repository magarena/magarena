package magic.card;

import java.util.Collection;

import magic.model.MagicAbility;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Talus_Paladin {
    public static final MagicWhenOtherComesIntoPlayTrigger T1 = new MagicWhenOtherComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
			final MagicPlayer player = permanent.getController();
			return (otherPermanent.getController() == player &&
                    otherPermanent.hasSubType(MagicSubType.Ally)) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicSimpleMayChoice(
                        		player + " may have Ally creatures he or " +
        	                    "she controls gain lifelink until end of turn.",
                                MagicSimpleMayChoice.PUMP,
                                1,
                                MagicSimpleMayChoice.DEFAULT_YES),
                        new Object[]{player},
                        this,
                        player + " may$ have Ally creatures he or " +
                		"she controls gain lifelink until end of turn.") :
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final Collection<MagicTarget> targets =
	                    game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_ALLY_YOU_CONTROL);
	    			for (final MagicTarget target : targets) {
	    				final MagicPermanent creature = (MagicPermanent)target;
	    				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.LifeLink));
	    			}
			}			
		}		
    };
    
    public static final MagicWhenOtherComesIntoPlayTrigger T2 = new MagicWhenOtherComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
			final MagicPlayer player = permanent.getController();
			return (otherPermanent.getController() == player &&
                    otherPermanent.hasSubType(MagicSubType.Ally)) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicSimpleMayChoice(
                        		player + " may put a +1/+1 counter on " + permanent + ".",
                                MagicSimpleMayChoice.ADD_PLUSONE_COUNTER,
                                1,
                                MagicSimpleMayChoice.DEFAULT_YES),
                        new Object[]{permanent},
                        this,
                        player + " may$ put a +1/+1 counter on " + permanent + ".") :
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
						MagicCounterType.PlusOne,
						1,
						true));
			}			
		}		
    };
}
