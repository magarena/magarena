package magic.card;

import magic.model.*;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicExileUntilEndOfTurnAction;
import magic.model.action.MagicPlayerAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.*;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;
import magic.model.condition.MagicCondition;

public class Ghost_Council_of_Orzhova {
	public static final MagicPermanentActivation A =new MagicPermanentActivation(
			new MagicCondition[]{
                MagicManaCost.ONE.getCondition(),
                MagicCondition.ONE_CREATURE_CONDITION
            },
            new MagicActivationHints(MagicTiming.Removal,false,1),
            "Exile") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			final MagicPlayer player=source.getController();
			return new MagicEvent[]{					
				new MagicPayManaCostEvent(source,player,MagicManaCost.ONE),
				new MagicSacrificePermanentEvent(source,player,MagicTargetChoice.SACRIFICE_CREATURE)};
		}
		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Exile " + source + ". Return it to the battlefield under its owner's control at end of turn.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicExileUntilEndOfTurnAction((MagicPermanent)data[0]));
		}
	};
	
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenComesIntoPlay) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(
                    permanent,
                    player,
                    MagicTargetChoice.TARGET_OPPONENT,
                    new Object[]{player},
                    this,
                    "Target opponent$ loses 1 life and " + player + " gains 1 life.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    game.doAction(new MagicChangeLifeAction(player,-1));
                    game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],1));
                }
			});
		}		
    };
}
