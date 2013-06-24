package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.target.MagicWeakenTargetPicker;

public abstract class MagicWeakenCreatureActivation extends MagicPermanentActivation {

    public MagicWeakenCreatureActivation(
            final MagicActivationHints hints,
            final String text) {
        super(MagicActivation.NO_COND,hints,text);
    }

    public MagicWeakenCreatureActivation(
            final MagicCondition[] conds,
            final MagicActivationHints hints,
            final String text) {
        super(conds,hints,text);
    }

    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            MagicTargetChoice.NEG_TARGET_CREATURE,
            new MagicWeakenTargetPicker(1,1),
            this,
            "Put a -1/-1 counter on target creature$."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.processTargetPermanent(game,new MagicPermanentAction() {
            public void doAction(final MagicPermanent creature) {
                game.doAction(new MagicChangeCountersAction(
                    creature,
                    MagicCounterType.MinusOne,
                    1,
                    true
                ));
            }
        });
    }
}
