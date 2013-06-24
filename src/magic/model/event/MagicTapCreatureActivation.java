package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.target.MagicTapTargetPicker;

public abstract class MagicTapCreatureActivation extends MagicPermanentActivation {

    public MagicTapCreatureActivation(
            final MagicActivationHints hints,
            final String text) {
        super(MagicActivation.NO_COND,hints,text);
    }

    public MagicTapCreatureActivation(
            final MagicCondition[] conds,
            final MagicActivationHints hints,
            final String text) {
        super(conds,hints,text);
    }

    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            MagicTargetChoice.NEG_TARGET_CREATURE,
            MagicTapTargetPicker.Tap,
            this,
            "Tap target creature$."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.processTargetPermanent(game,new MagicPermanentAction() {
            public void doAction(final MagicPermanent creature) {
                game.doAction(new MagicTapAction(creature,true));
            }
        });
    }
}
