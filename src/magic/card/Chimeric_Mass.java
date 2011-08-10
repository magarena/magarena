package magic.card;

import magic.model.*;
import magic.model.action.MagicBecomesCreatureAction;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.stack.MagicCardOnStack;
import magic.model.variable.MagicDummyLocalVariable;

import java.util.EnumSet;

public class Chimeric_Mass {

	public static final MagicPermanentActivation V2612 =new MagicPermanentActivation(            "Chimeric Mass",
            new MagicCondition[]{MagicManaCost.ONE.getCondition()},
            new MagicActivationHints(MagicTiming.Animate,false,1),
            "Animate") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Until end of turn, Chimeric Mass becomes a Construct artifact creature with \"This creature's "+
    				"power and toughness are each equal to the number of charge counters on it.\"");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicBecomesCreatureAction((MagicPermanent)data[0],
        new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			final int charge=permanent.getCounters(MagicCounterType.Charge);
			pt.power=charge;
			pt.toughness=charge;
		}
		
		@Override
		public EnumSet<MagicSubType> getSubTypeFlags(final MagicPermanent permanent,final EnumSet<MagicSubType> flags) {
            final EnumSet<MagicSubType> mod = flags.clone();
            mod.add(MagicSubType.Construct);
			return mod;
		}
		
        @Override
		public int getTypeFlags(final MagicPermanent permanent,final int flags) {
			return flags|MagicType.Creature.getMask();
		}
	}));
		}
	};

	public static final MagicSpellCardEvent V6457 =new MagicSpellCardEvent("Chimeric Mass") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final int charges=payedCost.getX();
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),new Object[]{cardOnStack,charges},
				this,"Chimeric Mass enters the battlefield with "+charges+" charge counters on it.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			final MagicPlayCardFromStackAction action=new MagicPlayCardFromStackAction(cardOnStack,null);
			game.doAction(action);
			final MagicPermanent permanent=action.getPermanent();
			game.doAction(new MagicChangeCountersAction(permanent,MagicCounterType.Charge,(Integer)data[1],true));
		}
	};
	
	// ***** AURA PERMANENTS *****
	
}
