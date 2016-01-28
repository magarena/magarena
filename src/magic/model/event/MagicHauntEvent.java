package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.AddTriggerAction;
import magic.model.action.MoveCardAction;
import magic.model.action.RemoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.ThisDiesTrigger;

public class MagicHauntEvent extends MagicEvent {

    public MagicHauntEvent(final MagicPermanent permanent, final MagicSourceEvent effect) {
        this(permanent.getCard(), permanent.getController(), effect);
    }

    public MagicHauntEvent(final MagicCardOnStack cardOnStack, final MagicSourceEvent effect) {
        this(cardOnStack.getCard(), cardOnStack.getController(), effect);
    }

    private MagicHauntEvent(final MagicCard card, final MagicPlayer player, final MagicSourceEvent effect) {
        super(
            card,
            player,
            MagicTargetChoice.TARGET_CREATURE,
            EVENT_ACTION(effect),
            "Exile SN haunting target creature$."
        );
    }

    private static final MagicEventAction EVENT_ACTION(final MagicSourceEvent effect) {
        return (final MagicGame game, final MagicEvent event) -> {
            final MagicCard card = event.getCard();
            if (card.isInGraveyard()) {
                event.processTargetPermanent(game, creatureToHaunt -> {
                    // only exile if valid target
                    game.doAction(new RemoveCardAction(card, MagicLocationType.Graveyard));
                    game.doAction(new MoveCardAction(card, MagicLocationType.Graveyard, MagicLocationType.Exile));
                    game.doAction(new AddTriggerAction(
                        creatureToHaunt,
                        ThisDiesTrigger.createDelayed(event.getSource(), event.getPlayer(), effect)
                    ));
                });
            }
        };
    }
}
