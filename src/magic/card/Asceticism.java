package magic.card;

import magic.model.*;
import magic.model.action.MagicRegenerateAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.target.MagicRegenerateTargetPicker;

public class Asceticism {

    public static final MagicPermanentActivation A1 = new MagicPermanentActivation(
            new MagicCondition[]{MagicManaCost.ONE_GREEN.getCondition()},
            new MagicActivationHints(MagicTiming.Pump,true),
            "Regen") {

        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE_GREEN)};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    MagicRegenerateTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Regenerate target creature$.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicPermanent creature = event.getTarget(game,choiceResults,0);
            if (creature != null) {
                game.doAction(new MagicRegenerateAction(creature));
            }
        }
    };
}
