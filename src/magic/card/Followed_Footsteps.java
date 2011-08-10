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

public class Followed_Footsteps {

	public static final MagicSpellCardEvent V6500 =new MagicPlayAuraEvent("Followed Footsteps",
			MagicTargetChoice.TARGET_CREATURE,MagicCopyTargetPicker.getInstance());
    public static final MagicTrigger V10546 =new MagicTrigger(MagicTriggerType.AtUpkeep,"Followed Footsteps") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPlayer player=permanent.getController();
			final MagicPermanent enchantedCreature=permanent.getEnchantedCreature();
			if (player==data&&enchantedCreature!=null) {
				return new MagicEvent(permanent,player,new Object[]{permanent,player},this,
					"You put a token that's a copy of enchanted creature onto the battlefield.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicPermanent enchantedCreature=permanent.getEnchantedCreature();
			if (enchantedCreature!=null) {
				game.doAction(new MagicPlayTokenAction((MagicPlayer)data[1],enchantedCreature.getCardDefinition()));
			}
		}		
    };
    
}
