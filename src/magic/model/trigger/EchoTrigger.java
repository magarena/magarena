package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.action.ChangeStateAction;
import magic.model.action.SacrificeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicMatchedCostEvent;

public class EchoTrigger extends AtUpkeepTrigger {

    private final MagicMatchedCostEvent matchedCost;

    public EchoTrigger(final MagicMatchedCostEvent aMatchedCost) {
        matchedCost = aMatchedCost;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
        return (permanent.isController(upkeepPlayer) &&
                permanent.hasState(MagicPermanentState.MustPayEchoCost)) ?
            new MagicEvent(
                permanent,
                new MagicMayChoice("Pay the echo cost?"),
                this,
                "PN may$ pay the echo cost. If he or she doesn't, sacrifice SN."
            ):
            MagicEvent.NONE;
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicPermanent permanent = event.getPermanent();
        final MagicEvent cost = matchedCost.getEvent(permanent);
        if (event.isYes() && cost.isSatisfied()) {
            game.addEvent(cost);
            game.doAction(ChangeStateAction.Clear(
                permanent,
                MagicPermanentState.MustPayEchoCost
            ));
        } else {
            game.doAction(new SacrificeAction(permanent));
        }
    }
}
