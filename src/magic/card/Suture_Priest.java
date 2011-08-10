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

public class Suture_Priest {

    public static final MagicTrigger V8872 =new MagicTrigger(MagicTriggerType.WhenOtherComesIntoPlay,"Suture Priest") {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPermanent otherPermanent=(MagicPermanent)data;
			if (otherPermanent!=permanent&&otherPermanent.isCreature()) {
				final MagicPlayer player=permanent.getController();
				final MagicPlayer controller=otherPermanent.getController();
				final boolean same=controller==player;
				return new MagicEvent(
                        permanent,
                        player,
                        new Object[]{controller,same?1:-1},
                        this,
                        controller.getName()+(same?" gains 1 life.":" loses 1 life."));
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,
                final Object data[],final Object[] choiceResults) {
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],(Integer)data[1]));
		}		
    };
    
}
