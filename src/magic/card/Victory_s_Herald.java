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

public class Victory_s_Herald {

    public static final MagicTrigger V9322 =new MagicTrigger(MagicTriggerType.WhenAttacks,"Victory's Herald") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent==data) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{player},this,"Attacking creatures gain flying and lifelink until end of turn.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_ATTACKING_CREATURE);
			for (final MagicTarget target : targets) {
				
				game.doAction(new MagicSetAbilityAction((MagicPermanent)target,MagicAbility.VICTORYS_HERALD_FLAGS));
			}
		}		
    };
    
}
