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

public class Soul_Link {

	public static final MagicSpellCardEvent V6526 =new MagicPlayAuraEvent("Soul Link",
			MagicTargetChoice.TARGET_CREATURE,new MagicNoCombatTargetPicker(true,true,true));	
    public static final MagicTrigger V10703 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Soul Link") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent.getEnchantedCreature()) {
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
    
    public static final MagicTrigger V10724 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Soul Link") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getTarget()==permanent.getEnchantedCreature()) {
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
