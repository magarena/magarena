package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicEvent;

public class MagicEchoTrigger extends MagicAtUpkeepTrigger {

	private MagicManaCost manaCost;
	
    public MagicEchoTrigger(final MagicManaCost manaCost) {
    	this.manaCost = manaCost;
	}
    
    @Override
	public MagicEvent executeTrigger(
			final MagicGame game,
			final MagicPermanent permanent,
			final MagicPlayer data) {
    	final MagicPlayer player = permanent.getController();
		return (player == data &&
			permanent.hasState(MagicPermanentState.MustPayEchoCost)) ?
            new MagicEvent(
                    permanent,
                    player,
                    new MagicMayChoice(
                            player + " may pay " + manaCost + ".",
                            new MagicPayManaCostChoice(manaCost)),
                    new Object[]{permanent},
                    this,
                    player + " may$ pay " + manaCost +
                    ". If he or she doesn't, sacrifice " + permanent + ".") :
		   MagicEvent.NONE;
	}

	@Override
	public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object data[],
            final Object[] choiceResults) {
		final MagicPermanent permanent = (MagicPermanent)data[0];
		if (MagicMayChoice.isYesChoice(choiceResults[0])) {
			game.doAction(new MagicChangeStateAction(
					permanent,
					MagicPermanentState.MustPayEchoCost,false));
		} else {
			game.doAction(new MagicSacrificeAction(permanent));
		}
	}
}

