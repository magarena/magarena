package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicUntapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.trigger.MagicWhenAttacksTrigger;

public class Grimgrin__Corpse_Born {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
			new MagicCondition[]{
                MagicCondition.TWO_CREATURES_CONDITION
            },
			new MagicActivationHints(MagicTiming.Pump),
            "Pump") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			final MagicTargetFilter targetFilter = new MagicTargetFilter.MagicOtherPermanentTargetFilter(
					MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,(MagicPermanent)source);
			final MagicTargetChoice targetChoice = new MagicTargetChoice(
					targetFilter,false,MagicTargetHint.None,"a creature other than " + source + " to sacrifice");
			return new MagicEvent[]{new MagicSacrificePermanentEvent(
					source,
					source.getController(),
					targetChoice)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
					source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Untap " + source + " and put a +1/+1 counter on it.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			game.doAction(new MagicUntapAction((MagicPermanent)data[0]));
			game.doAction(new MagicChangeCountersAction(
					(MagicPermanent)data[0],
					MagicCounterType.PlusOne,
					1,
					true));
		}
	};
	
	public static final MagicWhenAttacksTrigger T1 = new MagicWhenAttacksTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPlayer player = permanent.getController();
			return (permanent == creature) ?
                new MagicEvent(
                        permanent,
                        player,
                        MagicTargetChoice.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                        new MagicDestroyTargetPicker(false),
                        new Object[]{permanent},
                        this,
                        "Destroy target creature$ your opponent controls, " +
                        "then put a +1/+1 counter on " + permanent + "."):
                MagicEvent.NONE;           
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicDestroyAction(creature));
                }
			});
			game.doAction(new MagicChangeCountersAction(
					(MagicPermanent)data[0],
					MagicCounterType.PlusOne,
					1,
					true));
		}
    };
}
