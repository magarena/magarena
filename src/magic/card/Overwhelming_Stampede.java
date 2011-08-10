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

public class Overwhelming_Stampede {

	public static final MagicSpellCardEvent V5758 =new MagicSpellCardEvent("Overwhelming Stampede") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack,player},this,
				"Until end of turn, creatures you control gain trample and get +X/+X, where X is the greatest power among creatures you control.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[1],MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			int power=0;
			for (final MagicTarget target : targets) {

				final MagicPermanent creature=(MagicPermanent)target;
				power=Math.max(power,creature.getPowerToughness(game).power);
			}
			for (final MagicTarget target : targets) {
				
				final MagicPermanent creature=(MagicPermanent)target;
				game.doAction(new MagicChangeTurnPTAction(creature,power,power));
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Trample));
			}
		}
	};
	
}
