package magic.card;

import magic.model.*;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicNoCombatTargetPicker;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

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
