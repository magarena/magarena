package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicDrawAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Soul_of_the_Harvest {
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent otherPermanent) {
            return (otherPermanent != permanent &&
                    otherPermanent.isNonToken() &&
                    otherPermanent.isCreature() &&
                    otherPermanent.getController() == permanent.getController()) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new MagicSimpleMayChoice(
                            "You may draw a card.",
                            MagicSimpleMayChoice.DRAW_CARDS,
                            1,
                            MagicSimpleMayChoice.DEFAULT_NONE),
                        this,
                        "You may$ draw a card."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicChoice.isYesChoice(choiceResults[0])) {
                game.doAction(new MagicDrawAction(event.getPlayer(),1));
            }            
        }        
    };
}
