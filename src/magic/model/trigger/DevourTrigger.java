package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.ChangeCountersAction;
import magic.model.action.SacrificeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicSacrificeTargetPicker;

public class DevourTrigger extends EntersBattlefieldTrigger {

    private final int amount;

    public DevourTrigger(final int aAmount) {
        super(MagicTrigger.REPLACEMENT);
        amount = aAmount;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent perm, final MagicPayedCost payedCost) {
        return new MagicEvent(
            perm,
            new MagicMayChoice(
                "Sacrifice a creature to " + perm + "?",
                MagicTargetChoice.ANOTHER_CREATURE_YOU_CONTROL
            ),
            MagicSacrificeTargetPicker.create(),
            this,
            "You may$ sacrifice a creature$ to SN."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        if (event.isYes()) {
            event.processTargetPermanent(game, (final MagicPermanent creature) -> {
                final MagicPermanent permanent = event.getPermanent();
                game.doAction(new SacrificeAction(creature));
                game.doAction(new ChangeCountersAction(
                    event.getSource(),
                    permanent,
                    MagicCounterType.PlusOne,
                    amount
                ));
                game.addEvent(executeTrigger(
                    game,
                    permanent,
                    MagicPayedCost.NO_COST
                ));
            });
        }
    }
}
