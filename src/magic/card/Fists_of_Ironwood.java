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

public class Fists_of_Ironwood {

	public static final MagicSpellCardEvent V6496 =new MagicPlayAuraEvent("Fists of Ironwood",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicTrampleTargetPicker.getInstance());
    public static final MagicTrigger V10513 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Fists of Ironwood") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"You put two 1/1 green Saproling creature tokens onto the battlefield.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.SAPROLING_TOKEN_CARD));
			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.SAPROLING_TOKEN_CARD));
		}
    };
    
}
