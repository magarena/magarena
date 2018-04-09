package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.ChangeCountersAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicPTTargetFilter;
import magic.model.target.MagicTargetFilterFactory;
import magic.model.target.Operator;

public class MagicBolsterEvent extends MagicEvent {

    public static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) ->
        event.processTargetPermanent(game, (final MagicPermanent creature) ->
            game.doAction(new ChangeCountersAction(
                event.getPlayer(),
                creature,
                MagicCounterType.PlusOne,
                event.getRefInt()
            ))
        );

    public MagicBolsterEvent(final MagicEvent event, final int amount) {
        super(
            event.getSource(),
            event.getPlayer(),
            new MagicTargetChoice(
                new MagicPTTargetFilter(
                    MagicTargetFilterFactory.CREATURE_YOU_CONTROL,
                    Operator.ANY,
                    0,
                    Operator.EQUAL,
                    computeMinToughness(event)
                ),
                "a creature with least toughness among creatures you control"
            ),
            amount,
            EVENT_ACTION,
            "PN puts RN +1/+1 counters on creature$ with least toughness among creatures he or she control."
        );
    }

    private static int computeMinToughness(final MagicEvent event) {
        int minToughness = Integer.MAX_VALUE;
        for (final MagicPermanent it : MagicTargetFilterFactory.CREATURE_YOU_CONTROL.filter(event)) {
            minToughness = Math.min(minToughness, it.getToughnessValue());
        }
        return minToughness;
    }
}
