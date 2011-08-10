package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.*;
import magic.model.action.MagicPlayTokenAction;
import magic.model.action.MagicRegenerateAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicSingleActivationCondition;
import magic.model.event.*;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Skeletal_Vampire {

	public static final MagicPermanentActivation V1738 =new MagicPermanentActivation(            "Skeletal Vampire",
			new MagicCondition[]{MagicManaCost.THREE_BLACK_BLACK.getCondition(),MagicCondition.CONTROL_BAT_CONDITION},
			new MagicActivationHints(MagicTiming.Token,true),
            "Token") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			final MagicPlayer player=source.getController();
			return new MagicEvent[]{					
				new MagicPayManaCostEvent(source,player,MagicManaCost.THREE_BLACK_BLACK),
				new MagicSacrificePermanentEvent(source,player,MagicTargetChoice.SACRIFICE_BAT)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(source,player,new Object[]{player},this,"Put two 1/1 black Bat creature tokens with flying onto the battlefield.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.BAT_TOKEN_CARD));
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.BAT_TOKEN_CARD));			
		}
	};

	public static final MagicPermanentActivation V1767 =new MagicPermanentActivation(            "Skeletal Vampire",
			new MagicCondition[]{
                MagicCondition.CAN_REGENERATE_CONDITION,
                MagicCondition.CONTROL_BAT_CONDITION,
                new MagicSingleActivationCondition()},
			new MagicActivationHints(MagicTiming.Pump),
            "Regen") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificePermanentEvent(source,source.getController(),MagicTargetChoice.SACRIFICE_BAT)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(source,source.getController(),new Object[]{source},this,"Regenerate Skeletal Vampire.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			game.doAction(new MagicRegenerateAction((MagicPermanent)data[0]));
		}
	};
	
    public static final MagicTrigger V8739 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Skeletal Vampire") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"You put two 1/1 black Bat creature tokens with flying onto the battlefield.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.BAT_TOKEN_CARD));			
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.BAT_TOKEN_CARD));
		}		
    };
        
}
