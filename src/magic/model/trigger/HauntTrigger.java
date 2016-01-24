package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.AddTriggerAction;
import magic.model.action.MoveCardAction;
import magic.model.action.RemoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class HauntTrigger extends OtherDiesTrigger {

    private HauntTrigger() {
    }

    public static final HauntTrigger create(final MagicSourceEvent sourceEvent) {
        return new HauntTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
                return new MagicEvent(
                    permanent,
                    MagicTargetChoice.TARGET_CREATURE,
                    this,
                    "Exile SN haunting target creature$."
                );
            }

            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                final MagicCard card = event.getPermanent().getCard();
                if (card.isInGraveyard()) {
                    event.processTargetPermanent(game, creatureToHaunt -> {
                            game.doAction(new MoveCardAction(card, MagicLocationType.Graveyard, MagicLocationType.Exile)); //Only exiled if valid targt
                            game.doAction(new RemoveCardAction(card, MagicLocationType.Graveyard));
                            game.doAction(new AddTriggerAction(
                                creatureToHaunt,
                                ThisDiesTrigger.create(sourceEvent)
                            ));
                        }
                    );
                }
            }
        };
    }

    @Override
    public boolean accept(final MagicPermanent source, final MagicPermanent died) {
        return source.equals(died);
    }

}
