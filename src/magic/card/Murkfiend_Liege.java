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

public class Murkfiend_Liege {

    public static final MagicTrigger V8221 =new MagicTrigger(MagicTriggerType.AtUpkeep,"Murkfiend Liege") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			if (player!=data) {
				return new MagicEvent(permanent,player,new Object[]{player},this,"Untap all green and/or blue creatures you control.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				
				final MagicPermanent creature=(MagicPermanent)target;
				final int colorFlags=creature.getColorFlags();
				if (creature.isTapped()&&(MagicColor.Blue.hasColor(colorFlags)||MagicColor.Green.hasColor(colorFlags))) {
					game.doAction(new MagicUntapAction(creature));
				}
			}
		}

		@Override
		public boolean usesStack() {

			return false;
		}
    };
    
}
