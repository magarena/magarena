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

public class Goblin_Shortcutter {

    public static final MagicTrigger V7521 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Goblin Shortcutter") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_CREATURE,new MagicNoCombatTargetPicker(false,true,false),
				MagicEvent.NO_DATA,this,"Target creature$ can't block this turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.CannotBlock));
			}
		}
    };

}
