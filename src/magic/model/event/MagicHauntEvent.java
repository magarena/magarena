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
    static MagicSourceEvent sourceEvent;

    public MagicHauntEvent(final MagicPermanent permanent, final MagicSourceEvent aSourceEvent) {
        this(permanent.getCard(), permanent.getController(), aSourceEvent);
    }

    public MagicHauntEvent(final MagicCardOnStack cardOnStack, final MagicSourceEvent aSourceEvent) {
        this(cardOnStack.getCard(), cardOnStack.getController(), aSourceEvent);
    }

    private MagicHauntEvent(final MagicCard card, final MagicPlayer player, final MagicSourceEvent aSourceEvent) {
        super(
            card,
            player,
            MagicTargetChoice.TARGET_CREATURE,
            EVENT_ACTION,
            "Exile SN haunting target creature$."
        );
        sourceEvent = aSourceEvent;
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {
        final MagicCard card = event.getCard();
        if (card.isInGraveyard()) {
            event.processTargetPermanent(game, creatureToHaunt -> {
                // only exile if valid target
                game.doAction(new RemoveCardAction(card, MagicLocationType.Graveyard));
                game.doAction(new MoveCardAction(card, MagicLocationType.Graveyard, MagicLocationType.Exile));
                game.doAction(new AddTriggerAction(
                    creatureToHaunt,
                    ThisDiesTrigger.createDelayed(event.getSource(), event.getPlayer(), sourceEvent)
                ));
            });
        }
    };
}
