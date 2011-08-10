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

public class Armadillo_Cloak {

	public static final MagicSpellCardEvent V6480 =new MagicPlayAuraEvent("Armadillo Cloak",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance());
    public static final MagicTrigger V10453 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Armadillo Cloak") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (permanent.getEnchantedCreature()==damage.getSource()) {
				final MagicPlayer player=permanent.getController();
				final int amount=damage.getDealtAmount();
				return new MagicEvent(permanent,player,new Object[]{player,amount},this,"You gain "+amount+" life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],(Integer)data[1]));
		}
    };
        
}
