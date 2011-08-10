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

public class Breath_of_Darigaaz {

	public static final MagicSpellCardEvent V5269 =new MagicSpellCardEvent("Breath of Darigaaz") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,
				new MagicKickerChoice(null,MagicManaCost.TWO,false),
				new Object[]{cardOnStack},this,
				"Breath of Darigaaz deals 1 damage to each creature without flying and each player. "+
				"If Breath of Darigaaz was kicked$$, it deals 4 damage to each creature without flying and each player instead.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			final MagicSource source=cardOnStack.getSource();
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final int amount=(Integer)choiceResults[1]>0?4:1;
			final Collection<MagicTarget> targets=game.filterTargets(cardOnStack.getController(),MagicTargetFilter.TARGET_CREATURE_WITHOUT_FLYING);
			for (final MagicTarget target : targets) {

				final MagicDamage damage=new MagicDamage(source,target,amount,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
			for (final MagicPlayer player : game.getPlayers()) {
				
				final MagicDamage damage=new MagicDamage(source,player,amount,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
	
}
