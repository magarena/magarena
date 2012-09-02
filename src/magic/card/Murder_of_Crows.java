package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherPutIntoGraveyardFromPlayTrigger;

public class Murder_of_Crows {
    public static final MagicWhenOtherPutIntoGraveyardFromPlayTrigger T = new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (permanent != otherPermanent && otherPermanent.isCreature()) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new MagicSimpleMayChoice(
                        "You may draw a card. If you do, discard a card.",
                        MagicSimpleMayChoice.DRAW_CARDS,
                        1,
                        MagicSimpleMayChoice.DEFAULT_NONE
                    ),
                    this,
                    "You may$ draw a card. If you do, discard a card."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicChoice.isYesChoice(choiceResults[0])) {
                final MagicPlayer player=event.getPlayer();
                game.doAction(new MagicDrawAction(player,1));
                game.addEvent(new MagicDiscardEvent(event.getSource(),player,1,false));
            }
        }
    };
}
