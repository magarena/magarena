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

public class Butcher_of_Malakir {

    public static final MagicTrigger V6895 =new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard,"Butcher of Malakir") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			if (MagicLocationType.Play==triggerData.fromLocation) {
				final MagicPlayer controller=permanent.getController();
				return new MagicEvent(permanent,controller,new Object[]{permanent,game.getOpponent(controller)},this,"Your opponent sacrifices a creature.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer opponent=(MagicPlayer)data[1];
			if (opponent.controlsPermanentWithType(MagicType.Creature)) {
				game.addEvent(new MagicSacrificePermanentEvent((MagicPermanent)data[0],opponent,MagicTargetChoice.SACRIFICE_CREATURE));
			}
		}
    };
    
    public static final MagicTrigger V6918 =new MagicTrigger(MagicTriggerType.WhenOtherPutIntoGraveyardFromPlay,"Butcher of Malakir") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPermanent otherPermanent=(MagicPermanent)data;
			final MagicPlayer controller=permanent.getController();
			if (otherPermanent!=permanent&&otherPermanent.getController()==controller&&otherPermanent.isCreature()) {
				return new MagicEvent(permanent,controller,new Object[]{permanent,game.getOpponent(controller)},this,"Your opponent sacrifices a creature.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer opponent=(MagicPlayer)data[1];
			if (opponent.controlsPermanentWithType(MagicType.Creature)) {
				game.addEvent(new MagicSacrificePermanentEvent((MagicPermanent)data[0],opponent,MagicTargetChoice.SACRIFICE_CREATURE));
			}
		}
    };

}
