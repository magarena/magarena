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

public class Goblin_Artillery {

	public static final MagicPermanentActivation V1040 =new MagicPermanentActivation(			"Goblin Artillery",
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(
                    source,
                    player,
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(2),
                    new Object[]{source,player},
                    this,
                    "Goblin Artillery deals 2 damage to target creature or player$ and 3 damage to you.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicSource source=(MagicSource)data[0];
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage1=new MagicDamage(source,target,2,false);
				game.doAction(new MagicDealDamageAction(damage1));
				final MagicDamage damage2=new MagicDamage(source,(MagicTarget)data[1],3,false);
				game.doAction(new MagicDealDamageAction(damage2));
			}
		}
	};

}
