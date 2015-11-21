package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.ChangeTurnPTAction;
import magic.model.event.MagicEvent;

public class BecomesBlockedPumpTrigger extends MagicWhenSelfBecomesBlockedTrigger {

    private final int amountPower;
    private final int amountToughness;

    public BecomesBlockedPumpTrigger(final int amountPower, final int amountToughness) {
        this.amountPower = amountPower;
        this.amountToughness = amountToughness;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent blocked) {
        return new MagicEvent(
            permanent,
            this,
            "SN gets " + getString(amountPower) + "/" + getString(amountToughness) + " until end of turn."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new ChangeTurnPTAction(
            event.getPermanent(),
            amountPower,
            amountToughness
        ));
    }

    private String getString(final int pt) {
        return pt >= 0 ?
                "+" + pt :
                Integer.toString(pt);
    }
}

