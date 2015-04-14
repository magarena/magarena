package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MoveCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.event.MagicEvent;

public class MagicFromGraveyardToLibraryTrigger extends MagicWhenPutIntoGraveyardTrigger {

    private static final MagicFromGraveyardToLibraryTrigger INSTANCE = new MagicFromGraveyardToLibraryTrigger();

    private MagicFromGraveyardToLibraryTrigger() {}

    public static final MagicFromGraveyardToLibraryTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MoveCardAction act) {
        final MagicCard card = act.card;
        return card.isToken() == false ?
            new MagicEvent(
                card,
                this,
                "Shuffle SN into its owners library."
            ):
            MagicEvent.NONE;
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicCard card = event.getCard();
        if (card.isInGraveyard()) {
            game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
            game.doAction(new MoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersLibrary));
        }
    }
}
