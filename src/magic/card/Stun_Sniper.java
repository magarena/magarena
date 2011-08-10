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

public class Stun_Sniper {

	public static final MagicPermanentActivation V1926 =new MagicPermanentActivation(            "Stun Sniper",
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicManaCost.ONE.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.ONE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    new MagicTapTargetPicker(true,false),
                    new Object[]{source},
                    this,
                    "Stun Sniper deals 1 damage to target creature$. Tap that creature.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {			
				final MagicDamage damage=new MagicDamage((MagicPermanent)data[0],creature,1,false);
				game.doAction(new MagicDealDamageAction(damage));
				if (!creature.isTapped()) {
					game.doAction(new MagicTapAction(creature,true));
				}
			}
		}
	};
	
}
