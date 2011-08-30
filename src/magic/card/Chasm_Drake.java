package magic.card;

import magic.model.*;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicFlyingTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;
import magic.model.action.MagicPermanentAction;

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
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Flying));
                }
            });
		}
    };
}
