package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicPlayMod;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.condition.MagicCondition;
import magic.model.stack.MagicCardOnStack;

import java.util.Arrays;

public class MagicMorphCastActivation extends MagicCardActivation {

    public MagicMorphCastActivation() {
        super(
            new MagicCondition[]{
                MagicCondition.CARD_CONDITION,
            },
            new MagicActivationHints(MagicTiming.Pump, true),
            "Morph"
        );
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(
                source,
                MagicManaCost.create("{3}")
            )
        );
    }
    
    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            EVENT_ACTION,
            "Play a face down card."
        );
    }
    
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new MagicRemoveCardAction(event.getCard(), MagicLocationType.OwnersHand));
        game.doAction(new MagicPlayCardAction(event.getCard(),event.getPlayer(),MagicPlayMod.FACE_DOWN));
    }
    
    private final MagicEventAction EVENT_ACTION = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard card = event.getCard();
            game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
                
            final MagicCardOnStack cardOnStack=new MagicCardOnStack(
                card,
                event.getPlayer(),
                game.getPayedCost()
            ) {
                @Override
                public MagicCardDefinition getCardDefinition() {
                    return MagicCardDefinition.FACE_DOWN;
                }
                @Override
                public boolean hasColor(final MagicColor color) {
                    return false;
                }
                @Override
                public boolean hasSubType(final MagicSubType subType) {
                    return false;
                }
                @Override
                public boolean hasType(final MagicType type) {
                    if (type==MagicType.Creature) {
                        return true;
                    } else {
                        return false;
                    }
                }
                @Override
                public String getName() {
                    return "a face down card";
                }
            };

            game.doAction(new MagicPutItemOnStackAction(cardOnStack));
        }
    };
    
}
