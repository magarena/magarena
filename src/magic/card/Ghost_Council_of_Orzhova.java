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

public class Ghost_Council_of_Orzhova {

	public static final MagicPermanentActivation V954 =new MagicPermanentActivation(            "Ghost Council of Orzhova",
			new MagicCondition[]{MagicManaCost.ONE.getCondition(),MagicCondition.TWO_CREATURES_CONDITION},
            new MagicActivationHints(MagicTiming.Removal,false,1),
            "Exile"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			final MagicPlayer player=source.getController();
			return new MagicEvent[]{					
				new MagicPayManaCostEvent(source,player,MagicManaCost.ONE),
				new MagicSacrificePermanentEvent(source,player,MagicTargetChoice.SACRIFICE_CREATURE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(source,source.getController(),new Object[]{source},this,
				"Exile Ghost Council of Orzhova. Return it to the battlefield under its owner's control at end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			game.doAction(new MagicExileUntilEndOfTurnAction((MagicPermanent)data[0]));
		}
	};
	
    public static final MagicTrigger V7427 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Ghost Council of Orzhova") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,MagicTargetChoice.TARGET_OPPONENT,
				new Object[]{player},this,"Target opponent$ loses 1 life and you gain 1 life.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				game.doAction(new MagicChangeLifeAction(player,-1));
				game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],1));
			}
		}		
    };

}
