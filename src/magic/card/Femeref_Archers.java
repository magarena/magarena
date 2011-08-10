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

public class Femeref_Archers {

	public static final MagicPermanentActivation V826 =new MagicPermanentActivation(			"Femeref Archers",
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Attack),
            "Damage"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_ATTACKING_CREATURE_WITH_FLYING,
                    new MagicDamageTargetPicker(4),
                    new Object[]{source},this,"Femeref Archers deals 4 damage to target attacking creature$ with flying.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],creature,4,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
	
}
