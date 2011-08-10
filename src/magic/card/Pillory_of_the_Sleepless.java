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

public class Pillory_of_the_Sleepless {

	public static final MagicSpellCardEvent V6516 =new MagicPlayAuraEvent("Pillory of the Sleepless",
			MagicTargetChoice.NEG_TARGET_CREATURE,new MagicNoCombatTargetPicker(true,true,true));
    public static final MagicTrigger V10634 =new MagicTrigger(MagicTriggerType.AtUpkeep,"Pillory of the Sleepless") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPermanent enchanted=permanent.getEnchantedCreature();
			if (enchanted!=null) {
				final MagicPlayer player=enchanted.getController();
				if (player==data) {
					return new MagicEvent(enchanted,player,new Object[]{player},this,"You lose 1 life.");
				}
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],-1));
		}
    };
    
}
