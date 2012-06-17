package magic.card;

import magic.model.MagicCard;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.action.MagicBecomesCreatureAction;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPlayAbilityEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.event.MagicTiming;
import magic.model.stack.MagicCardOnStack;
import magic.model.mstatic.MagicStatic;
import magic.model.mstatic.MagicLayer;

import java.util.EnumSet;

public class Chimeric_Mass {
        
    private static final MagicStatic PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
		@Override
		public void modPowerToughness(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
			final int charge=permanent.getCounters(MagicCounterType.Charge);
			pt.set(charge,charge);
		}
    };
		
    private static final MagicStatic ST = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
		@Override
		public void modSubTypeFlags(
                final MagicPermanent permanent,
                final EnumSet<MagicSubType> flags) {
            flags.add(MagicSubType.Construct);
		}
        @Override
		public int getTypeFlags(final MagicPermanent permanent,final int flags) {
			return flags|MagicType.Creature.getMask();
		}
	};

	public static final MagicPermanentActivation A1 = new MagicPermanentActivation(
            new MagicCondition[]{MagicManaCost.ONE.getCondition()},
            new MagicActivationHints(MagicTiming.Animate,false,1),
            "Animate") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(
                    source,
                    source.getController(),
                    MagicManaCost.ONE),
                    new MagicPlayAbilityEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Until end of turn, " + source + 
                    " becomes a Construct artifact creature with " + 
                    "\"This creature's power and toughness are each equal to the number of charge counters on it.\"");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicBecomesCreatureAction((MagicPermanent)data[0],PT,ST));
		}
	};

	public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(
                final MagicCardOnStack cardOnStack,
                final MagicPayedCost payedCost) {
			final int charges=payedCost.getX();
            final MagicCard card = cardOnStack.getCard();
			return new MagicEvent(
                    card,
                    cardOnStack.getController(),
                    new Object[]{cardOnStack,charges},
                    this,
                    card + " enters the battlefield with "+charges+" charge counters on it.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			final MagicPlayCardFromStackAction action = new MagicPlayCardFromStackAction(cardOnStack);
			game.doAction(action);
			final MagicPermanent permanent=action.getPermanent();
			game.doAction(new MagicChangeCountersAction(
                        permanent,
                        MagicCounterType.Charge,
                        (Integer)data[1],
                        true));
		}
	};
}
