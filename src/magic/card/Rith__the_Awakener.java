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

public class Rith__the_Awakener {

    public static final MagicTrigger V8604 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Rith, the Awakener") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,
	new MagicMayChoice(
			"You may pay {2}{G}.",new MagicPayManaCostChoice(MagicManaCost.TWO_GREEN),MagicColorChoice.MOST_INSTANCE),
    new Object[]{player},this,
					"You may$ pay {2}{G}$. If you do, choose a color$. "+
					"Put a 1/1 green Saproling creature token onto the battlefield for each permanent of that color.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicPlayer player=(MagicPlayer)data[0];
				final MagicColor color=(MagicColor)choiceResults[2];
				final Collection<MagicTarget> targets=game.filterTargets(player,MagicTargetFilter.TARGET_PERMANENT);
				for (final MagicTarget target : targets) {
					
					final MagicPermanent permanent=(MagicPermanent)target;
					if (color.hasColor(permanent.getColorFlags())) {
						game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.SAPROLING_TOKEN_CARD));
					}
				}
			}
		}
    };
    
}
