package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MoveCardAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class MagicWhenDiscardedTrigger extends OtherPutIntoGraveyardTrigger {
    public MagicWhenDiscardedTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenDiscardedTrigger() {}

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MoveCardAction act) {
        return (act.fromLocation == MagicLocationType.OwnersHand) ?
            getEvent(permanent, act.card) : MagicEvent.NONE;
    }

    protected abstract MagicEvent getEvent(final MagicPermanent source, final MagicCard card);

    public static MagicWhenDiscardedTrigger player(final MagicSourceEvent sourceEvent) {
        return new MagicWhenDiscardedTrigger() {
            @Override
            public MagicEvent getEvent(final MagicPermanent source, final MagicCard card) {
                return sourceEvent.getEvent(source, card.getOwner());
            }
        };
    }

    public static MagicWhenDiscardedTrigger opponent(final MagicSourceEvent sourceEvent) {
        return new MagicWhenDiscardedTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MoveCardAction act) {
                return super.accept(permanent, act) && permanent.isEnemy(act.card);
            }

            @Override
            public MagicEvent getEvent(final MagicPermanent source, final MagicCard card) {
                return sourceEvent.getEvent(source, card.getOwner());
            }
        };
    }
}
