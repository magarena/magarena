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

public class Blaze {

	public static final MagicSpellCardEvent V5197 =new MagicSpellCardEvent("Blaze") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {

			final int amount=payedCost.getX();
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
				new MagicDamageTargetPicker(amount),new Object[]{cardOnStack,amount},this,"Blaze deals "+amount+" damage to target creature or player$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage(cardOnStack.getCard(),target,(Integer)data[1],false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};

}
