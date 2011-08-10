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

public class Hissing_Miasma {

    public static final MagicTrigger V10139 =new MagicTrigger(MagicTriggerType.WhenAttacks,"Hissing Miasma") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPermanent creature=(MagicPermanent)data;
			final MagicPlayer controller=creature.getController();
			if (controller!=permanent.getController()) {
				return new MagicEvent(permanent,controller,new Object[]{controller},this,"You lose 1 life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],-1));
		}
    };

}
