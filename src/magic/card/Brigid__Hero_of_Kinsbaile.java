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

public class Brigid__Hero_of_Kinsbaile {

	public static final MagicPermanentActivation V278 =new MagicPermanentActivation(			"Brigid, Hero of Kinsbaile",
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Block),
            "Damage"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {

			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {

			return new MagicEvent(source,source.getController(),MagicTargetChoice.NEG_TARGET_PLAYER,
				new Object[]{source},this,"Brigid, Hero of Kinsbaile deals 2 damage to each attacking or blocking creature target player$ controls.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				final MagicSource source=(MagicSource)data[0];
				final Collection<MagicTarget> targets=game.filterTargets(player,MagicTargetFilter.TARGET_ATTACKING_OR_BLOCKING_CREATURE_YOU_CONTROL);
				for (final MagicTarget target : targets) {
					final MagicDamage damage=new MagicDamage(source,target,2,false);
					game.doAction(new MagicDealDamageAction(damage));
				}
			}
		}
	};
	
}
