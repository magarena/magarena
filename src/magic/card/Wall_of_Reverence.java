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

public class Wall_of_Reverence {

    public static final MagicTrigger V9421 =new MagicTrigger(MagicTriggerType.AtEndOfTurn,"Wall of Reverence") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,MagicTargetChoice.TARGET_CREATURE_YOU_CONTROL,MagicPowerTargetPicker.getInstance(),
					new Object[]{player},this,"You gain life equal to the power of target creature$ you control.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],creature.getPower(game)));
			}
		}
    };

}
