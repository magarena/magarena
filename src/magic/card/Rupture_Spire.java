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

public class Rupture_Spire {

    public static final MagicTrigger V9928 =new MagicTappedIntoPlayTrigger("Rupture Spire");
     
    public static final MagicTrigger V9930 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Rupture Spire") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),
	new MagicMayChoice("You may pay {1}.",new MagicPayManaCostChoice(MagicManaCost.ONE)),
    new Object[]{permanent},this,
				"You may$ pay {1}. If you don't, sacrifice Rupture Spire.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isNoChoice(choiceResults[0])) {
				game.doAction(new MagicSacrificeAction((MagicPermanent)data[0]));
			}
		}
    };
    
}
