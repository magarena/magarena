package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Angelic_Chorus {
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
			final MagicPlayer player = permanent.getController();
			return (otherPermanent.isCreature() && 
				otherPermanent.getController() == player) ?
				new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player,otherPermanent},
                        this,
                        player + " gains life equal to the toughness of " + otherPermanent + ".") :
               MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			// get toughness here so counters on the creature are considered
			final int toughness = ((MagicPermanent)data[1]).getToughness(game);
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],toughness));
		}
    };
}
