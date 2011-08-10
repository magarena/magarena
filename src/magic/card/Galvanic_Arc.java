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

public class Galvanic_Arc {

	public static final MagicSpellCardEvent V6502 =new MagicPlayAuraEvent("Galvanic Arc",
			MagicTargetChoice.TARGET_CREATURE,MagicFirstStrikeTargetPicker.getInstance());
    public static final MagicTrigger V10571 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Galvanic Arc") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(3),
				new Object[]{permanent},this,"Galvanic Arc deals 3 damage to target creature or player$.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],target,3,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };
    
}
