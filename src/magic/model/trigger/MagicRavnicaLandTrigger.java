package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;

public class MagicRavnicaLandTrigger extends MagicWhenComesIntoPlayTrigger {
	
	private static final MagicChoice RAVNICA_CHOICE = new MagicMayChoice("You may pay 2 life.");
	
	@Override
	public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer player) {
		if (player.getLife()>1) {
			return new MagicEvent(
                    permanent,
                    player,
                    RAVNICA_CHOICE,
                    new Object[]{player,permanent},
                    this,
                    "You may$ pay 2 life. If you don't, "+permanent.getName()+" enters the battlefield tapped.");
		}
		return null;
	}

	@Override
	public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] data,
            final Object[] choices) {
		if (MagicMayChoice.isYesChoice(choices[0])) {
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],-2));
		} else {
			game.doAction(new MagicTapAction((MagicPermanent)data[1],false));
		}
	}
	
	@Override
	public boolean usesStack() {
		return false;
	}
}
