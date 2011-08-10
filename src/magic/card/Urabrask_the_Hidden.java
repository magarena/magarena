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

public class Urabrask_the_Hidden {

    public static final MagicTrigger V9273 =new MagicTrigger(MagicTriggerType.WhenOtherComesIntoPlay,"Urabrask the Hidden") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPermanent otherPermanent=(MagicPermanent)data;
			if (otherPermanent.isCreature()&&otherPermanent.getController()!=permanent.getController()) {
				return new MagicTapEvent(otherPermanent);
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

		}

		@Override
		public boolean usesStack() {

			return false;
		}		
    };
    
}
