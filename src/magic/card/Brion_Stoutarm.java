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

public class Brion_Stoutarm {

	public static final MagicPermanentActivation V312 =new MagicPermanentActivation(            "Brion Stoutarm",
			new MagicCondition[]{
                MagicCondition.CAN_TAP_CONDITION,MagicManaCost.RED.getCondition(),
                MagicCondition.TWO_CREATURES_CONDITION
            },
			new MagicActivationHints(MagicTiming.Removal),
            "Damage"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {

			final MagicTargetFilter targetFilter=new MagicTargetFilter.MagicOtherPermanentTargetFilter(
					MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,(MagicPermanent)source);
			final MagicTargetChoice targetChoice=new MagicTargetChoice(
					targetFilter,false,MagicTargetHint.None,"a creature other than Brion Stoutarm to sacrifice");
			return new MagicEvent[]{
				new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.RED),
				new MagicSacrificePermanentEvent(source,source.getController(),targetChoice)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {

			final MagicTarget sacrificed=payedCost.getTarget();
			return new MagicEvent(source,source.getController(),MagicTargetChoice.NEG_TARGET_PLAYER,
				new Object[]{source,sacrificed},this,"Brion Stoutarm deals damage equal to the power of "+sacrificed.getName()+" to target player$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				final MagicPermanent sacrificed=(MagicPermanent)data[1];
				final MagicDamage damage=new MagicDamage((MagicPermanent)data[0],player,sacrificed.getPower(game),false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
	
}
