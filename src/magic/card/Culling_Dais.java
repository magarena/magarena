package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicDrawAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificeEvent;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;

public class Culling_Dais {
	public static final MagicPermanentActivation A1 = new MagicPermanentActivation(
			new MagicCondition[]{
				MagicCondition.CAN_TAP_CONDITION,
                MagicCondition.ONE_CREATURE_CONDITION
            },
            new MagicActivationHints(MagicTiming.Pump),
            "Charge") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
					new MagicTapEvent((MagicPermanent)source),
					new MagicSacrificePermanentEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.SACRIFICE_CREATURE)};
		}
		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Put a charge counter on " + source + ".");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicChangeCountersAction(
                    (MagicPermanent)data[0],
                    MagicCounterType.Charge,
                    1,
                    true));
		}
	};
	
	public static final MagicPermanentActivation A2 = new MagicPermanentActivation(
            new MagicCondition[]{
                MagicCondition.CHARGE_COUNTER_CONDITION,
                MagicManaCost.ONE.getCondition()
            },
            new MagicActivationHints(MagicTiming.Pump),
            "Draw") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			final MagicPermanent permanent = (MagicPermanent)source;
			return new MagicEvent[]{
				new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE),
				new MagicSacrificeEvent(permanent)};
		}

		@Override
		public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
			final MagicPlayer player = source.getController();
			final int amount = source.getCounters(MagicCounterType.Charge);
			return new MagicEvent(
                source,
                player,
                new Object[]{player,amount},
                this,
                amount > 1 ?
                    player + " draws " + amount + " cards." :
                    player + " draws a card."); 
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicDrawAction(
					(MagicPlayer)data[0],
					(Integer)data[1]));
		}
	};
}
