package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicDrawAction;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;

//When C is put into a graveyard from the battlefield, you may or must draw a card.
public class MagicDieDrawCardTrigger extends MagicWhenDiesTrigger {

    private final boolean mustDraw;

    public MagicDieDrawCardTrigger(final boolean mandatory) {
        this.mustDraw = mandatory;
    }

    @Override
    public MagicEvent getEvent(final MagicPermanent permanent) {
        return mustDraw ?
            new MagicEvent(
                permanent,
                this,
                "PN draws a card."
            ):
            new MagicEvent(
                permanent,
                new MagicSimpleMayChoice(
                    MagicSimpleMayChoice.DRAW_CARDS,
                    1,
                    MagicSimpleMayChoice.DEFAULT_NONE
                ),
                this,
                "PN may$ draw a card."
            );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        if (mustDraw || event.isYes()) {
            game.doAction(new MagicDrawAction(event.getPlayer()));
        }
    }
}
