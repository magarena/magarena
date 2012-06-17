package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.MagicSource;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicRegenerateAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicRegenerateTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;

public class Mad_Auntie {
    public static final MagicStatic S1 = new MagicStatic(MagicLayer.ModPT,
	    MagicTargetFilter.TARGET_GOBLIN_YOU_CONTROL) {
	@Override
	public void modPowerToughness(final MagicGame game,
		final MagicPermanent permanent, final MagicPowerToughness pt) {
	    pt.add(1, 1);
	}

	@Override
	public boolean condition(final MagicGame game,
		final MagicPermanent source, final MagicPermanent target) {
	    return source != target;
	}
    };

    public static final MagicPermanentActivation A1 = new MagicPermanentActivation(
	    new MagicCondition[] { MagicCondition.CAN_TAP_CONDITION, MagicCondition.CAN_REGENERATE_CONDITION },
	    new MagicActivationHints(MagicTiming.Pump, false), "Regen") {

	@Override
	public MagicEvent[] getCostEvent(final MagicSource source) {
	    return new MagicEvent[] { new MagicTapEvent((MagicPermanent) source) };
	}

	@Override
	public MagicEvent getPermanentEvent(final MagicPermanent source,
		final MagicPayedCost payedCost) {
	    final MagicTargetFilter targetFilter = new MagicTargetFilter.MagicOtherPermanentTargetFilter(
		    MagicTargetFilter.TARGET_GOBLIN_CREATURE, source);
	    final MagicTargetChoice targetChoice = new MagicTargetChoice(
		    targetFilter, true, MagicTargetHint.Positive,
		    "another target Goblin");
	    return new MagicEvent(source, source.getController(), targetChoice,
		    MagicRegenerateTargetPicker.getInstance(),
		    MagicEvent.NO_DATA, this,
		    "Regenerate another target Goblin$.");
	}

	@Override
	public void executeEvent(final MagicGame game, final MagicEvent event,
		final Object[] data, final Object[] choiceResults) {
	    event.processTargetPermanent(game, choiceResults, 0,
		    new MagicPermanentAction() {
			public void doAction(final MagicPermanent creature) {
			    game.doAction(new MagicRegenerateAction(creature));
			}
		    });
	}
    };
}
