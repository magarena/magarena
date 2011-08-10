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

public class Narcolepsy {

	public static final MagicSpellCardEvent V6512 =new MagicPlayAuraEvent("Narcolepsy",
			MagicTargetChoice.NEG_TARGET_CREATURE,new MagicNoCombatTargetPicker(true,true,true));
    public static final MagicTrigger V10610 =new MagicTrigger(MagicTriggerType.AtUpkeep,"Narcolepsy") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPermanent enchantedCreature=permanent.getEnchantedCreature();
			if (enchantedCreature!=null&&!enchantedCreature.isTapped()) {
				return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,
					"If "+enchantedCreature.getName()+" is untapped, tap it.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicPermanent enchantedCreature=permanent.getEnchantedCreature();
			if (enchantedCreature!=null&&!enchantedCreature.isTapped()) {
				game.doAction(new MagicTapAction(enchantedCreature,true));
			}
		}		
    };
    
}
