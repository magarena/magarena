package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicLocationType;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.stack.MagicAbilityOnStack;
import magic.model.target.MagicCardFilterImpl;
import magic.model.target.MagicTargetType;

import java.util.Arrays;

public class MagicTransmuteActivation extends MagicCardActivation {
    final MagicManaCost cost;
    
    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchToLocationEvent(
                event, 
                getTransmuteChoice(event.getRefInt()),
                MagicLocationType.OwnersHand
            ));
        }
    };
        
    private static MagicTargetChoice getTransmuteChoice(final int cmc) {
        final MagicCardFilterImpl transmuteFilter = new MagicCardFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                return target.getConvertedCost() == cmc;
            }
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
    public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(source, cost),
            new MagicDiscardSelfEvent(source)
        );
    }
    
    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            this,
            "Transmute SN."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicAbilityOnStack abilityOnStack = new MagicAbilityOnStack(
            this,
            new MagicEvent(
                event.getSource(),
                event.getCard().getConvertedCost(),
                EVENT_ACTION,
                "PN searches his or her library for a card with converted mana cost of RN. " +
                "Then PN shuffles his or her library."
            )
        );
        game.doAction(new MagicPutItemOnStackAction(abilityOnStack));
    }
}
