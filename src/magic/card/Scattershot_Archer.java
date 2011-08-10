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

public class Scattershot_Archer {

	public static final MagicPermanentActivation V1647 =new MagicPermanentActivation(			"Scattershot Archer",
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(source,source.getController(),new Object[]{source},this,
				"Scattershot Archer deals 1 damage to each creature with flying.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent permanent=(MagicPermanent)data[0];
			final Collection<MagicTarget> targets=game.filterTargets(permanent.getController(),MagicTargetFilter.TARGET_CREATURE_WITH_FLYING);
			for (final MagicTarget target : targets) {
				final MagicDamage damage=new MagicDamage(permanent,target,1,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
	
}
