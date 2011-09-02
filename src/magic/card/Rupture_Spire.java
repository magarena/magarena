package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTappedIntoPlayTrigger;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Rupture_Spire {
    public static final MagicTappedIntoPlayTrigger T =new MagicTappedIntoPlayTrigger();
    
    public static final MagicWhenComesIntoPlayTrigger T2 = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
			return new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new MagicMayChoice(
                        "You may pay {1}.",
                        new MagicPayManaCostChoice(MagicManaCost.ONE)),
                    new Object[]{permanent},
                    this,
                    "You may$ pay {1}. If you don't, sacrifice " + permanent + ".");
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
