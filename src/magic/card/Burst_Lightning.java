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

public class Burst_Lightning {

	public static final MagicSpellCardEvent V3412 =new MagicSpellCardEvent("Burst Lightning") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,
				new MagicKickerChoice(MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,MagicManaCost.FOUR,false),
				new MagicDamageTargetPicker(2),new Object[]{cardOnStack},this,
				"Burst Lightning deals 2 damage to target creature or player$. "+
				"If Burst Lightning was kicked$, it deals 4 damage to that creature or player instead.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final int amount=((Integer)choiceResults[1])>0?4:2;
				final MagicDamage damage=new MagicDamage(cardOnStack.getCard(),target,amount,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};

}
