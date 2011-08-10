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

public class Stuffy_Doll {

	public static final MagicPermanentActivation V1893 =new MagicPermanentActivation(			"Stuffy Doll",
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Stuffy Doll deals 1 damage to itself.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicDamage damage=new MagicDamage(permanent,permanent,1,false);
			game.doAction(new MagicDealDamageAction(damage));
		}
	};
	
    public static final MagicTrigger V8829 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Stuffy Doll") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getTarget()==permanent) {
				final MagicPlayer player=permanent.getController();
				final int amount=damage.getDealtAmount();
				return new MagicEvent(permanent,player,new Object[]{permanent,game.getOpponent(player),amount},this,
					"Stuffy Doll deals "+amount+" damage to your opponent.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicDamage damage=new MagicDamage((MagicSource)data[0],(MagicTarget)data[1],(Integer)data[2],false);
			game.doAction(new MagicDealDamageAction(damage));
		}
    };
    
}
