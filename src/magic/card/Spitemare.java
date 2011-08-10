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

public class Spitemare {

    public static final MagicTrigger V8784 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Spitemare") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getTarget()==permanent) {
				final int amount=damage.getDealtAmount();
				return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(amount),
					new Object[]{permanent,amount},this,"Spitemare deals "+amount+" damage to target creature or player$.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],target,(Integer)data[1],false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };
    
}
