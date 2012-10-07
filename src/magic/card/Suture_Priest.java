package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Suture_Priest {
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPermanent other) {
            return other != permanent && other.isCreature();
        }
        
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent other) {
            return permanent.isFriend(other) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.GAIN_LIFE,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES
                    ), 
                    MAY_GAIN_ONE_LIFE,
                    "PN may$ gain 1 life."
                ):
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.OPPONENT_LOSE_LIFE,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES
                    ),
                    MAY_LOSE_ONE_LIFE,
                    "PN may$ have " + other.getController() + " lose 1 life."
                );
        }
    };
        
    private static final MagicEventAction MAY_GAIN_ONE_LIFE = new MagicEventAction() {
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                game.doAction(new MagicChangeLifeAction(event.getPlayer(), 1));
            }
        }        
    };
    
    private static final MagicEventAction MAY_LOSE_ONE_LIFE = new MagicEventAction() {
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                game.doAction(new MagicChangeLifeAction(event.getPlayer().getOpponent(), -1));
            }
        }        
    };
}
