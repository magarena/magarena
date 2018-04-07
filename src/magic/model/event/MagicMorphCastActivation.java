package magic.model.event;

import magic.data.CardDefinitions;
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
import magic.model.MagicMessage;
import magic.model.condition.MagicCondition;
import magic.model.stack.MagicCardOnStack;
import magic.model.action.MagicPlayMod;
import magic.model.action.PutItemOnStackAction;
import magic.model.action.RemoveCardAction;
import magic.model.action.PlayCardFromStackAction;

import java.util.Arrays;

public class MagicMorphCastActivation extends MagicHandCastActivation {

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
        final MagicCardOnStack morphSpell = genMorphSpell(source);
        final MagicManaCost modCost = source.getGame().modCost(
            MagicCard.createTokenCard(morphSpell, morphSpell.getController()),
            MagicManaCost.create("{3}")
        );
        return Arrays.asList(
            new MagicPayManaCostEvent(
                morphSpell,
                modCost
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

    private MagicCardOnStack genMorphSpell(final MagicCard card) {
        final MagicCardDefinition morph = CardDefinitions.getCard("Morph");
        return new MagicCardOnStack(
            card,
            MagicMorphCastActivation.this,
            card.getGame().getPayedCost()
        ) {
            @Override
            public MagicCardDefinition getCardDefinition() {
                return morph;
            }
            @Override
            public boolean hasColor(final MagicColor color) {
                return morph.hasColor(color);
            }
            @Override
            public boolean hasAbility(final MagicAbility ability) {
                return morph.hasAbility(ability);
            }
            @Override
            public boolean hasSubType(final MagicSubType subType) {
                return morph.hasSubType(subType);
            }
            @Override
            public boolean hasType(final MagicType type) {
                return morph.hasType(type);
            }
            @Override
            public boolean canBeCountered() {
                return !hasAbility(MagicAbility.CannotBeCountered);
            }
            @Override
            public int getConvertedCost() {
                return 0;
            }
            @Override
            public String getName() {
                return "Face-down creature spell #" + (getId() % 1000);
            }
            @Override
            public boolean isFaceDown() {
                return true;
            }
        };
    }

    private final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {
        final MagicCard card = event.getCard();
        game.doAction(new RemoveCardAction(card,MagicLocationType.OwnersHand));
        game.doAction(new PutItemOnStackAction(genMorphSpell(card)));
    };

    @Override
    public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
        return new MagicEvent(
            cardOnStack,
            this,
            "Put " + MagicMessage.getCardToken("face-down creature", cardOnStack.getCard()) + " onto the battlefield."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicCardOnStack spell = event.getCardOnStack();
        final MagicCardDefinition carddef = spell.getSource().getCardDefinition();
        game.doAction(new PlayCardFromStackAction(spell, carddef, MagicPlayMod.MORPH));
    }
}
