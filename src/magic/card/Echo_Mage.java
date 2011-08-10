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

public class Echo_Mage {

	public static final MagicPermanentActivation V615 =        new MagicLevelUpActivation("Echo Mage",MagicManaCost.ONE_BLUE,4);
	
	public static final MagicPermanentActivation V617 =new MagicPermanentActivation("Echo Mage",
			new MagicCondition[]{
                MagicCondition.TWO_CHARGE_COUNTERS_CONDITION,
                MagicCondition.CAN_TAP_CONDITION,MagicManaCost.BLUE_BLUE.getCondition()},
			new MagicActivationHints(MagicTiming.Spell),
            "Copy"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.BLUE_BLUE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			final int amount=source.getCounters(MagicCounterType.Charge)>=4?2:1;
			final String description = amount == 2 ?
					"Copy target instant or sorcery spell$ twice. You may choose new targets for the copies.":
					"Copy target instant or sorcery spell$. You may choose new targets for the copy.";
			return new MagicEvent(
                    source,
                    player,
                    MagicTargetChoice.TARGET_INSTANT_OR_SORCERY_SPELL,
                    new Object[]{player,amount},
                    this,
                    description);
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicCardOnStack targetSpell=event.getTarget(game,choiceResults,0);
			if (targetSpell!=null) {
				final MagicPlayer player=(MagicPlayer)data[0];
				final int amount=(Integer)data[1];
				for (int count=amount;count>0;count--) {
					game.doAction(new MagicCopyCardOnStackAction(player,targetSpell));
				}
			}
		}
	};
	
}
