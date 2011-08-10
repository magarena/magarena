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

public class Douse_in_Gloom {

	public static final MagicSpellCardEvent V3708 =new MagicSpellCardEvent("Douse in Gloom") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_CREATURE,new MagicDamageTargetPicker(2),
				new Object[]{cardOnStack,player},this,"Douse in Gloom deals 2 damage to target creature$ and you gain 2 life.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));			
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage(cardOnStack.getCard(),target,2,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[1],2));
		}
	};
	
}
