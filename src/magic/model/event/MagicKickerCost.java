package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicSource;
import magic.model.action.SetKickerAction;
import magic.model.choice.MagicMayChoice;

public class MagicKickerCost extends MagicAdditionalCost implements MagicEventAction {

    final MagicMatchedCostEvent cost;
    final String name;

    private MagicKickerCost(final MagicMatchedCostEvent aCost, final String aName) {
        cost = aCost;
        name = aName;
    }

    public MagicKickerCost(final MagicMatchedCostEvent aCost) {
        this(aCost, "kicker");
    }

    public static MagicKickerCost Buyback(final MagicMatchedCostEvent aCost) {
        return new MagicKickerCost(aCost, "buyback");
    }

    public static MagicKickerCost Entwine(final MagicMatchedCostEvent aCost) {
        return new MagicKickerCost(aCost, "entwine");
    }

    public static MagicKickerCost Conspire(final MagicMatchedCostEvent aCost) {
        return new MagicKickerCost(aCost, "conspire");
    }

    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            new MagicMayChoice(
                "Pay the " + name + " cost?",
                cost
            ),
            this,
            "PN may$ pay the " + name + " cost."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicEvent costEvent = cost.getEvent(event.getSource());
        if (event.isYes() && costEvent.isSatisfied()) {
            game.addFirstEvent(costEvent);
            game.doAction(new SetKickerAction(1));
        }
    }
}
