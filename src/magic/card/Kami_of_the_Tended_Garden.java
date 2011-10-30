package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtUpkeepTrigger;
import magic.model.trigger.MagicSoulshiftTrigger;

public class Kami_of_the_Tended_Garden {
	public static final MagicSoulshiftTrigger T1 = new MagicSoulshiftTrigger(3);
	
	public static final MagicAtUpkeepTrigger T2 = new MagicAtUpkeepTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
			final MagicPlayer player = permanent.getController();
			return (player == data) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicMayChoice(
                                "You may pay {G}.",
                                new MagicPayManaCostChoice(MagicManaCost.GREEN)),
                            new Object[]{permanent},
                            this,
                            "You may$ pay {G}$. If you don't, sacrifice " + permanent + ".") :
                MagicEvent.NONE;
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isNoChoice(choiceResults[0])) {
				game.doAction(new MagicSacrificeAction((MagicPermanent)data[0]));
			}			
		}
    };
}
