package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDrawAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;

public class Enclave_Cryptologist {
    public static final MagicPermanentActivation A2 = new MagicPermanentActivation(
            new MagicCondition[]{
                MagicCondition.CHARGE_COUNTER_CONDITION,
                MagicCondition.CAN_TAP_CONDITION,
            },
            new MagicActivationHints(MagicTiming.Draw),
            "Draw") {

        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final MagicPlayer player = source.getController();
            final int amount = source.getCounters(MagicCounterType.Charge);
            final String description = amount >= 3 ?
                    " draws a card." :
                    " draws a card, then discards a card.";
            return new MagicEvent(
                    source,
                    player,
                    new Object[]{amount,player,source},
                    this,
                    player + description);
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicPlayer player = (MagicPlayer)data[1];
            game.doAction(new MagicDrawAction(player,1));
            if ((Integer)data[0] <= 2) {
                game.addEvent(new MagicDiscardEvent(
                        (MagicSource)data[2],
                        player,
                        1,
                        false));
            }
        }
    };
}
