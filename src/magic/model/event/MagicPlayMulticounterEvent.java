package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.stack.MagicCardOnStack;

public class MagicPlayMulticounterEvent extends MagicSpellCardEvent {

    final MagicManaCost cost;

    public MagicPlayMulticounterEvent(final MagicManaCost aCost) {
        cost = aCost;
    }

    @Override
    public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
        return new MagicEvent(
            cardOnStack,
            new MagicKickerChoice(cost),
            this,
            "$Play SN. SN enters the battlefield " +
            "with a +1/+1 counter on it for each time it was kicked$"
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicCardOnStack cardOnStack = event.getCardOnStack();
        final MagicPlayCardFromStackAction action = new MagicPlayCardFromStackAction(cardOnStack);
        game.doAction(action);

        final int kicker = event.getKickerFromChosen();
        if (kicker > 0) {
            final MagicPermanent permanent = action.getPermanent();
            game.doAction(MagicChangeCountersAction.Enters(
                permanent,
                MagicCounterType.PlusOne,
                kicker
            ));
        }
    }
}
