package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.trigger.MagicAtUpkeepTrigger;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Avalanche_Riders {
    public static final MagicAtUpkeepTrigger T1 = new MagicAtUpkeepTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
			final MagicPlayer player = permanent.getController();
			return (player == data && 
					permanent.hasState(MagicPermanentState.MustPayEchoCost)) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicMayChoice(
                                player + " may pay {3}{R}.",
                                new MagicPayManaCostChoice(MagicManaCost.THREE_RED)),
                        new Object[]{permanent},
                        this,
                        player + " may pay {3}{R}. " +
                        "If he or she doesn't, sacrifice " + permanent + "."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPermanent permanent = (MagicPermanent)data[0];
			if (MagicMayChoice.isNoChoice(choiceResults[0])) {
				game.doAction(new MagicSacrificeAction(permanent));
			} else {
				game.doAction(new MagicChangeStateAction(
						permanent,
						MagicPermanentState.MustPayEchoCost,false));
			}
		}		
    };
    
    public static final MagicWhenComesIntoPlayTrigger T2 = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
			return new MagicEvent(
                    permanent,
                    player,
                    MagicTargetChoice.TARGET_LAND,
                    new MagicDestroyTargetPicker(false),
                    MagicEvent.NO_DATA,
                    this,
                    "Destroy target land$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new MagicDestroyAction(permanent));
                }
			});
		}
    };
}
