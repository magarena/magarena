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

public class Loxodon_Hierarch {

	public static final MagicPermanentActivation V1262 =new MagicPermanentActivation(			"Loxodon Hierarch",
            new MagicCondition[]{MagicManaCost.GREEN_WHITE.getCondition()},
            new MagicActivationHints(MagicTiming.Pump),
            "Regen") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
                new MagicPayManaCostSacrificeEvent(source,source.getController(),
                MagicManaCost.GREEN_WHITE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(source,player,new Object[]{player},this,"Regenerate each creature you control.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				final MagicPermanent permanent=(MagicPermanent)target;
				if (permanent.canRegenerate()) {
					game.doAction(new MagicRegenerateAction(permanent));
				}
			}
		}
	};
	
    public static final MagicTrigger V7944 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Loxodon Hierarch") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"You gain 4 life.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],4));
		}
    };

}
