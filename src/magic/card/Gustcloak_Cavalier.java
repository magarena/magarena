package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicRemoveFromCombatAction;
import magic.model.action.MagicTapAction;
import magic.model.action.MagicUntapAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTapTargetPicker;
import magic.model.trigger.MagicFlankingTrigger;
import magic.model.trigger.MagicWhenAttacksTrigger;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;

public class Gustcloak_Cavalier {
	public static final MagicFlankingTrigger T1 = new MagicFlankingTrigger();
    
    public static final MagicWhenAttacksTrigger T2 = new MagicWhenAttacksTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPlayer player = permanent.getController();
			return (permanent == creature) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicMayChoice(player + " may tap target creature.",
                        		MagicTargetChoice.NEG_TARGET_CREATURE),
                        new MagicTapTargetPicker(true,false),
                        MagicEvent.NO_DATA,
                        this,
                        player + " may$ tap target creature$."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        if (!creature.isTapped()) {
                            game.doAction(new MagicTapAction(creature,true));
                        }
                    }
                });
			}
		}		
    };
    
    public static final MagicWhenBecomesBlockedTrigger T3 = new MagicWhenBecomesBlockedTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
			final MagicPlayer player = permanent.getController();
            return (creature == permanent) ?
            		new MagicEvent(
                            permanent,
                            player,
                            new MagicMayChoice(player + " may untap " + permanent +
                            		" and remove it from combat."),
                            new Object[]{permanent},
                            this, 
                            player + " may$ untap " + permanent +
                    		" and remove it from combat.") :
            MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicPermanent permanent = (MagicPermanent)data[0];
				game.doAction(new MagicUntapAction(permanent));
				game.doAction(new MagicRemoveFromCombatAction(permanent));
			}
		}
    };
}
