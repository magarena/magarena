package magic.model.event;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicGainAbilityAction;
import magic.model.condition.MagicCondition;

import java.util.Arrays;

public class MagicGainActivation extends MagicPermanentActivation {

    private final MagicManaCost cost;
    private final MagicAbility ability;

    public MagicGainActivation(final MagicManaCost cost,final MagicAbility ability,final MagicActivationHints hints,final String txt) {
        super(hints,txt);
        this.cost=cost;
        this.ability=ability;
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(source,cost),
            new MagicPlayAbilityEvent(source)
        );
    }

    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            this,
            "SN gains "+ability+" until end of turn."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new MagicGainAbilityAction(event.getPermanent(),ability));
    }
}
