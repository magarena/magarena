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

public class Unquestioned_Authority {

	public static final MagicSpellCardEvent V6532 =new MagicPlayAuraEvent("Unquestioned Authority",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicUnblockableTargetPicker.getInstance());
    public static final MagicTrigger V10745 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Unquestioned Authority") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"You draw a card.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicDrawAction((MagicPlayer)data[0],1));
		}
    };
    
}
