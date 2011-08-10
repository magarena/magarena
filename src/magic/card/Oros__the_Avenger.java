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

public class Oros__the_Avenger {

    public static final MagicTrigger V8355 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Oros, the Avenger") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final MagicPlayer player=permanent.getController();		
				return new MagicEvent(permanent,player,
	new MagicMayChoice("You may pay {2}{W}.",new MagicPayManaCostChoice(MagicManaCost.TWO_WHITE)),
    new Object[]{player,permanent},this,
					"You may$ pay {2}{W}$. If you do, Oros deals 3 damage to each nonwhite creature.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicPermanent permanent=(MagicPermanent)data[1];
				final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_NONWHITE_CREATURE);
				for (final MagicTarget target : targets) {
					final MagicDamage damage=new MagicDamage(permanent,target,3,false);
					game.doAction(new MagicDealDamageAction(damage));
				}
			}
		}
    };

}
