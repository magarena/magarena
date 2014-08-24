package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicSource;
import magic.model.action.MagicSetKickerAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;

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

    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            new MagicMayChoice(
                "Pay the " + name + " cost?"
            ),
            this,
            "PN may$ pay the " + name + "cost."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicEvent costEvent = cost.getEvent(event.getSource());
        if (event.isYes() & costEvent.hasOptions(game)) {
            game.addFirstEvent(costEvent);
            game.doAction(new MagicSetKickerAction(1));
        }
    }
}
