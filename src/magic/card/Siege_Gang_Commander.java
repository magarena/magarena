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

public class Siege_Gang_Commander {

	public static final MagicPermanentActivation V1674 =new MagicPermanentActivation(            "Siege-Gang Commander",
			new MagicCondition[]{MagicManaCost.ONE_RED.getCondition(),MagicCondition.CONTROL_GOBLIN_CONDITION},
			new MagicActivationHints(MagicTiming.Removal,true),
            "Damage") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			final MagicPlayer player=source.getController();
			return new MagicEvent[]{					
				new MagicPayManaCostEvent(source,player,MagicManaCost.ONE_RED),
				new MagicSacrificePermanentEvent(source,player,MagicTargetChoice.SACRIFICE_GOBLIN)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(source,source.getController(),MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(2),
				new Object[]{source},this,"Siege-Gang Commander deals 2 damage to target creature or player$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],target,2,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
	
    public static final MagicTrigger V8719 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Siege-Gang Commander") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"You put three 1/1 red Goblin creature tokens onto the battlefield.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[0];
			for (int count=3;count>0;count--) {
				
				game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.GOBLIN1_TOKEN_CARD));
			}
		}		
    };

}
