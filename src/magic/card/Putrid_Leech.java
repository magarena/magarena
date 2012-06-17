package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayLifeEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPlayAbilityEvent;
import magic.model.event.MagicTiming;

public class Putrid_Leech {
    public static final MagicPermanentActivation A = new MagicPermanentActivation( 
            new MagicCondition[]{MagicCondition.ABILITY_ONCE_CONDITION,MagicCondition.TWO_LIFE_CONDITION},
            new MagicActivationHints(MagicTiming.Pump),
            "Pump") {

        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            return new MagicEvent[]{
                new MagicPayLifeEvent(source,source.getController(),2),
                new MagicPlayAbilityEvent((MagicPermanent)source)};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    source + " gets +2/+2 until end of turn.");
        }

        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
            final MagicPermanent permanent=(MagicPermanent)data[0];
            game.doAction(new MagicChangeTurnPTAction(permanent,2,2));
        }
    };
}
