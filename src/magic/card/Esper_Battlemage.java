package magic.card;

import magic.model.*;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicPreventDamageAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.target.MagicWeakenTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.action.MagicPermanentAction;

public class Esper_Battlemage {
	public static final MagicPermanentActivation A =new MagicPermanentActivation(
			new MagicCondition[]{
                MagicCondition.CAN_TAP_CONDITION,
                MagicManaCost.WHITE.getCondition()},
            new MagicActivationHints(MagicTiming.Pump),
            "Prevent") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.WHITE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(
                    source,
                    player,
                    new Object[]{player},
                    this,
                    "Prevent the next 2 damage that would be dealt to you this turn.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicPreventDamageAction((MagicPlayer)data[0],2));
		}
	};

	public static final MagicPermanentActivation A2 = new MagicPermanentActivation(
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicManaCost.BLACK.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "-1/-1") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.BLACK)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    new MagicWeakenTargetPicker(1,1),
				    MagicEvent.NO_DATA,
                    this,
                    "Target creature$ gets -1/-1 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,-1,-1));
                }
			});
		}
	};
}
