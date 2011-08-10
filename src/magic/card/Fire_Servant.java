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

public class Fire_Servant {

    public static final MagicTrigger V7272 =new MagicTrigger(MagicTriggerType.IfDamageWouldBeDealt,"Fire Servant",3) {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			final MagicSource source=damage.getSource();
			if (source.getController()==permanent.getController()&&source.isSpell()&&
				MagicColor.Red.hasColor(source.getColorFlags())&&source.getCardDefinition().isSpell()) {
				// Generates no event or action.
				damage.setAmount(damage.getAmount()<<1);
			}			
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
		}
    };

}
