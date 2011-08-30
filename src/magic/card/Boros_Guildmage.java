package magic.card;

import magic.model.*;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.target.MagicFirstStrikeTargetPicker;
import magic.model.target.MagicHasteTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.action.MagicPermanentAction;

public class Boros_Guildmage {
	public static final MagicPermanentActivation A1 = new MagicPermanentActivation(
            new MagicCondition[]{MagicManaCost.ONE_RED.getCondition()},
            new MagicActivationHints(MagicTiming.Pump,true),
            "Haste") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE_RED)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    MagicHasteTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Target creature$ gains haste until end of turn.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Haste));
                }
			});
		}
	};

	public static final MagicPermanentActivation A2 = new MagicPermanentActivation(
            new MagicCondition[]{MagicManaCost.ONE_WHITE.getCondition()},
            new MagicActivationHints(MagicTiming.Block,true),
            "First strike") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE_WHITE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    MagicFirstStrikeTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Target creature$ gains first strike until end of turn.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicSetAbilityAction(creature,MagicAbility.FirstStrike));
                }
			});
		}	
	};
}
