package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MoveCardAction;
import magic.model.action.ShiftCardAction;
import magic.model.event.MagicEvent;

public class FromGraveyardIntoLibraryTrigger extends ThisPutIntoGraveyardTrigger {

    private static final FromGraveyardIntoLibraryTrigger INSTANCE = new FromGraveyardIntoLibraryTrigger();

    private FromGraveyardIntoLibraryTrigger() {}

    public static final FromGraveyardIntoLibraryTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MoveCardAction act) {
        final MagicCard card = act.card;
        return !card.isToken() ?
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
            game.doAction(new ShiftCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersLibrary));
        }
    }
}
