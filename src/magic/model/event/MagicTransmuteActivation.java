package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicSource;
import magic.model.MagicManaCost;
import magic.model.MagicPlayer;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.target.MagicCardFilterImpl;
import magic.model.target.MagicTargetType;

import java.util.Arrays;

public class MagicTransmuteActivation extends MagicCardAbilityActivation {

    final MagicManaCost cost;

    private static MagicTargetChoice getTransmuteChoice(final int cmc) {
        final MagicCardFilterImpl transmuteFilter = new MagicCardFilterImpl() {
            @Override
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
                return target.getConvertedCost() == cmc;
            }
            @Override
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == MagicTargetType.Library;
            }
        };
        return new MagicTargetChoice(transmuteFilter,"a card with converted mana cost of " + cmc);
    }

    public MagicTransmuteActivation(final MagicManaCost aCost) {
        super(
            new MagicCondition[]{MagicCondition.SORCERY_CONDITION},
            new MagicActivationHints(MagicTiming.Main,true),
            "Transmute"
        );
        cost = aCost;
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(source, cost),
            new MagicDiscardSelfEvent(source)
        );
    }

    @Override
    public MagicEvent getCardEvent(final MagicCard card, final MagicPayedCost payedCost) {
        return new MagicEvent(
            card,
            card.getConvertedCost(),
            this,
            "PN searches his or her library for a card with converted mana cost of RN. " +
            "Then PN shuffles his or her library."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.addEvent(new MagicSearchToLocationEvent(
            event,
            getTransmuteChoice(event.getRefInt()),
            MagicLocationType.OwnersHand
        ));
    }
}
