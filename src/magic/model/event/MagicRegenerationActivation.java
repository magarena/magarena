package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicRegenerateAction;
import magic.model.condition.MagicCondition;

import java.util.Arrays;

public class MagicRegenerationActivation extends MagicPermanentActivation {

    private static final MagicActivationHints hint = new MagicActivationHints(MagicTiming.Pump);

    private final MagicManaCost cost;

    public MagicRegenerationActivation(final MagicManaCost aCost) {
        super(hint, "Regen");
        cost = aCost;
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(source,cost),
            new MagicRegenerationConditionsEvent(source,this)
        );
    }

    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            this,
            "Regenerate SN."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new MagicRegenerateAction(event.getPermanent()));
    }
}
