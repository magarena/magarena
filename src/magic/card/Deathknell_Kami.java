package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;

public class Deathknell_Kami {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicConditionFactory.ManaCost("{2}")},
            new MagicActivationHints(MagicTiming.Pump),
            "Pump") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{new MagicPayManaCostEvent(
                    source,
                    source.getController(),
                    MagicManaCost.create("{2}"))};
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    this,
                    "SN gets +1/+1 until end of turn.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            game.doAction(new MagicChangeTurnPTAction(permanent,1,1));
            game.doAction(new MagicChangeStateAction(
                    permanent,
                    MagicPermanentState.SacrificeAtEndOfTurn,
                    true));
        }
    };
}
