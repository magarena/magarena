package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.TapAction;
import magic.model.condition.MagicCondition;

public class MagicTapEnchantedEvent extends MagicEvent {

    private static final MagicCondition cond = MagicCondition.ENCHANTED_IS_UNTAPPED_CONDITION;

    public MagicTapEnchantedEvent(final MagicPermanent permanent) {
        super(
            permanent,
            EVENT_ACTION,
            "Tap SN."
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) ->
        game.doAction(new TapAction(event.getPermanent().getEnchantedPermanent()));

    @Override
    public boolean isSatisfied() {
        return cond.accept(getSource()) && super.isSatisfied();
    }
}
