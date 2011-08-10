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

public class Inkmoth_Nexus {

    public static final MagicPermanentActivation V3003 =new MagicPermanentActivation(			"Inkmoth Nexus",
            new MagicCondition[]{new MagicArtificialCondition(
					MagicManaCost.ONE.getCondition(),
                    MagicManaCost.ONE.getCondition())},
			new MagicActivationHints(MagicTiming.Animate),
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
                    "Inkmoth Nexus becomes a 1/1 Blinkmoth artifact creature with flying and infect until end of turn." + 
                    " It's still a land.");
		}

		@Override
        public void executeEvent(final MagicGame game,final MagicEvent event,
                final Object[] data,final Object[] choiceResults) {
			final MagicPermanent permanent=(MagicPermanent)data[0];
			game.doAction(new MagicBecomesCreatureAction(permanent,
    new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.power=1;
			pt.toughness=1;
		}
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return flags|MagicAbility.Flying.getMask()|MagicAbility.Infect.getMask();
		}
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
			return flags|MagicType.Artifact.getMask()|MagicType.Creature.getMask();
		}
	}));
		}
	};
	
	
}
