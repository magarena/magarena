package magic.model.event;

import java.util.Collections;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.action.MagicPlayMod;
import magic.model.action.PlayCardFromStackAction;
import magic.model.action.PutItemOnStackAction;
import magic.model.action.RemoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicPumpTargetPicker;

public class MagicBestowActivation extends MagicHandCastActivation {

    final MagicManaCost cost;
    final public static MagicPlayAuraEvent BestowEvent = new MagicPlayAuraEvent(
        MagicTargetChoice.POS_TARGET_CREATURE,
        MagicTargetChoice.POS_CREATURE,
        MagicPumpTargetPicker.create()
    ) {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final boolean valid = event.processTargetPermanent(game, (final MagicPermanent creature) ->
                game.doAction(new PlayCardFromStackAction(
                    event.getCardOnStack(),
                    creature,
                    MagicPlayMod.BESTOWED
                ))
            );
            if (!valid) {
                game.doAction(new PlayCardFromStackAction(event.getCardOnStack()));
            }
        }
    };

    public MagicBestowActivation(final MagicManaCost aCost) {
        super(
            new MagicCondition[]{MagicCondition.CARD_CONDITION},
            new MagicActivationHints(MagicTiming.Aura, true),
            "Bestow"
        );
        cost = aCost;
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Collections.singletonList(MagicPayManaCostEvent.Cast(source, cost));
    }

    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            EVENT_ACTION,
            "Play SN."
        );
    }

    private final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {
        final MagicCard card = event.getCard();
        game.doAction(new RemoveCardAction(card,MagicLocationType.OwnersHand));

        final MagicCardOnStack cardOnStack=new MagicCardOnStack(
            card,
            BestowEvent,
            game.getPayedCost()
        ) {
            @Override
            public boolean hasType(final MagicType type) {
                if (type == MagicType.Creature) {
                    return false;
                } else {
                    return super.hasType(type);
                }
            }

            @Override
            public boolean hasSubType(final MagicSubType subType) {
                if (subType == MagicSubType.Aura) {
                    return true;
                } else if (MagicSubType.ALL_CREATURES.contains(subType)) {
                    return false;
                } else {
                    return super.hasSubType(subType);
                }
            }
        };

        game.doAction(new PutItemOnStackAction(cardOnStack));
    };
}
