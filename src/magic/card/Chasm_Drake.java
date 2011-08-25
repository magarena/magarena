package magic.card;

import magic.model.*;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicFlyingTargetPicker;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Chasm_Drake {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenAttacks) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
            final MagicPlayer player = permanent.getController();
			return (permanent == data) ?
                new MagicEvent(
                        permanent,
                        player,
                        MagicTargetChoice.TARGET_CREATURE_YOU_CONTROL,
                        MagicFlyingTargetPicker.getInstance(),
                        new Object[]{permanent},
                        this,
                        "Target creature$ you control gains flying until end of turn."):
                null;           
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent creature = event.getTarget(game,choiceResults,0);
			if (creature != null) {
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Flying));
			}
		}
    };
}
