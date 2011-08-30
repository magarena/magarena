package magic.card;

import magic.model.*;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.target.MagicTapTargetPicker;
import magic.model.action.MagicPermanentAction;

public class Stun_Sniper {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicManaCost.ONE.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.ONE)};
		}
		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    new MagicTapTargetPicker(true,false),
                    new Object[]{source},
                    this,
                    source + " deals 1 damage to target creature$. Tap that creature.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final MagicDamage damage=new MagicDamage((MagicPermanent)data[0],creature,1,false);
                    game.doAction(new MagicDealDamageAction(damage));
                    if (!creature.isTapped()) {
                        game.doAction(new MagicTapAction(creature,true));
                    }
                }
			});
		}
	};
}
