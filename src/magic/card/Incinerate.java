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

public class Incinerate {

	public static final MagicSpellCardEvent V3963 =new MagicSpellCardEvent("Incinerate") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(3,true),
				new Object[]{cardOnStack},this,
				"Incinerate deals 3 damage to target creature or player$. A creature dealt damage this way can't be regenerated this turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage(cardOnStack.getCard(),target,3,false);
				damage.setNoRegeneration();
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
		
}
