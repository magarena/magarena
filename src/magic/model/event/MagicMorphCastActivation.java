package magic.model.event;


import java.util.Arrays;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicAbility;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.MagicPayedCost;
import magic.model.condition.MagicCondition;
import magic.model.stack.MagicCardOnStack;
import magic.model.action.MagicPlayMod;
import magic.model.action.PutItemOnStackAction;
import magic.model.action.RemoveCardAction;
import magic.model.action.PlayCardFromStackAction;

public class MagicMorphCastActivation extends MagicCardActivation {

    public static final MagicMorphCastActivation Morph = new MagicMorphCastActivation("Morph");
    public static final MagicMorphCastActivation Megamorph = new MagicMorphCastActivation("Megamorph");

    private MagicMorphCastActivation(final String name) {
        super(
            new MagicCondition[]{
                MagicCondition.SORCERY_CONDITION
            },
            new MagicActivationHints(MagicTiming.Main, true),
            name
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
            "Play a face-down card."
        );
    }
    
    private final MagicEventAction EVENT_ACTION = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard card = event.getCard();
            game.doAction(new RemoveCardAction(card,MagicLocationType.OwnersHand));
                
            final MagicCardOnStack cardOnStack=new MagicCardOnStack(
                card,
                MagicMorphCastActivation.this,
                game.getPayedCost()
            ) {
                @Override
                public boolean hasColor(final MagicColor color) {
                    return MagicCardDefinition.MORPH.hasColor(color);
                }
                @Override
                public boolean hasAbility(final MagicAbility ability) {
                    return MagicCardDefinition.MORPH.hasAbility(ability);
                }
                @Override
                public boolean hasSubType(final MagicSubType subType) {
                    return MagicCardDefinition.MORPH.hasSubType(subType);
                }
                @Override
                public boolean hasType(final MagicType type) {
                    return MagicCardDefinition.MORPH.hasType(type);
                }
                @Override
                public boolean canBeCountered() {
                    return MagicCardDefinition.MORPH.hasAbility(MagicAbility.CannotBeCountered) == false;
                }
                @Override
                public String getName() {
                    return MagicCardDefinition.MORPH.getName();
                }
            };

            game.doAction(new PutItemOnStackAction(cardOnStack));
        }
    };
    
    @Override
    public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
        return new MagicEvent(
            cardOnStack,
            this,
            "Put a face-down creature onto the battlefield."
        );
    }
    
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new PlayCardFromStackAction(event.getCardOnStack(), MagicPlayMod.MORPH));
    }
}
