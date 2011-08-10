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

public class Eldrazi_Monument {

    public static final MagicTrigger V10300 =new MagicTrigger(MagicTriggerType.AtUpkeep,"Eldrazi Monument") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,new Object[]{permanent,player},this,"Sacrifice a creature. If you can't, sacrifice Eldrazi Monument.");
			}
			return null;
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicPlayer player=(MagicPlayer)data[1];
			if (player.controlsPermanentWithType(MagicType.Creature)) {
				game.addEvent(new MagicSacrificePermanentEvent(permanent,player,MagicTargetChoice.SACRIFICE_CREATURE));
			} else {
				game.doAction(new MagicSacrificeAction(permanent));				
			}			
		}
    };
    
}
