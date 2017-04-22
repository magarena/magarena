package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MoveCardAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class CardIsDiscardedTrigger extends OtherPutIntoGraveyardTrigger {
    public CardIsDiscardedTrigger(final int priority) {
        super(priority);
    }

    public CardIsDiscardedTrigger() {}

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MoveCardAction act) {
        return (act.fromLocation == MagicLocationType.OwnersHand) ?
            getEvent(permanent, act.card) : MagicEvent.NONE;
    }

    protected abstract MagicEvent getEvent(final MagicPermanent source, final MagicCard card);

    public static CardIsDiscardedTrigger player(final MagicSourceEvent sourceEvent) {
        return new CardIsDiscardedTrigger() {
            @Override
            public MagicEvent getEvent(final MagicPermanent source, final MagicCard card) {
                return sourceEvent.getTriggerEvent(source, card.getOwner());
            }
        };
    }

    public static CardIsDiscardedTrigger opponent(final MagicSourceEvent sourceEvent) {
        return new CardIsDiscardedTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MoveCardAction act) {
                return super.accept(permanent, act) && permanent.isEnemy(act.card);
            }

            @Override
            public MagicEvent getEvent(final MagicPermanent source, final MagicCard card) {
                return sourceEvent.getTriggerEvent(source, card.getOwner());
            }
        };
    }

    public static CardIsDiscardedTrigger you(final MagicSourceEvent sourceEvent) {
        return new CardIsDiscardedTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MoveCardAction act) {
                return super.accept(permanent, act) && permanent.isFriend(act.card);
            }

            @Override
            public MagicEvent getEvent(final MagicPermanent source, final MagicCard card) {
                return sourceEvent.getTriggerEvent(source, card.getOwner());
            }
        };
    }
}
