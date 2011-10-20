package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicPlayerAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Sludge_Strider {
	public static final MagicWhenOtherComesIntoPlayTrigger T1 = new MagicWhenOtherComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
			return (otherPermanent != permanent &&
					otherPermanent.isArtifact(game)) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new MagicMayChoice(
                        		"You may pay {1}.",
                        		new MagicPayManaCostChoice(MagicManaCost.ONE),
                        		MagicTargetChoice.NEG_TARGET_PLAYER),
                        new Object[]{permanent.getController()},
                        this,
                        "You may$ pay {1}$. If you do, target player$ loses 1 life and you gain 1 life."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				event.processTargetPlayer(game,choiceResults,2,new MagicPlayerAction() {
	                public void doAction(final MagicPlayer player) {
	                    game.doAction(new MagicChangeLifeAction(player,-1));
	                }
				});
				game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],1));
            }			
		}	
    };
    
    public static final MagicWhenLeavesPlayTrigger T2 = new MagicWhenLeavesPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent data) {
			return (data != permanent &&
					data.isArtifact(game)) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new MagicMayChoice(
                        		"You may pay {1}.",
                        		new MagicPayManaCostChoice(MagicManaCost.ONE),
                        		MagicTargetChoice.NEG_TARGET_PLAYER),
                        new Object[]{permanent.getController()},
                        this,
                        "You may$ pay {1}$. If you do, target player$ loses 1 life and you gain 1 life."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				event.processTargetPlayer(game,choiceResults,2,new MagicPlayerAction() {
	                public void doAction(final MagicPlayer player) {
	                    game.doAction(new MagicChangeLifeAction(player,-1));
	                }
				});
				game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],1));
            }			
		}	
    };
}
