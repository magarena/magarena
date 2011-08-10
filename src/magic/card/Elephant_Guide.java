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

public class Elephant_Guide {

	public static final MagicSpellCardEvent V6492 =new MagicPlayAuraEvent("Elephant Guide",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance());
    public static final MagicTrigger V10474 =new MagicTrigger(MagicTriggerType.WhenOtherPutIntoGraveyardFromPlay,"Elephant Guide") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent.getEnchantedCreature()==data) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{player},this,"Put a 3/3 green Elephant creature token onto the battlefield.");
			}			
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.ELEPHANT_TOKEN_CARD));
		}
    };
    
}
