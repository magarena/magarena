package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTapTargetPicker;
import magic.model.trigger.MagicFlankingTrigger;
import magic.model.trigger.MagicWhenAttacksTrigger;

public class Sidar_Jabari {
    public static final MagicFlankingTrigger T1 = new MagicFlankingTrigger();
    
    public static final MagicWhenAttacksTrigger T2 = new MagicWhenAttacksTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPlayer player = permanent.getController();
			return (permanent == creature) ?
                new MagicEvent(
                        permanent,
                        player,
                        MagicTargetChoice.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                        new MagicTapTargetPicker(true,false),
                        MagicEvent.NO_DATA,
                        this,
                        "Tap target creature defending player controls."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicTapAction(creature,true));
                }
            });
		}		
    };
}
