package magic.card;

import magic.model.*;
import magic.model.action.MagicDealDamageAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.action.MagicTargetAction;

public class Goblin_Artillery {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
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
                    source + " deals 2 damage to target creature or player$ and 3 damage to you.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicSource source=(MagicSource)data[0];
            event.processTarget(game,choiceResults,0,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage1=new MagicDamage(source,target,2,false);
                    game.doAction(new MagicDealDamageAction(damage1));
                    final MagicDamage damage2=new MagicDamage(source,(MagicTarget)data[1],3,false);
                    game.doAction(new MagicDealDamageAction(damage2));
                }
			});
		}
	};
}
