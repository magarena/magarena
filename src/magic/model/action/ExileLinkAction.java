package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;

public class ExileLinkAction extends MagicAction {

    public final MagicPermanent source;
    public final MagicPermanent permanent;
    private final MagicCard card;
    private final MagicLocationType location;

    public ExileLinkAction(final MagicPermanent source,final MagicCard card,final MagicLocationType location) {
        this.source = source;
        this.permanent = MagicPermanent.NONE;
        this.card = card;
        this.location = location;
    }

    public ExileLinkAction(final MagicPermanent source,final MagicPermanent permanent) {
        this.source = source;
        this.permanent = permanent;
        this.card = permanent.getCard();
        this.location = MagicLocationType.Exile;
    }

    @Override
    public void doAction(final MagicGame game) {
        if (permanent != MagicPermanent.NONE) {
            game.doAction(new MagicRemoveFromPlayAction(permanent,location));
        } else {
            game.doAction(new MagicRemoveCardAction(card,location));
            game.doAction(new MagicMoveCardAction(card,location,MagicLocationType.Exile));
        }

        source.addExiledCard(card);
    }

    @Override
    public void undoAction(final MagicGame game) {
        source.removeExiledCard(card);
    }
}
