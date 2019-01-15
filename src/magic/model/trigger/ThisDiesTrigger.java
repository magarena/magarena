package magic.model.trigger;

import magic.data.CardDefinitions;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.PlayTokensAction;
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

    public static ThisDiesTrigger Afterlife(final int n) {
        return new ThisDiesTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
                return new MagicEvent(
                    permanent,
                    n,
                    this,
                    "PN creates RN 1/1 white and black Spirit creature tokens with flying."
                );
            }

            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new PlayTokensAction(
                    event.getPlayer(),
                    CardDefinitions.getToken("1/1 white and black Spirit creature token with flying"),
                    event.getRefInt()
                ));
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
