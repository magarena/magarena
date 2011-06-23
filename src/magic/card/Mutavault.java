package magic.card;

import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.condition.*;
import magic.model.variable.*;
import magic.model.*;

public class Mutavault {
    public static final MagicManaActivation TAP = new MagicTapManaActivation(new MagicManaType[]{MagicManaType.Colorless},0);

	public static final MagicPermanentActivation RAGING_RAVINE=new MagicPermanentActivation(
			new MagicCondition[]{new MagicArtificialCondition(
			MagicManaCost.ONE.getCondition(),
            MagicManaCost.TWO.getCondition())},
			new MagicActivationHints(MagicTiming.Animate),
            "Animate") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(source,source.getController(),new Object[]{source},this,
                    "Mutavault becomes a 2/2 creature with all creature types until end of turn. It's still a land.");
		}

		@Override
        public void executeEvent(final MagicGame game,final MagicEvent
                event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent permanent=(MagicPermanent)data[0];
			game.doAction(new MagicBecomesCreatureAction(permanent,VAR));
		}
	};
    
    private static final MagicLocalVariable VAR = new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.power=2;
			pt.toughness=2;
		}
        @Override
		public int getTypeFlags(final MagicPermanent permanent,final int flags) {
			return flags|MagicType.Creature.getMask();
		}
        @Override
        public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
            return flags|MagicAbility.Changeling.getMask();
        }
	};

    public static final MagicChangeCardDefinition SET = new MagicChangeCardDefinition() {
        @Override
        public void change(MagicCardDefinition cdef) {
		    cdef.setExcludeManaOrCombat();
        }
    };
}
