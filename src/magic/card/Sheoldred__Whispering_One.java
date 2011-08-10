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

public class Sheoldred__Whispering_One {

    public static final MagicTrigger V8940 =new MagicTrigger(MagicTriggerType.AtUpkeep,"Sheoldred, Whispering One") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,MagicGraveyardTargetPicker.getInstance(),
					new Object[]{player},this,"Return target creature card$ from your graveyard to the battlefield.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicCard card=event.getTarget(game,choiceResults,0);
			if (card!=null) {
				game.doAction(new MagicReanimateAction((MagicPlayer)data[0],card,MagicPlayCardAction.NONE));
			}
		}
    };
    
    public static final MagicTrigger V8962 =new MagicTrigger(MagicTriggerType.AtUpkeep,"Sheoldred, Whispering One") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			if (player!=data) {
				return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent,data},this,"Your opponent sacrifices a creature.");
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
