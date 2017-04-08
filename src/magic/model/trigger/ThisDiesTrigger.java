package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.event.MagicHauntEvent;
import magic.model.event.MagicSourceEvent;

public abstract class ThisDiesTrigger extends OtherDiesTrigger {
    public static ThisDiesTrigger create(final MagicSourceEvent sourceEvent) {
        return new ThisDiesTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent source, final MagicPermanent died) {
                return sourceEvent.getTriggerEvent(source);
            }
        };
    }

    public static ThisDiesTrigger createHaunt(final MagicSourceEvent sourceEvent) {
        return new ThisDiesTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
                return new MagicHauntEvent(permanent, sourceEvent);
            }
        };
    }

    public static ThisDiesTrigger createDelayed(final MagicCard staleCard, final MagicPlayer stalePlayer, final MagicSourceEvent sourceEvent) {
        return new ThisDiesTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent source, final MagicPermanent died) {
                final MagicCard mappedCard = staleCard.getOwner().map(game).getExile().getCard(staleCard.getId());
                return mappedCard.isInExile() ? sourceEvent.getTriggerEvent(mappedCard) : MagicEvent.NONE;
            }
        };
    }

    public ThisDiesTrigger(final int priority) {
        super(priority);
    }

    public ThisDiesTrigger() {}

    @Override
    public boolean accept(final MagicPermanent source, final MagicPermanent died) {
        return source == died;
    }
}
