package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBlocksTrigger;

public class Perimeter_Captain {
    public static final MagicWhenBlocksTrigger T = new MagicWhenBlocksTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
			final MagicPlayer player=permanent.getController();
            return (creature.getController() == player &&
                    creature.hasAbility(game,MagicAbility.Defender)) ?
                    		new MagicEvent(
                                    permanent,
                                    player,
                                    new MagicSimpleMayChoice(
                                            "You may gain 2 life.",
                                            MagicSimpleMayChoice.GAIN_LIFE,
                                            2,
                                            MagicSimpleMayChoice.DEFAULT_YES),
                                    new Object[]{player},
                                    this,
                                    player + " may$ gain 2 life.") :
                                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],2));
			}
		}
    };
}
