package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.ChangeCountersAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicPTTargetFilter;
import magic.model.target.MagicTargetFilterFactory;
import magic.model.target.Operator;

public class MagicBolsterEvent extends MagicEvent {

    public static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) ->
        event.processTargetPermanent(game, (final MagicPermanent creature) ->
            game.doAction(new ChangeCountersAction(
                creature,
                MagicCounterType.PlusOne,
                event.getRefInt()
            ))
        );

    public MagicBolsterEvent(final MagicSource source, final MagicPlayer player, final int amount, final int minToughness) {
        super(
            source,
            player,
            new MagicTargetChoice(
                new MagicPTTargetFilter(
                    MagicTargetFilterFactory.CREATURE_YOU_CONTROL,
                    Operator.ANY,
                    0,
                    Operator.EQUAL,
                    minToughness
                ),
                "a creature with least toughness among creatures you control"
            ),
            amount,
            EVENT_ACTION,
            "PN puts RN +1/+1 counters on creature$ with least toughness among creatures he or she control."
        );
    }
}
