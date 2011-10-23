package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicManaType;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicPlayerAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicPayManaCostTapEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTapManaActivation;
import magic.model.event.MagicTiming;
import magic.model.target.MagicDamageTargetPicker;
import java.util.Arrays;

public class Stensia_Bloodhall {
    public static final MagicManaActivation M = new MagicTapManaActivation(
            Arrays.asList(MagicManaType.Colorless),0);
    
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{
            		MagicCondition.CAN_TAP_CONDITION,
            		MagicManaCost.THREE_BLACK_RED.getCondition()
            },
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
					new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.THREE_BLACK_RED)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_PLAYER,
                    new MagicDamageTargetPicker(2),
                    new Object[]{source},
                    this,
                    source + " deals 2 damage to target player$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    final MagicDamage damage = new MagicDamage((MagicPermanent)data[0],player,2,false);
                    game.doAction(new MagicDealDamageAction(damage));
                }
			});
		}
	};
}
