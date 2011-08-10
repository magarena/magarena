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

public class Flame_Kin_Zealot {

	public static final MagicTrigger V7322 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Flame-Kin Zealot") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"Creatures you control get +1/+1 and gain haste until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				
				final MagicPermanent creature=(MagicPermanent)target;
				game.doAction(new MagicChangeTurnPTAction(creature,1,1));
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Haste));
			}
		}
    };
    
}
