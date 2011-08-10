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

public class Death_Grasp {

	public static final MagicSpellCardEvent V5423 =new MagicSpellCardEvent("Death Grasp") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final int amount=payedCost.getX();
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(amount),
				new Object[]{cardOnStack,player,amount},this,"Death Grasp deals "+amount+" damage to target creature or player$. You gain "+amount+" life.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final int amount=(Integer)data[2];
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage(cardOnStack.getCard(),target,amount,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[1],amount));
		}
	};
	
}
