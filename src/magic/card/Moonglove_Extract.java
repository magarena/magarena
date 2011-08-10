package magic.card;

import magic.model.*;
import magic.model.action.MagicDealDamageAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.*;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;

public class Moonglove_Extract {

	public static final MagicPermanentActivation V2693 =new MagicPermanentActivation(			"Moonglove Extract",
            null,
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(2),
                    new Object[]{source},
                    this,
                    "Moonglove Extract deals 2 damage to target creature or player$.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {

			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],target,2,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
	
}
