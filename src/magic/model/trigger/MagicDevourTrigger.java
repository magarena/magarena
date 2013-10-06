package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPayedCost;
import magic.model.MagicType;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicSacrificeTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicOtherPermanentTargetFilter;
import magic.model.target.MagicTargetHint;

public class MagicDevourTrigger extends MagicWhenComesIntoPlayTrigger {

    private final int amount;

    public MagicDevourTrigger(final int amount) {
        this.amount = amount;
    }

    @Override
    public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent perm,
            final MagicPayedCost payedCost) {
        final MagicTargetFilter<MagicPermanent> targetFilter = new MagicOtherPermanentTargetFilter(
            MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,
            perm
        );
        final MagicTargetChoice targetChoice = new MagicTargetChoice(
            targetFilter,
            MagicTargetHint.None,
            "a creature other than " + perm + " to sacrifice"
        );
        return (perm.getController().getNrOfPermanents(MagicType.Creature) > 1) ?
            new MagicEvent(
                perm,
                new MagicMayChoice(
                    targetChoice
                ),
                MagicSacrificeTargetPicker.create(),
                this,
                "You may$ sacrifice a creature$ to SN."
            ) :
            MagicEvent.NONE;
    }

    @Override
    public boolean usesStack() {
        return false;
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        if (event.isYes()) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final MagicPermanent permanent = event.getPermanent();
                    game.doAction(new MagicSacrificeAction(creature));
                    game.doAction(new MagicChangeCountersAction(
                        permanent,
                        MagicCounterType.PlusOne,
                        amount,
                        true
                    ));
                    final MagicEvent newEvent = executeTrigger(
                        game,
                        permanent,
                        MagicPayedCost.NO_COST
                    );
                    if (newEvent.isValid()) {
                        game.addEvent(newEvent);
                    }
                }
            });
        }
    }
}
