package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPermanentAction;
import magic.model.event.MagicEvent;
import magic.model.choice.MagicTargetChoice;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.target.MagicPumpTargetPicker;

public class Ulvenwald_Bear {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            if (game.getCreatureDiedThisTurn()) {
                return new MagicEvent(
                        permanent,
                        player,
                        MagicTargetChoice.POS_TARGET_CREATURE,
                        MagicPumpTargetPicker.create(),
                        MagicEvent.NO_DATA,
                        this,
                        player + " puts two +1/+1 counters on target creature$.");
            }
            return MagicEvent.NONE;
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeCountersAction(
                            creature,
                            MagicCounterType.PlusOne,
                            2,
                            true));
                }
            });
        }
    };
}
