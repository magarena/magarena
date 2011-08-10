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

public class Mask_of_Memory {

    public static final MagicTrigger V9556 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Mask of Memory") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (permanent.getEquippedCreature()==damage.getSource()&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,
    new MagicSimpleMayChoice(
            "You may draw two cards.",MagicSimpleMayChoice.DRAW_CARDS,2),
    new Object[]{permanent,player},this,
					"You may$ draw two cards. If you do, discard a card.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicChoice.isYesChoice(choiceResults[0])) {
				final MagicPlayer player=(MagicPlayer)data[1];
				game.doAction(new MagicDrawAction(player,2));
				game.addEvent(new MagicDiscardEvent((MagicPermanent)data[0],player,1,false));
			}
		}
    };

}
