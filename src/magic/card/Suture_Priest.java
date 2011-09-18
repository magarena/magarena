package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Suture_Priest {
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final MagicPlayer player=permanent.getController();
            final MagicPlayer controller=otherPermanent.getController();
            final boolean same=controller==player;
			return (otherPermanent!=permanent&&otherPermanent.isCreature(game)) ?
                new MagicEvent(
                    permanent,
                    player,
                    same ?
                    		new MagicSimpleMayChoice(
                    				"You may gain 1 life.",
                    				MagicSimpleMayChoice.GAIN_LIFE,
                    				1,
                    				MagicSimpleMayChoice.DEFAULT_YES) :
                    		new MagicSimpleMayChoice(
                    				"You may have your opponent lose 1 life.",
                    				MagicSimpleMayChoice.OPPONENT_LOSE_LIFE,
                    				1,
                    				MagicSimpleMayChoice.DEFAULT_YES),
                    new Object[]{controller,same?1:-1},
                    this,
                    same ?
                    		controller + " may$ gain 1 life." :
                    		player + " may$ have " + controller + " lose 1 life.") :
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],(Integer)data[1]));
		}		
    };
}
