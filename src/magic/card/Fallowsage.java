package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesTappedTrigger;

public class Fallowsage {
    public static final MagicWhenBecomesTappedTrigger T = new MagicWhenBecomesTappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
            final MagicPlayer player = permanent.getController();
            return (permanent == data) ?
                    new MagicEvent(
                            permanent,
                            player,
                            new MagicSimpleMayChoice(
                                player + " may draw a card.",
                                MagicSimpleMayChoice.DRAW_CARDS,
                                1,
                                MagicSimpleMayChoice.DEFAULT_NONE),
                            new Object[]{player},
                            this,
                            player + " may$ draw a card.") :
                        MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicChoice.isYesChoice(choiceResults[0])) {
                game.doAction(new MagicDrawAction((MagicPlayer)data[0],1));
            }
        }
    };
}
