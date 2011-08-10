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

public class Meglonoth {

    public static final MagicTrigger V8021 =new MagicTrigger(MagicTriggerType.WhenBlocks,"Meglonoth") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent==data) {
				final MagicPermanent blocked=permanent.getBlockedCreature();
				if (blocked!=null) {
					return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent,blocked.getController()},this,
						"Megnoloth deals damage to the blocked creature's controller equal to Megnoloth's power.");
				}
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicDamage damage=new MagicDamage(permanent,(MagicTarget)data[1],permanent.getPower(game),false);
			game.doAction(new MagicDealDamageAction(damage));
		}
    };

}
