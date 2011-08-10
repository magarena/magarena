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

public class Raging_Ravine {

	public static final MagicPermanentActivation V3050 =new MagicPermanentActivation(			"Raging Ravine",new MagicCondition[]{new MagicArtificialCondition(
					MagicManaCost.TWO_RED_GREEN.getCondition(),
                    MagicManaCost.ONE_RED_RED_GREEN_GREEN.getCondition())},
			new MagicActivationHints(MagicTiming.Animate)) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.TWO_RED_GREEN)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(source,source.getController(),new Object[]{source},this,
				"Until end of turn, Raging Ravine becomes a 3/3 red and green Elemental creature with \"" +
				"Whenever this creature attacks, put a +1/+1 counter on it.\" It's still a land.");
		}

		@Override
        public void executeEvent(final MagicGame game,final MagicEvent
                event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent permanent=(MagicPermanent)data[0];
			game.doAction(new MagicBecomesCreatureAction(permanent,
    new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.power=3;
			pt.toughness=3;
		}
		@Override
		public EnumSet<MagicSubType> getSubTypeFlags(final MagicPermanent permanent,final EnumSet<MagicSubType> flags) {
            final EnumSet<MagicSubType> mod = flags.clone();
            mod.add(MagicSubType.Elemental);
			return mod;
		}
        @Override
		public int getTypeFlags(final MagicPermanent permanent,final int flags) {
			return flags|MagicType.Creature.getMask();
		}
		@Override
		public int getColorFlags(final MagicPermanent permanent,final int flags) {
			return MagicColor.Red.getMask()|MagicColor.Green.getMask();
		}		
	}));
			game.doAction(new MagicAddTurnTriggerAction(permanent,
    new MagicTrigger(MagicTriggerType.WhenAttacks,"Raging Ravine") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			if (permanent==data&&permanent.isCreature()) {
				return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,"Put a +1/+1 counter on Raging Ravine.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.PlusOne,1,true));
		}
    }));
		}
	};


    public static final MagicTrigger V9980 =new MagicTappedIntoPlayTrigger("Raging Ravine");

}
