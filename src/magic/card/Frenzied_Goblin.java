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

public class Frenzied_Goblin {

    public static final MagicTrigger V7380 =new MagicTrigger(MagicTriggerType.WhenAttacks,"Frenzied Goblin") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent==data) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,
	new MagicMayChoice(
			"You may pay {R}.",new MagicPayManaCostChoice(MagicManaCost.RED),MagicTargetChoice.NEG_TARGET_CREATURE),new MagicNoCombatTargetPicker(false,true,false),
					MagicEvent.NO_DATA,this,"You may$ pay {R}$. If you do, target creature$ can't block this turn.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicPermanent creature=event.getTarget(game,choiceResults,2);
				if (creature!=null) {
					game.doAction(new MagicSetAbilityAction(creature,MagicAbility.CannotBlock));
				}
			}
		}
    };
    
}
