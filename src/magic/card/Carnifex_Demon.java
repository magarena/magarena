package magic.card;
import java.util.*;
import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.condition.*;
import magic.model.*;
import magic.data.*;
import magic.model.variable.*;

public class Carnifex_Demon {

	public static final MagicPermanentActivation V353 =new MagicPermanentActivation(            "Carnifex Demon",
			new MagicCondition[]{
                MagicCondition.MINUS_COUNTER_CONDITION,
                MagicManaCost.BLACK.getCondition()
            },
            new MagicActivationHints(MagicTiming.Removal),
            "-1/-1"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {

			return new MagicEvent[]{
				new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.BLACK),
				new MagicRemoveCounterEvent((MagicPermanent)source,MagicCounterType.MinusOne,1)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {

			return new MagicEvent(source,source.getController(),new Object[]{source},this,"Put a -1/-1 counter on each other creature.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicPermanent creature=(MagicPermanent)data[0];
			final Collection<MagicTarget> targets=game.filterTargets(creature.getController(),MagicTargetFilter.TARGET_CREATURE);
			for (final MagicTarget target : targets) {
			
				if (target!=creature) {
					game.doAction(new MagicChangeCountersAction((MagicPermanent)target,MagicCounterType.MinusOne,1,true));
				}
			}
		}
	};

    public static final MagicTrigger V6961 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Carnifex Demon") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,
					"Carnifex Demon enters the battlefield with two -1/-1 counters on it.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.MinusOne,2,false));
		}

		@Override
		public boolean usesStack() {

			return false;
		}
    };
    
}
