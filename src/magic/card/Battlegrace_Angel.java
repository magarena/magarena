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

public class Battlegrace_Angel {

    public static final MagicTrigger V6857 =new MagicTrigger(MagicTriggerType.WhenAttacks,"Battlegrace Angel") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPermanent creature=(MagicPermanent)data;
			final MagicPlayer player=permanent.getController();
			if (creature.getController()==player&&player.getNrOfAttackers()==1) {
				return new MagicEvent(permanent,player,new Object[]{creature},this,creature.getName()+" gains lifelink until end of turn.");
			}
			return null;
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {			

			game.doAction(new MagicSetAbilityAction((MagicPermanent)data[0],MagicAbility.LifeLink));
		}
    };
    
}
