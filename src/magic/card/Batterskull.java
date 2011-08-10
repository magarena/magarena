package magic.card;

import magic.model.*;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.trigger.MagicLivingWeaponTrigger;
import magic.model.trigger.MagicTrigger;

public class Batterskull {
	public static final MagicPermanentActivation RET = new MagicPermanentActivation(
            new MagicCondition[]{MagicManaCost.THREE.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "Return"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.THREE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Return Batterskull to its owner's hand.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicRemoveFromPlayAction((MagicPermanent)data[0],MagicLocationType.OwnersHand));
		}
	};
    
    public static final MagicTrigger LW = new MagicLivingWeaponTrigger();
}
