package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.TapAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicEvent;

public class EntersTappedUnlessTrigger extends MagicWhenComesIntoPlayTrigger {

    private final MagicCondition condition;

    public EntersTappedUnlessTrigger(final MagicCondition condition) {
        super(MagicTrigger.REPLACEMENT);
        this.condition = condition;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
        return condition.accept(permanent) ?
            new MagicEvent(
                permanent,
                this,
                "SN enters the battlefield tapped."
            ) :
            MagicEvent.NONE;
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(TapAction.Enters(event.getPermanent()));
    }
}
