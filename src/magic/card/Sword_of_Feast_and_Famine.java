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

public class Sword_of_Feast_and_Famine {

    public static final MagicTrigger V9734 = new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Sword of Feast and Famine") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent.getEquippedCreature() && damage.getTarget().isPlayer() && damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				final MagicPlayer damagedPlayer=(MagicPlayer)damage.getTarget();
				return new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent,player,damagedPlayer},
                        this,
                        damagedPlayer + " discards a card and you untap all lands you control.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.addEvent(new MagicDiscardEvent((MagicPermanent)data[0],(MagicPlayer)data[2],1,false));
			final Collection<MagicTarget> targets = 
                game.filterTargets((MagicPlayer)data[1],MagicTargetFilter.TARGET_LAND_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				game.doAction(new MagicUntapAction((MagicPermanent)target));
			}
		}
    };
    
}
