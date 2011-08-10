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

public class Char {

	public static final MagicSpellCardEvent V3439 =new MagicSpellCardEvent("Char") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(4),
				new Object[]{cardOnStack,player},this,"Char deals 4 damage to target creature or player$ and 2 damage to you.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicSource source=cardOnStack.getCard();
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage1=new MagicDamage(source,target,4,false);
				game.doAction(new MagicDealDamageAction(damage1));
				final MagicDamage damage2=new MagicDamage(source,(MagicPlayer)data[1],2,false);
				game.doAction(new MagicDealDamageAction(damage2));
			}
		}
	};
	
}
