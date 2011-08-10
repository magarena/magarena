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

public class Sword_of_Fire_and_Ice {

    public static final MagicTrigger V9767 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Sword of Fire and Ice") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent.getEquippedCreature()&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(2),
					new Object[]{permanent,player},this,"Sword of Fire and Ice deals 2 damage to target creature or player$ and you draw a card.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],target,2,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
			game.doAction(new MagicDrawAction((MagicPlayer)data[1],1));
		}
    };

}
