package magic.card;

import magic.model.*;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicDealDamageAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.action.MagicPermanentAction;

public class Arbalest_Elite {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicManaCost.TWO_WHITE.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.TWO_WHITE)};
		}
		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_ATTACKING_OR_BLOCKING_CREATURE,
                    new MagicDamageTargetPicker(3),
                    new Object[]{source},
                    this,
                    source + " deals 3 damage to target attacking or blocking creature$. " +
                    source + " doesn't untap during your next untap step.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final MagicDamage damage=new MagicDamage((MagicPermanent)data[0],creature,3,false);
                    game.doAction(new MagicDealDamageAction(damage));
                    game.doAction(new MagicChangeStateAction((MagicPermanent)data[0],MagicPermanentState.DoesNotUntap,true));
                }
			});
		}
	};
}
