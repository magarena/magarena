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

public class Vines_of_Vastwood {

	public static final MagicSpellCardEvent V4930 =new MagicSpellCardEvent("Vines of Vastwood") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),
				new MagicKickerChoice(MagicTargetChoice.TARGET_CREATURE,MagicManaCost.GREEN,false),
				MagicPumpTargetPicker.getInstance(),new Object[]{cardOnStack},this,
				"Target creature$ can't be the target of spells or abilities your opponent controls this turn. "+
				"If Vines of Vastwood was kicked$, that creature gets +4/+4 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
                final MagicPlayer player = event.getPlayer();
                if (player.getIndex() == 0) {
                    game.doAction(new MagicSetAbilityAction(creature,MagicAbility.CannotBeTheTarget1));
                } else {
                    game.doAction(new MagicSetAbilityAction(creature,MagicAbility.CannotBeTheTarget0));
                }
				if (((Integer)choiceResults[1])>0) {
					game.doAction(new MagicChangeTurnPTAction(creature,4,4));
				}
			}
		}
	};
	
}
