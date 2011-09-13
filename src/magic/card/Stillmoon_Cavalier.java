package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicSetAbilityAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPumpActivation;
import magic.model.event.MagicTiming;

public class Stillmoon_Cavalier {
	// gains flying until end of turn
	public static final MagicPermanentActivation A1 = new MagicPermanentActivation(
            new MagicCondition[]{MagicManaCost.WHITE_OR_BLACK.getCondition()},
            new MagicActivationHints(MagicTiming.Pump,true),
            "Flying"
            ) {
		
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.WHITE_OR_BLACK)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
	                source,
	                source.getController(),
	                new Object[]{source},
	                this,
	                source + " gains flying until end of turn.");
		}

		@Override
		public void executeEvent(
				final MagicGame game,
				final MagicEvent event,
				final Object[] data,
				final Object[] choiceResults) {
			game.doAction(new MagicSetAbilityAction((MagicPermanent)data[0],MagicAbility.Flying));
		}
	};
	
	// gains first strike until end of turn
	public static final MagicPermanentActivation A2 = new MagicPermanentActivation(
			new MagicCondition[]{MagicManaCost.WHITE_OR_BLACK.getCondition()},
			new MagicActivationHints(MagicTiming.Pump,true),
			"First strike"
			) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.WHITE_OR_BLACK)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
					source,
					source.getController(),
					new Object[]{source},
					this,
					source + " gains first strike until end of turn.");
		}

		@Override
		public void executeEvent(
				final MagicGame game,
				final MagicEvent event,
				final Object[] data,
				final Object[] choiceResults) {
			game.doAction(new MagicSetAbilityAction((MagicPermanent)data[0],MagicAbility.FirstStrike));
		}
	};
	
	// gets +1/+0 until end of turn
	public static final MagicPermanentActivation A3 = new MagicPumpActivation(MagicManaCost.WHITE_OR_BLACK_WHITE_OR_BLACK,1,0);
}
